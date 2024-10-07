using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Events;

public class Wearable : MonoBehaviour
{
    public UnityEvent equiped;

    public void TrackOutfit(string outfit)
    {
        TrackingManager.singleton.SetOutfit(outfit);
    }
}
