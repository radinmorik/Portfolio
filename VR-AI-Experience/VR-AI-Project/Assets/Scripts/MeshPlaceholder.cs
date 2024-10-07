using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.XR.Interaction.Toolkit;
using UnityEngine.Events;

public class MeshPlaceholder : MonoBehaviour
{
    // Sets up generated object, either with mesh from meshy or just placeholder description
    // Deletes/deactivates itself when done

    [SerializeField] MeshData meshData = new MeshData();
    bool generated;

    [SerializeField] InteractionTracking interactionTracking;

    private void Start()
    {
        Invoke("LateStart", 1);
    }

    private void LateStart()
    {
        if (GenerateManager.singleton.meshDatas.Count > 0)
        {
            //meshData = GenerateManager.singleton.GetRandomMeshData();
            meshData = GenerateManager.singleton.GetLatestMeshData();
        }
        if (meshData.mesh == null)
        {
            Invoke("LateStart", 1);
            gameObject.SetActive(false);
        }
        else
        {
            LoadMesh();
        }
    }

    public void LoadMesh()
    {
        if (generated)
        {
            Debug.LogWarning("Tried loading mesh twice on MeshPlaceholder");
            return;
        }

        if (meshData.mesh != null)
        {
            GetComponent<MeshFilter>().mesh = meshData.mesh;
            GetComponent<MeshCollider>().sharedMesh = meshData.mesh;

            if (meshData.texture != null)
            {
                GetComponent<MeshRenderer>().material.mainTexture = meshData.texture;
            }

            DestroyText();
        }
        else
        {
            ChangeText("Failed\n" + meshData.meshName);
        }

        FixSize(meshData.targetHeight);

        interactionTracking.keywords = new string[] { meshData.meshName };

        AddComponents(meshData.kinematic, meshData.pickupable, meshData.edible);

        generated = true;
        gameObject.SetActive(true);
    }

    private void AddComponents(bool kinematic, bool pickupable, bool edible)
    {
        gameObject.GetComponent<Rigidbody>().isKinematic = kinematic;

        if (pickupable)
        {
            XRGrabInteractable grab = gameObject.AddComponent<XRGrabInteractable>();
            grab.useDynamicAttach = true;
            // add tracking on events

            grab.selectEntered.AddListener((args) => interactionTracking.TriggerTracking("Picked up"));
        }
        if (edible)
        {
            gameObject.AddComponent<Edible>();
        }
    }

    private void FixSize(float targetHeight)
    {
        transform.localScale = Vector3.one;
        float currentHeight = GetComponent<MeshFilter>().sharedMesh.bounds.size.y;
        transform.localScale *= targetHeight / currentHeight;
        transform.localPosition = new Vector3(transform.localPosition.x, transform.localPosition.y + targetHeight / 2 - 0.1f, transform.localPosition.z);
    }

    public void ChangeText(string newText)
    {
        TextMeshPro[] children = GetComponentsInChildren<TextMeshPro>();
        foreach (TextMeshPro child in children)
        {
            child.text = newText;
        }
    }

    private void DestroyText()
    {
        TextMeshPro[] children = GetComponentsInChildren<TextMeshPro>();
        foreach (TextMeshPro child in children)
        {
            Destroy( child.gameObject );
        }
    }
}
