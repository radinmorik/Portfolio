using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using static UnityEngine.Mesh;

public class ImagePlaceholder : MonoBehaviour
{
    [SerializeField] ImageData imageData = new ImageData();
    [SerializeField] GameObject canvas;
    bool generated = false;

    [SerializeField] bool staticSize = false;

    private void Start()
    {
        Invoke("LateStart", 1);
    }

    private void LateStart()
    {
        if (GenerateManager.singleton.imageDatas.Count > 0)
        {
            //imageData = GenerateManager.singleton.GetRandomImageData();
            imageData = GenerateManager.singleton.GetLatestImageData();
        }
        if (imageData.texture == null)
        {
            gameObject.SetActive(false);
        }
        else
        {
            LoadImage();
        }
    }
    public void LoadImage()
    {
        if (generated)
        {
            Debug.LogWarning("Tried loading image twice on ImagePlaceholder");
            return;
        }

        if (imageData.texture != null)
        {
            canvas.GetComponent<MeshRenderer>().material.mainTexture = imageData.texture;

            DestroyText();
        }
        else
        {
            ChangeText("Failed\n" + imageData.imageName);
        }

        if (!staticSize)
        {
            FixSize(imageData.targetHeight);
        }

        generated = true;
    }

    private void FixSize(float targetHeight)
    {
        canvas.transform.localScale = new Vector3(targetHeight, targetHeight, 1);
    }

    public void ChangeText(string newText)
    {
        TextMeshPro child = GetComponentInChildren<TextMeshPro>();
        child.text = newText;
    }
    private void DestroyText()
    {
        TextMeshPro child = GetComponentInChildren<TextMeshPro>();
        Destroy(child.gameObject);
    }
}
