using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;
using static Dall_imagegenerator;

public class GPTManager : MonoBehaviour
{
    string jsontest = "{\r\n\t\"id\": \"chatcmpl-8ssjpPDs3zwRytTSzonJyGs2BuCvC\",\r\n\t\"object\": \"chat.completion\",\r\n\t\"created\": 1708090817,\r\n\t\"model\": \"gpt-3.5-turbo-0613\",\r\n\t\"choices\": [\r\n\t\t{\r\n\t\t\t\"index\": 0,\r\n\t\t\t\"message\": {\r\n\t\t\t\t\"role\": \"assistant\",\r\n\t\t\t\t\"content\": \"Yo, brusjan, hva skjer? Du ruller rundt i en fet BMW, det hores badass ut! Hva er historien bak den bilen?\"\r\n\t\t\t},\r\n\t\t\t\"logprobs\": null,\r\n\t\t\t\"finish_reason\": \"stop\"\r\n\t\t}\r\n\t],\r\n\t\"usage\": {\r\n\t\t\"prompt_tokens\": 27,\r\n\t\t\"completion_tokens\": 38,\r\n\t\t\"total_tokens\": 65\r\n\t},\r\n\t\"system_fingerprint\": null\r\n}";
    string systemPrompt = "";
    string apiKey = Secrets.openAIAPIKey;
    string url = "https://api.openai.com/v1/chat/completions";
    Text Texttext;
    string postData = "{\n\t\"messages\": [\n\t\t{\n\t\t\t\"role\": \"system\",\n\t\t\t\"content\": \"Du er en brusjan.\"\n\t\t},\n\t\t{\n\t\t\t\"role\": \"user\",\n\t\t\t\"content\": \"walla brusjan, fet BMW!\"\n\t\t}\n\t],\n\t\"model\": \"gpt-3.5-turbo\"\n}\n";

    [SerializeField] TextMeshPro textMeshPro;

    public Dictionary<string, string> returnStrings = new Dictionary<string, string>();


    public static GPTManager singleton { get; private set; }
    private void Awake()
    {
        if (singleton != null && singleton != this)
        {
            Destroy(this.gameObject);
        }
        else
        {
            singleton = this;
            DontDestroyOnLoad(this);
        }
    }

    IEnumerator GetDatasTest()
    {
        ChatCompletion chatCompletion = JsonUtility.FromJson<ChatCompletion>(jsontest);
        textMeshPro.text = chatCompletion.choices[0].message.content;
        yield return chatCompletion;
    }

    public IEnumerator GenerateGPTResponse(string prompt, float temperature = 1)
    {
        List<Message> messages = new List<Message>();
        Message message = new Message();
        message.role = "user";
        message.content = prompt;
        messages.Add(message);

        ChatCompletionRequest requestData = new ChatCompletionRequest
        {
            model = "gpt-3.5-turbo",
            messages = messages,
            temperature = temperature
        };

        // Convert JSON data object to JSON string
        string jsonData = JsonUtility.ToJson(requestData);

        using (UnityWebRequest request = UnityWebRequest.Post(url, jsonData, "application/json"))
        {
            request.SetRequestHeader("Authorization", "Bearer " + apiKey);

            yield return request.SendWebRequest();

            if (request.result != UnityWebRequest.Result.Success)
            {
                Debug.LogError(request.error);

                if (request.error.Contains("500"))
                {
                    Debug.Log("Trying GPT call again in two seconds");
                    yield return new WaitForSeconds(2);
                    yield return GenerateGPTResponse(prompt, temperature);
                    yield break;
                }
            }
            else
            {
                string json = request.downloadHandler.text;
                ChatCompletion chatCompletion = JsonUtility.FromJson<ChatCompletion>(json);

                if (!returnStrings.ContainsKey(prompt))
                {
                    returnStrings.Add(prompt, chatCompletion.choices[0].message.content);
                    print("added returnString: " + prompt + " | " + chatCompletion.choices[0].message.content);
                }
                else
                {
                    returnStrings[prompt] = chatCompletion.choices[0].message.content;
                    print("changed returnString: " + prompt + " | " + chatCompletion.choices[0].message.content);
                }
            }
        }

        yield break;
    }
    [Serializable]
    public class ChatCompletion
    {
        public string id;
        public long created;
        public string model;
        public List<Choice> choices;
        public object system_fingerprint;
    }
    [Serializable]
    public class Message
    {
        public string role;
        public string content;
    }
    [Serializable]
    public class Choice
    {
        public int index;
        public Message message;
    }

    [Serializable]
    public class ChatCompletionRequest
    {
        public string model;
        public List<Message> messages;
        public float temperature;
    }
}
