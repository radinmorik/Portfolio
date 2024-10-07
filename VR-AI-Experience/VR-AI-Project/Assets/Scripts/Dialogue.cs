using System;
using System.Collections;
using System.Collections.Generic;
using Unity.VisualScripting;
using UnityEngine;
using TMPro;
using UnityEngine.UI;
using System.Security;

public class Dialogue : MonoBehaviour
{
    GPTManager gptManager;

    [SerializeField] AudioSource audioSource;

    [SerializeField] string npcName = "Receptionist";

    [SerializeField] NPCLine currentLine;

    [SerializeField] TextMeshProUGUI npcText;
    [SerializeField] Button option1Button;
    [SerializeField] TextMeshProUGUI option1Text;
    [SerializeField] Button option2Button;
    [SerializeField] TextMeshProUGUI option2Text;

    // Start is called before the first frame update
    void Start()
    {
        gptManager = GPTManager.singleton;

        currentLine = gameObject.AddComponent<NPCLine>();
        StartCoroutine(currentLine.Fill(3));

        StartCoroutine(UpdateGUI());
    }

    public void Option1()
    {
        option1Button.interactable = false;
        option2Button.interactable = false;
        if (currentLine.option1 != null)
        {
            currentLine = currentLine.option1.npcLine;
        }
        StartCoroutine(currentLine.Fill(3));
        StartCoroutine(UpdateGUI());
    }
    public void Option2()
    {
        option1Button.interactable = false;
        option2Button.interactable = false;
        if (currentLine.option2 != null)
        {
            currentLine = currentLine.option2.npcLine;
        }
        StartCoroutine(currentLine.Fill(3));
        StartCoroutine(UpdateGUI());
    }

    IEnumerator UpdateGUI()
    {
        option1Button.interactable = false;
        option2Button.interactable = false;

        while (currentLine.line == null || currentLine.line == "")
        {
            yield return null;
        }
        npcText.text = npcName + ": " + currentLine.line;

        while (currentLine.clip == null)
        {
            yield return null;
        }
        audioSource.clip = currentLine.clip;
        audioSource.Play();

        while (currentLine.option1.line == null || currentLine.option1.line == "")
        {
            yield return null;
        }
        option1Text.text = "POKE TO REPLY: " + currentLine.option1.line;

        while (currentLine.option2.line == null || currentLine.option2.line == "")
        {
            yield return null;
        }
        option2Text.text = "POKE TO REPLY: " + currentLine.option2.line;

        yield return new WaitForSeconds(1);

        option1Button.interactable = true;
        option2Button.interactable = true;
    }
}

[Serializable]
class NPCLine : MonoBehaviour
{
    public string line;
    public PlayerLine option1;
    public PlayerLine option2;
    public AudioClip clip;

    public IEnumerator Fill(int depth)
    {
        if (line == "" || line == null)
        {
            //yield return generate random line
            Debug.LogWarning("Had to generate new NPC line");

            string interactions = string.Join(", ", TrackingManager.singleton.GetRelevantKeywords(2));
            string outfit = TrackingManager.singleton.outfit;
            string prompt = "Reply with one sentence. Greet the student going to their lecture with a unique combination of elements such as time of day, weather conditions, and a personalized message based on the " + outfit + "-wearing player's outfit and previous actions of: " + interactions;

            yield return StartCoroutine(GPTManager.singleton.GenerateGPTResponse(prompt, 1.2f));
            //yield return new WaitForSeconds(2);
            if (GPTManager.singleton.returnStrings.ContainsKey(prompt))
            {
                line = GPTManager.singleton.returnStrings[prompt];

                yield return TTSManager.singleton.GenerateAudio(line, "onyx");

                yield return new WaitForSeconds(1);

                string clipKey = line.Substring(0, 10);
                clip = TTSManager.singleton.returnClips[clipKey];
            }
            else
            {
                Debug.LogWarning("Key not found in GPT returnStrings: " + prompt);
            }
        }

        if (depth == 0)
        {
            yield break;
        }

        //generate replies based on line
        if (option1 == null)
        {
            print("npc " + depth);
            option1 = gameObject.AddComponent<PlayerLine>();
            string prompt = "Reply with one sentence. Write a nice reply to this sentence: " + line;

            yield return StartCoroutine(GPTManager.singleton.GenerateGPTResponse(prompt, 1.2f));
            //yield return new WaitForSeconds(2);
            option1.line = GPTManager.singleton.returnStrings[prompt];
        }
        if (option2 == null)
        {
            option2 = gameObject.AddComponent<PlayerLine>();
            string prompt = "Reply with one sentence. Write a mean reply to this sentence: " + line;
            yield return StartCoroutine(GPTManager.singleton.GenerateGPTResponse(prompt, 1.2f));
            //yield return new WaitForSeconds(2);
            option2.line = GPTManager.singleton.returnStrings[prompt];
        }

        StartCoroutine(option1.Fill(depth - 1));

        StartCoroutine(option2.Fill(depth - 1));

        yield break;
    }
}

[Serializable]
class PlayerLine : MonoBehaviour
{
    public string line;
    public NPCLine npcLine;

    public IEnumerator Fill(int depth)
    {
        if (line == "" || line == null)
        {
            //yield return generate random line
            Debug.LogWarning("Had to generate new Player line");
            string interactions = string.Join(", ", TrackingManager.singleton.GetRelevantKeywords(4));
            string prompt = "Write a generic reply a player could say to an NPC. The player previously having done these actions: " + interactions;
            yield return StartCoroutine(GPTManager.singleton.GenerateGPTResponse(prompt, 1.2f));
            //yield return new WaitForSeconds(2);
            if (GPTManager.singleton.returnStrings.ContainsKey(prompt))
            {
                line = GPTManager.singleton.returnStrings[prompt];
            }
            else
            {
                Debug.LogWarning("Key not found in GPT returnstrings: " + prompt);
            }
        }

        if (depth == 0)
        {
            yield break;
        }

        //generate reply based on line
        if (npcLine == null)
        {
            print("player " + depth);
            npcLine = gameObject.AddComponent<NPCLine>();
            string prompt = "Reply with one sentence. Write a reply to this sentence: " + line;
            yield return StartCoroutine(GPTManager.singleton.GenerateGPTResponse(prompt, 1.2f));
            //yield return new WaitForSeconds(2);
            //npcLine.line = GPTManager.singleton.returnStrings[prompt];

            if (GPTManager.singleton.returnStrings.ContainsKey(prompt))
            {
                npcLine.line = GPTManager.singleton.returnStrings[prompt];

                yield return TTSManager.singleton.GenerateAudio(npcLine.line, "onyx");

                yield return new WaitForSeconds(1);

                string clipKey = npcLine.line.Substring(0, 10);
                npcLine.clip = TTSManager.singleton.returnClips[clipKey];
            }
            else
            {
                Debug.LogWarning("Key not found in GPT returnStrings: " + prompt);
            }
        }

        StartCoroutine(npcLine.Fill(depth - 1));

        yield break;
    }
}
