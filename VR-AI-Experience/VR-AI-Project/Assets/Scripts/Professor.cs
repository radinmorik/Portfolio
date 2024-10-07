using System.Collections;
using System.Collections.Generic;
using System.IO;
using UnityEngine;

public class Professor : MonoBehaviour
{
    GPTManager gptManager;

    [SerializeField] AudioSource audioSource;

    [SerializeField] string lectureString;

    // Start is called before the first frame update
    void Start()
    {
        StartCoroutine(GenerateLecture());
    }

    IEnumerator GenerateLecture()
    {
        string interactions = string.Join(", ", TrackingManager.singleton.GetRelevantKeywords(5));
        string prompt = "Write a weird two minute long lecture held by a professor. You can make up the subject based on these key sentences: " + interactions;

        yield return StartCoroutine(GPTManager.singleton.GenerateGPTResponse(prompt, 1.2f));

        if (GPTManager.singleton.returnStrings.ContainsKey(prompt))
        {
            lectureString = GPTManager.singleton.returnStrings[prompt];
        }
        else
        {
            Debug.LogWarning("Key not found in GPT returnStrings: " + prompt);
            Debug.Log("Trying once more in 2 seconds");
            yield return new WaitForSeconds(2);
            if (GPTManager.singleton.returnStrings.ContainsKey(prompt))
            {
                lectureString = GPTManager.singleton.returnStrings[prompt];
            }
            else
            {
                Debug.LogError("GPT retry failed");
            }
        }

        // generate audio
        yield return TTSManager.singleton.GenerateAudio(lectureString);

        yield return new WaitForSeconds(2);

        // play audio file
        string clipKey = lectureString.Substring(0, 10);
        if (TTSManager.singleton.returnClips.ContainsKey(clipKey))
        {
            audioSource.clip = TTSManager.singleton.returnClips[clipKey];
        }
        else
        {
            Debug.LogWarning("Key not found in TTS returnClips: " + clipKey);
            Debug.Log("Trying once more in 2 seconds");
            yield return new WaitForSeconds(2);
            if (TTSManager.singleton.returnClips.ContainsKey(clipKey))
            {
                audioSource.clip = TTSManager.singleton.returnClips[clipKey];
            }
            else
            {
                Debug.LogError("TTS retry failed");
            }
        }

        audioSource.Play();

        yield return null;
    }
}
