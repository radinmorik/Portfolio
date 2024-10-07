using Dummiesman;
using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using Unity.VisualScripting;
using UnityEngine;

public class GenerateManager : MonoBehaviour
{
    // For håndtering av generering av objekter og malerier og sånt

    GPTManager gptManager;
    MeshyManager meshyManager;
    Dall_imagegenerator dalleManager;

    TrackingManager trackingManager;

    public List<MeshData> meshDatas = new List<MeshData>();
    public List<ImageData> imageDatas = new List<ImageData>();

    //[SerializeField] MeshPlaceholder[] meshPlaceholders;
    //[SerializeField] ImagePlaceholder[] imagePlaceholders;

    OBJLoader objLoader = new OBJLoader();

    [SerializeField] bool forceGenerate;
    [SerializeField] bool skipGPT;
    [SerializeField] bool skipMeshy;

    public static GenerateManager singleton { get; private set; }
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

    // Start is called before the first frame update
    void Start()
    {
        gptManager = GPTManager.singleton;
        meshyManager = MeshyManager.singleton;
        dalleManager = Dall_imagegenerator.singleton;
        trackingManager = TrackingManager.singleton;

        //GenerateAllMeshes();
    }

    private void OnLevelWasLoaded(int level)
    {
        Debug.Log("Level was loaded");

        gptManager = GPTManager.singleton;
        meshyManager = MeshyManager.singleton;
        dalleManager = Dall_imagegenerator.singleton;
        trackingManager = TrackingManager.singleton;

        if (level == 0 || level == 3)
        {
            //Destroy(gameObject);
            Debug.Log("Not generating because level was index 0 or 3");
        }
        else
        {
            GenerateMeshes(3);
            GenerateImages(3);
        }
    }

    public MeshData GetRandomMeshData()
    {
        MeshData meshData = meshDatas[UnityEngine.Random.Range(0, meshDatas.Count)];
        return meshData;
    }
    public ImageData GetRandomImageData()
    {
        ImageData imageData = imageDatas[UnityEngine.Random.Range(0, imageDatas.Count)];
        return imageData;
    }

    public MeshData GetLatestMeshData()
    {
        MeshData meshData = meshDatas[meshDatas.Count - 1];
        meshDatas.RemoveAt(meshDatas.Count - 1);
        meshDatas.Insert(0, meshData);
        return meshData;
    }
    public ImageData GetLatestImageData()
    {
        ImageData imageData = imageDatas[imageDatas.Count - 1];
        imageDatas.RemoveAt(imageDatas.Count - 1);
        imageDatas.Insert(0, imageData);
        return imageData;
    }

    public void GenerateMeshes(int amount)
    {
        bool enoughTracking = TrackingManager.singleton.trackedKeywords.Count > 0;
        bool notTooLate = GameManager.singleton.mapProgression < GameManager.singleton.endingIndex - 3;

        if ((enoughTracking && notTooLate) || forceGenerate)
        {
            for (int i = 0; i < amount; i++)
            {
                StartCoroutine(GenerateMesh());
            }
        }
        else
        {
            if (!notTooLate)
            {
                Debug.Log("Too late in game to generate meshes");
            }
            else
            {
                Debug.Log("Not enough trackedKeywords to generate meshes");
            }
        }
    }

    public void GenerateImages(int amount)
    {
        bool enoughTracking = TrackingManager.singleton.trackedKeywords.Count > 0;
        bool notTooLate = GameManager.singleton.mapProgression < GameManager.singleton.endingIndex - 2;

        if ((enoughTracking && notTooLate) || forceGenerate)
        {
            for (int i = 0; i < amount; i++)
            {
                StartCoroutine(GenerateImage());
            }
        }
        else
        {
            if (!notTooLate)
            {
                Debug.Log("Too late in game to generate images");
            }
            else
            {
                Debug.Log("Not enough trackedKeywords to generate images");
            }
        }
    }

    public void GenerateAllMeshes()
    {
        /*for (int i = 0; i < meshPlaceholders.Length; i++)
        {
            print("test0");
            StartCoroutine(GenerateMesh(meshPlaceholders[i]));

            //Thread thread = new Thread(() => GenerateMesh(meshPlaceholders[i]));
            //thread.Start();

            meshPlaceholders[i] = null;
        }*/
    }

    IEnumerator GenerateMesh() // kanskje endre til tråd
    {
        string meshName;
        string prompt;
        float targetHeight;
        bool kinematic;
        bool pickupable;
        bool edible;

        if (!skipGPT)
        {
            string test = string.Join(", ", trackingManager.GetRelevantKeywords(4));

            string testPrompt = "Make a prompt to generate a 3D object in a game. The object should be themed after these sentences: " + test + "\r\n\r\nAnswer in the JSON format with the keys:\r\n{\r\n\"meshName\" : string - one to two words, with underscore as space\r\n\"prompt\": string - one sentence descripting the object\r\n\"targetHeight\": float - height of object in meters\r\n\"pickupable\": bool - should the player be able to pick up the object\r\n\"edible\": bool - should the player be able to eat the object\r\n}";

            if (!gptManager.returnStrings.ContainsKey(testPrompt))
            {
                yield return gptManager.GenerateGPTResponse(testPrompt);
            }

            yield return null;

            if (!gptManager.returnStrings.ContainsKey(testPrompt))
            {
                yield break;
            }

            ResponseMesh responseMesh = new ResponseMesh();

            print("mesh gpt response: " + gptManager.returnStrings[testPrompt]);
            try
            {
                responseMesh = JsonUtility.FromJson<ResponseMesh>(gptManager.returnStrings[testPrompt]);
            }
            catch (ArgumentException e)
            {
                Debug.LogError(e.Message);
            }

            if (responseMesh.targetHeight < .8f)
            {
                responseMesh.pickupable = true;
            }

            // Kall GPT med keywords for å generere prompt og data
            meshName = responseMesh.meshName;
            prompt = responseMesh.prompt;
            targetHeight = responseMesh.targetHeight;
            kinematic = !responseMesh.pickupable;
            pickupable = responseMesh.pickupable;
            edible = responseMesh.edible;

            targetHeight = Mathf.Clamp(targetHeight, 0.1f, 3.0f);
        }
        else
        {
            // Use standard GPT values
            meshName = "test_mesh";
            prompt = "A bright pink top hat with a banana inside.";
            targetHeight = .5f;
            kinematic = false;
            pickupable = true;
            edible = false;
        }

        // Se om slik mesh allerede finnes, kall på meshyManager hvis det ikke finnes
        string path = Path.Combine(Application.persistentDataPath, "generated_meshes/" + meshName);
        if (!Directory.Exists(path) || forceGenerate)
        {
            if (skipMeshy)
            {
                yield break;
            }
            yield return meshyManager.GenerateMesh(meshName, prompt);
        }

        MeshData meshData = new MeshData();
        meshData.meshName = meshName;
        meshData.targetHeight = targetHeight;
        meshData.kinematic = kinematic;
        meshData.pickupable = pickupable;
        meshData.edible = edible;

        try
        {
            // Last inn mesh fra fil med OBJImporter
            meshData.mesh = objLoader.Load(path + "/" + meshName + ".obj");
            objLoader = new OBJLoader();

            // Last inn texture fra fil med OBJImporter
            meshData.texture = ImageLoader.LoadTexture(path + "/" + meshName + "_texture.png");
        }
        catch (DirectoryNotFoundException directoryNotFound)
        {
            Debug.LogError(directoryNotFound.Message);
        }

        if (meshData.mesh== null)
        {
            yield break;
        }

        meshDatas.Add(meshData);

        //meshPlaceholder.LoadMesh(mesh, texture, targetHeight, meshName, kinematic, pickupable, edible);
        /*UnityMainThreadDispatcher.Instance().Enqueue(() => {
            meshPlaceholder.Generate(mesh, texture, .2f, prompt, false, true, true);
        });*/

        yield return null;
    }

    IEnumerator GenerateImage()
    {
        string test = string.Join(", ", trackingManager.GetRelevantKeywords(4));

        string imageGPTPrompt = "make a prompt to generate a painting in a game. The image should be themed after these sentences: " + test + "\r\n\r\nAnswer in the JSON format:\r\n{\r\nimageName : string - one to two words, with underscore as space\r\nprompt: string - one sentence descripting the image\r\ntargetHeight: float - height of canvas in meters\r\n}";

        if (!gptManager.returnStrings.ContainsKey(imageGPTPrompt))
        {
            yield return gptManager.GenerateGPTResponse(imageGPTPrompt);
        }

        if (!gptManager.returnStrings.ContainsKey(imageGPTPrompt))
        {
            yield break;
        }

        ResponseImage responseImage = new ResponseImage();

        //print("image gpt before? " + gptManager.returnStrings[imageGPTPrompt]);
        try
        {
            responseImage = JsonUtility.FromJson<ResponseImage>(gptManager.returnStrings[imageGPTPrompt]);
        }
        catch (ArgumentException e)
        {
            Debug.LogError(e.Message);
        }

        // Kall GPT med keywords for å generere prompt og data
        string imageName = responseImage.imageName;
        string prompt = responseImage.prompt;
        float targetHeight = responseImage.targetHeight;

        // Se om bilde allerede finnes, kall på bildegenerering hvis det ikke finnes
        string path = Path.Combine(Application.persistentDataPath, "generated_images/" + imageName);
        if (!Directory.Exists(path))
        {
            yield return dalleManager.GenerateImage(imageName, prompt);
        }

        ImageData imageData = new ImageData();
        imageData.imageName = imageName;
        imageData.targetHeight = targetHeight;

        // Last inn bilde fra fil med OBJImporter
        //Texture2D texture = ImageLoader.LoadTexture("path");
        try
        {
            // Last inn texture fra fil med OBJImporter
            imageData.texture = ImageLoader.LoadTexture(path + "/" + imageName + ".png");
        }
        catch (DirectoryNotFoundException directoryNotFound)
        {
            Debug.LogError(directoryNotFound.Message);
        }

        imageDatas.Add(imageData);

        yield return null;
    }

    [Serializable]
    class ResponseImage
    {
        public string imageName;
        public string prompt;
        public float targetHeight;
    }

    [Serializable]
    class ResponseMesh
    {
        public string meshName;
        public string prompt;
        public float targetHeight;
        public bool pickupable;
        public bool edible;
    }
}
