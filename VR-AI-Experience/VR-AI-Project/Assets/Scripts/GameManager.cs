using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameManager : MonoBehaviour
{
    public int mapProgression = 1;
    public int endingIndex = 8;

    public static GameManager singleton { get; private set; }
    private void Awake()
    {
        if (singleton != null && singleton != this)
        {
            Destroy(this.gameObject);
        }
        else
        {
            singleton = this;
            DontDestroyOnLoad(this);
        }
    }
}
