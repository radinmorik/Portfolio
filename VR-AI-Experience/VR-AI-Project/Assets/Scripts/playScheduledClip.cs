using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class playScheduledClip : MonoBehaviour
{
    public double soundDuration;
    public double goalTime;
    public AudioSource[] _audioSources;
    public int audioToggle;

    public AudioClip currentClip;


    private void Update()
    {
        if (AudioSettings.dspTime > goalTime - 1)
        {
            PlayScheduledClip();
        }
    }

    private void PlayScheduledClip()
    {
        _audioSources[audioToggle].clip = currentClip;
        _audioSources[audioToggle].PlayScheduled(goalTime);

        soundDuration = (double)currentClip.samples / currentClip.frequency;
        goalTime = goalTime + soundDuration;

        audioToggle = 1 - audioToggle;

    }
}