using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Net;
using UnityEngine;
using UnityEngine.Networking;
using static GPTManager;

public class TTSManager : MonoBehaviour
{
    string apiKey = Secrets.openAIAPIKey;
    string url = "https://api.openai.com/v1/audio/speech";

    public Dictionary<string, AudioClip> returnClips = new Dictionary<string, AudioClip>();

    public static TTSManager singleton { get; private set; }
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

    public IEnumerator GenerateAudio(string prompt, string voice = "fable")
    {
        TTSRequest requestData = new TTSRequest
        {
            model = "tts-1",
            voice = voice,
            input = prompt
        };

        // Convert JSON data object to JSON string
        string requestJSON = JsonUtility.ToJson(requestData);

        using (UnityWebRequest request = UnityWebRequest.Post(url, requestJSON, "application/json"))
        {
            request.SetRequestHeader("Authorization", "Bearer " + apiKey);

            string clipKey = prompt.Substring(0, 10);

            string downloadPath = Path.Combine(Application.persistentDataPath, "generated_audio/lecture/" + clipKey + ".mp3");
            request.downloadHandler = new DownloadHandlerFile(downloadPath);

            yield return request.SendWebRequest();

            if (request.result != UnityWebRequest.Result.Success)
                Debug.LogError(request.error);
            else
            {
                Debug.Log(request.result);

                StartCoroutine(GetAudioClip((clip) =>
                {
                    string clipKey = prompt.Substring(0, 10);
                    returnClips[clipKey] = clip;
                }, clipKey));
            }
        }

        yield break;
    }

    IEnumerator GetAudioClip(Action<AudioClip> onLoaded, string fileName)
    {
        using (UnityWebRequest www = UnityWebRequestMultimedia.GetAudioClip("file://" + Application.persistentDataPath + "/generated_audio/lecture/" + fileName + ".mp3", AudioType.MPEG))
        {
            yield return www.SendWebRequest();
            AudioClip myClip = DownloadHandlerAudioClip.GetContent(www);
            onLoaded.Invoke(myClip);
        }
    }

    [Serializable]
    public class TTSRequest
    {
        public string model;
        public string voice;
        public string input;
    }
}
