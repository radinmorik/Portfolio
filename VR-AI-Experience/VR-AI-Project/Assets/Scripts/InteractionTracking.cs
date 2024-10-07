using System.Collections;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;

public class InteractionTracking : MonoBehaviour
{
    TrackingManager trackingManager;

    public string[] keywords;

    void Start()
    {
        trackingManager = TrackingManager.singleton;
    }

    public void TriggerTracking(string trackingMethod="Interacted with") //optional trackingMethod keyword
    {
        foreach (string keyword in keywords)
        {
            print(trackingMethod + " " + keyword);
            trackingManager.AddKeyword(trackingMethod + " " + keyword);
        }
    }
}
