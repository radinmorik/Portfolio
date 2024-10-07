using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;

public class Mail : MonoBehaviour
{
    [SerializeField] TextMeshPro mailText;

    void Start()
    {
        StartCoroutine(getMail());
    }

    public IEnumerator getMail()
    {
        string interactions = string.Join(", ", TrackingManager.singleton.GetRelevantKeywords(4));
        string prompt = "Write a personal letter to a student. You can make up their and your name (or other funny ways to refer to you both). Personalize the content based on the student's usual actions of: " + interactions;

        yield return StartCoroutine(GPTManager.singleton.GenerateGPTResponse(prompt, 1.3f));

        if (GPTManager.singleton.returnStrings.ContainsKey(prompt))
        {
            string returnString = GPTManager.singleton.returnStrings[prompt];
            mailText.text = returnString;
            Debug.Log("Set mail text to: " + returnString);
        }
        else
        {
            Debug.LogWarning("Key not found in GPT returnStrings: " + prompt);
        }
    }
}
