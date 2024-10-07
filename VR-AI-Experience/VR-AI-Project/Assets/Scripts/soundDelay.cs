using System.Collections;
using UnityEngine;

public class SoundDelay : MonoBehaviour
{
    public AudioClip[] sounds;
    public float minTimeBetweenSounds = 3.0f;
    public float maxTimeBetweenSounds = 20.0f;
    public int timesToPlay = 20;

    public AudioSource audioSource; // Assign this in the Unity Editor

    void Start()
    {
        // Start playing sounds
        StartCoroutine(SoundLoop());
    }

    private IEnumerator SoundLoop()
    {
        for (int i = 0; i < timesToPlay || timesToPlay == 0; i++) // Loop until timesToPlay is reached (if it's not set to 0)
        {
            // Randomly select a time between minTimeBetweenSounds and maxTimeBetweenSounds
            float randomDelay = Random.Range(minTimeBetweenSounds, maxTimeBetweenSounds);
            yield return new WaitForSeconds(randomDelay);

            if (audioSource != null && sounds.Length > 0)
            {
                audioSource.clip = sounds[Random.Range(0, sounds.Length)];
                audioSource.Play();
            }
        }
    }
}
