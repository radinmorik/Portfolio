using SimpleJSON;
using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using UnityEngine;
using UnityEngine.Networking;

public class MeshyManager : MonoBehaviour
{
    string postURL = "https://api.meshy.ai/v2/text-to-3d";
    string getURL = "https://api.meshy.ai/v2/text-to-3d/";

    [SerializeField] string artStyle = "realistic";
    [SerializeField] string textureRichness = "low";

    public static MeshyManager singleton { get; private set; }
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

    void Start()
    {
        //StartCoroutine(CallGenerateAPI());
        //StartCoroutine(CallRefineAPI("018e3294-117a-79c6-a746-576a834506ec", "unfinished"));
        //StartCoroutine(CallRetrieveAPI("018f0fb5-ae6a-71d9-bbfe-70c71cba6f39", "test_mesh"));
    }

    public IEnumerator GenerateMesh(string meshName, string prompt)
    {
        yield return StartCoroutine(CallPreviewAPI(meshName, prompt));
    }

    IEnumerator CallPreviewAPI(string meshName, string prompt = "banana")
    {
        string payload = "{ \"mode\": \"preview\", \"prompt\": \"" + prompt + "\", \"art_style\": \"" + artStyle + "\" }";

        using (UnityWebRequest www = UnityWebRequest.Post(postURL, payload, "application/json"))
        {
            www.SetRequestHeader("Authorization", "Bearer " + Secrets.meshyAPIKey);

            yield return www.SendWebRequest();

            if (www.result != UnityWebRequest.Result.Success)
            {
                Debug.LogError(www.error);
            }
            else
            {
                Debug.Log(www.result);
                print(www.downloadHandler.text);

                ResponseId json = JsonUtility.FromJson<ResponseId>(www.downloadHandler.text);

                yield return new WaitForSeconds(30);

                yield return StartCoroutine(CallRefineAPI(json.result, meshName));
            }
        }
    }

    IEnumerator CallRefineAPI(string taskId, string meshName)
    {
        string payload = "{ \"mode\": \"refine\", \"preview_task_id\": \"" + taskId + "\", \"texture_richness\": \"" + textureRichness + "\" }";

        using (UnityWebRequest www = UnityWebRequest.Post(postURL, payload, "application/json"))
        {
            www.SetRequestHeader("Authorization", "Bearer " + Secrets.meshyAPIKey);

            yield return www.SendWebRequest();

            if (www.result != UnityWebRequest.Result.Success)
            {
                Debug.LogWarning(www.error);
                yield return new WaitForSeconds(5);
                yield return StartCoroutine(CallRefineAPI(taskId, meshName));
            }
            else
            {
                Debug.Log(www.result);
                print(www.downloadHandler.text);

                ResponseId json = JsonUtility.FromJson<ResponseId>(www.downloadHandler.text);

                yield return new WaitForSeconds(30);

                yield return StartCoroutine(CallRetrieveAPI(json.result, meshName));
            }
        }
    }

    IEnumerator CallRetrieveAPI(string taskId, string meshName)
    {
        using (UnityWebRequest www = UnityWebRequest.Get(getURL + taskId))
        {
            www.SetRequestHeader("Authorization", "Bearer " + Secrets.meshyAPIKey);

            yield return www.SendWebRequest();

            if (www.result != UnityWebRequest.Result.Success)
            {
                Debug.LogWarning(www.error);
                yield return new WaitForSeconds(5);
                yield return StartCoroutine(CallRetrieveAPI(taskId, meshName));
            }
            else
            {
                Debug.Log(www.result);
                print(www.downloadHandler.text);

                ResponseTask json = JsonUtility.FromJson<ResponseTask>(www.downloadHandler.text);

                if (json.status == "SUCCEEDED")
                {
                    yield return StartCoroutine(DownloadMesh(json.model_urls.obj, json.texture_urls[0].base_color, meshName));
                }
                else if (json.status == "IN_PROGRESS")
                {
                    yield return new WaitForSeconds(101 - json.progress);
                    yield return StartCoroutine(CallRetrieveAPI(taskId, meshName));
                }
                else if (json.status == "PENDING")
                {
                    Debug.Log("Meshy generation pending, waiting 2 minutes before calling again");
                    yield return new WaitForSeconds(120);
                    yield return StartCoroutine(CallRetrieveAPI(taskId, meshName));
                }
                else
                {
                    Debug.LogWarning("Meshy generation failed or expired");
                }
            }
        }
    }

    IEnumerator DownloadMesh(string downloadMeshURL, string downloadTextureURL, string meshName)
    {
        using (UnityWebRequest www = UnityWebRequest.Get(downloadMeshURL))
        {
            string downloadPath = Path.Combine(Application.persistentDataPath, "generated_meshes/" + meshName + "/" + meshName + ".obj");
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
        using (UnityWebRequest www = UnityWebRequest.Get(downloadTextureURL))
        {
            string downloadPath = Path.Combine(Application.persistentDataPath, "generated_meshes/" + meshName + "/" + meshName + "_texture.png");
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
class ResponseId
{
    public string result;
}
[Serializable]
class ResponseTask
{
    public string id;
    public ModelURLs model_urls;
    public int progress;
    public string status;
    public List<TextureURLs> texture_urls;
}
[Serializable]
class ModelURLs
{
    public string obj;
}
[Serializable]
class TextureURLs
{
    public string base_color;
}