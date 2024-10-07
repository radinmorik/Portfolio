//using OpenAI_API;
//using OpenAI_API.Chat;
//using OpenAI_API.Models;
//using System;
//using System.Collections;
//using System.Collections.Generic;
//using TMPro;
//using UnityEngine;
//using UnityEngine.UI;

//public class AITest : MonoBehaviour
//{
//    private const string SystemPrompt = "";
//    public TMP_Text textField;
//    public TMP_InputField inputField;
//    public Button okButton;

//    //TEMP
//    public String openAiApiKey;


//    private OpenAIAPI api;
//    private List<ChatMessage> messages;


//    // Start is called before the first frame update
//    void Start()
//    {
//        api = new OpenAIAPI(openAiApiKey);
//        StartConversation();
//        okButton.onClick.AddListener(() => GetResponse());
        
//    }

//    private void StartConversation()
//    {
//        messages = new List<ChatMessage> {
//            new ChatMessage(ChatMessageRole.System, SystemPrompt)
//        };
//    }

//    private async void GetResponse()
//    {
//        if (inputField.text.Length < 1)
//        {
//            return;
//        }

//        okButton.enabled = false;

//        ChatMessage userMessage = new ChatMessage
//        {
//            Role = ChatMessageRole.User,
//            Content = inputField.text
//        };
//        if (userMessage.Content.Length > 100) 
//        {
//            userMessage.Content = userMessage.Content.Substring(0, 100);
//        }
//        Debug.Log(userMessage);

//        // Add mesage
//        messages.Add(userMessage);

//        // Update text
//        textField.text = string.Format("You: {0}", userMessage.Content);

//        // Clear
//        inputField.text = "";

//        // Send entire chat to OpenAI to get next message
//        var chatResult = await api.Chat.CreateChatCompletionAsync(new ChatRequest()
//        {
//            Model = Model.ChatGPTTurbo,
//            Temperature = 0.1,
//            MaxTokens = 50,
//            Messages = messages
//        });

//        // Get response message
//        ChatMessage responseMessage = new ChatMessage();
//        responseMessage.Role = chatResult.Choices[0].Message.Role;
//        responseMessage.Content = chatResult.Choices[0].Message.Content;
//        Debug.Log(responseMessage);

//        // Add response to list
//        messages.Add(responseMessage);

//        // Update textfield
//        textField.text = string.Format("You: {0}\n\nAssist: {1}", userMessage.Content, responseMessage.Content);

//        // Enable OK button
//        okButton.enabled = true;
//    }
//}
