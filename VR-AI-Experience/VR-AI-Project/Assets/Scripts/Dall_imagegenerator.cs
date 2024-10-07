using UnityEngine;
using UnityEngine.Networking;
using System;
using System.Collections;
using System.IO;

public class Dall_imagegenerator : MonoBehaviour
{
    string apiKey = Secrets.openAIAPIKey;
    string apiUrl = "https://api.openai.com/v1/images/generations";

    string imageUrl;

    public static Dall_imagegenerator singleton { get; private set; }
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

    public IEnumerator GenerateImage(string imageName, string prompt)
    {
        ImageGenerationRequest requestData = new ImageGenerationRequest
        {
            model = "dall-e-3",
            prompt = prompt,
            size = "1024x1024",
            quality = "standard",
            n = 1
        };

        // Convert JSON data object to JSON string
        string jsonData = JsonUtility.ToJson(requestData);

        // Log JSON data for debugging
        //Debug.Log("Request Data: " + jsonData);

        using (UnityWebRequest request = UnityWebRequest.Post(apiUrl, jsonData, "application/json"))
        {
            // Set headers
            request.SetRequestHeader("Authorization", "Bearer " + apiKey);

            // Send request
            yield return request.SendWebRequest();

            // Check for errors
            if (request.result == UnityWebRequest.Result.ConnectionError ||
                request.result == UnityWebRequest.Result.ProtocolError)
            {
                Debug.LogError("Error: " + request.error);
            }
            else
            {
                // Parse response
                string jsonResponse = request.downloadHandler.text;
                //Debug.Log("Response: " + jsonResponse);

                // Check for 400 Bad Request
                if (request.responseCode == 400)
                {
                    Debug.LogError("Bad Request: " + jsonResponse);
                }
                else
                {
                    // Extract image URL from the response
                    DALLEImageResponse imageResponse = JsonUtility.FromJson<DALLEImageResponse>(jsonResponse);

                    // Ensure data array is not null and contains at least one element
                    if (imageResponse.data != null && imageResponse.data.Length > 0)
                    {
                        imageUrl = imageResponse.data[0].url;
                        Debug.Log("Image URL: " + imageUrl);
                    }
                    else
                    {
                        Debug.LogError("No image URL found in response.");
                    }
                }
            }
            using (UnityWebRequest www = UnityWebRequest.Get(imageUrl))
            {
                string downloadPath = Path.Combine(Application.persistentDataPath, "generated_images/" + imageName + "/" + imageName + ".png");
                www.downloadHandler = new DownloadHandlerFile(downloadPath);
                yield return www.SendWebRequest();

                if (www.result != UnityWebRequest.Result.Success)
                {
                    Debug.LogError(www.error);
                }
                else
                {
                    Debug.Log(www.result);
                }
            }
        }
    }

    [Serializable]
    public class DALLEImageResponse
    {
        public DALLEImage[] data;
    }

    [Serializable]
    public class DALLEImage
    {
        public string url;
    }

    [System.Serializable]
    public class ImageGenerationRequest
    {
        public string model;
        public string prompt;
        public string size;
        public string quality;
        public int n;
    }
}
