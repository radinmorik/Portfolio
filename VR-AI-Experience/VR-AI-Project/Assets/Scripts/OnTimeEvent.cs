using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Events;

public class OnTimeEvent : MonoBehaviour
{
    [SerializeField] int time;
    float timer;

    [SerializeField] UnityEvent timeEvent;

    private void Update()
    {
        timer += Time.deltaTime;

        if (timer > time)
        {
            timer = 0;
            timeEvent.Invoke();
        }
    }
}
