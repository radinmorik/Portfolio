using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Events;

public class Edible : MonoBehaviour
{
    public UnityEvent eaten;

    private void Start()
    {
        if (eaten == null)
        {
            if (GetComponent<InteractionTracking>())
            {
                eaten = new UnityEvent();
                eaten.AddListener(() => GetComponent<InteractionTracking>().TriggerTracking("Ate"));
            }
        }
    }
}
