using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AudioOnImpact : MonoBehaviour
{
    [SerializeField] AudioClip clip;
    [SerializeField] AudioSource source;
    // Start is called before the first frame update
    void Start()
    {
        source = GetComponentInChildren<AudioSource>();
    }
    private void OnCollisionEnter(Collision collision)
    {
        if(collision.impulse.magnitude > 0.3f)
        {
            if (clip != null)
            {
                source.PlayOneShot(clip);
            }
        }
    }
}
