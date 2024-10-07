using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class EndingSceneInfiteBridge : MonoBehaviour
{
    public AudioClip songClip; // Audio clip to be played
    public Transform playerTransform;
    public float triggerYPosition = 0f; // Y position at which the song starts fading in
    public float fadeDuration = 5f; // Duration of fade-in effect

    private AudioSource audioSource; // Modified to be private
    private float maxVolume; // Maximum volume of the audio clip

    private bool hasStartedFade = false;

    void Start()
    {
        // Create an AudioSource component and attach it to the GameObject
        audioSource = gameObject.AddComponent<AudioSource>();

        // Assign the song clip to the AudioSource
        audioSource.clip = songClip;

        // Calculate the maximum volume of the audio clip
        maxVolume = GetMaxVolume(songClip);
    }

    void Update()
    {
        // Check if the player's Y position is below the trigger point and the fade-in hasn't started yet
        if (playerTransform.position.y < triggerYPosition && !hasStartedFade)
        {
            Debug.Log("Starting fade-in.");
            StartCoroutine(FadeIn());
            hasStartedFade = true;
        }
    }

    IEnumerator FadeIn()
    {
        float timer = 0f;
        float startVolume = 0f;

        // Store the initial volume
        startVolume = audioSource.volume;

        Debug.Log("Fade-in duration: " + fadeDuration);

        // Gradually increase the volume over time
        while (timer < fadeDuration)
        {
            timer += Time.deltaTime;
            float lerpValue = timer / fadeDuration;
            float targetVolume = Mathf.Lerp(startVolume, maxVolume, lerpValue);
            audioSource.volume = targetVolume;
            Debug.Log("Lerp value: " + lerpValue + ", Target Volume: " + targetVolume + ", Current Volume: " + audioSource.volume);
            yield return null;
        }

        // Ensure the volume reaches the target value
        audioSource.volume = maxVolume;

        Debug.Log("Volume reached target value. Starting song playback.");
        // Start playing the song
        audioSource.Play();
    }

    float GetMaxVolume(AudioClip clip)
    {
        float[] samples = new float[clip.samples * clip.channels];
        clip.GetData(samples, 0);
        float max = 0;
        for (int i = 0; i < samples.Length; i++)
        {
            float sample = Mathf.Abs(samples[i]);
            if (sample > max)
            {
                max = sample;
            }
        }
        return max;
    }
}
