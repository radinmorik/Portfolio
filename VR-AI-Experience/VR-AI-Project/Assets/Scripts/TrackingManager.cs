using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.CompilerServices;
using TMPro;
using Unity.VisualScripting;
using UnityEngine;

public class TrackingManager : MonoBehaviour
{
    public Dictionary<string, int> trackedKeywords = new Dictionary<string, int>();

    public string outfit = "underwear";

    // Stats som øker for å avgjøre ending
    public int trackingCounter;
    public int messy;
    public int aggressive;
    public int eatTheChild;
    public int fly;
    public bool waterOn;

    [SerializeField] TextMeshProUGUI trackingMenu;

    GameManager gameManager;

    // Singleton instantiation
    public static TrackingManager singleton { get; private set; }
    private void Awake()
    {
        if (singleton != null && singleton != this)
        {
            Debug.Log("Destroyed tracking manager");
            Destroy(this.gameObject);
        }
        else
        {
            Debug.Log("Assigned new tracking manager");
            singleton = this;
            DontDestroyOnLoad(this);
        }
    }

    private void OnLevelWasLoaded(int level)
    {
        if (level == 0)
        {
            Destroy(gameObject);
        }
    }

    void Start()
    {
        gameManager = GameManager.singleton;
    }

    public string[] GetRelevantKeywords(int amount)
    {
        if (trackedKeywords.Count < amount)
        {
            amount = trackedKeywords.Count;
        }
        string[] relevantKeywords = new string[amount];

        //Sort keywords
        var sortedKeywords = trackedKeywords.OrderByDescending(kv => kv.Value);

        /*for (int i = 0; i < amount; i++)
        {
            relevantKeywords[i] = sortedKeywords.ToArray()[i].Key;
        }*/

        //Get random keywords
        for (int i = 0; i < amount; i++)
        {
            relevantKeywords[i] = sortedKeywords.ToArray()[UnityEngine.Random.Range(0, Mathf.RoundToInt(trackedKeywords.Count / 2f))].Key;
            //relevantKeywords[i] = trackedKeywords.Keys.ElementAt(UnityEngine.Random.Range(0, trackedKeywords.Count / 2));
        }

        return relevantKeywords;
    }

    public void SetOutfit(string newOutfit)
    {
        outfit = newOutfit;
    }

    public void AddKeyword(string keyword)
    {
        trackingCounter++;

        if (keyword.Contains("ate", StringComparison.OrdinalIgnoreCase))
        {
            eatTheChild++;
        }

        if (trackedKeywords.ContainsKey(keyword))
        {
            trackedKeywords[keyword] += gameManager.mapProgression;
        }
        else
        {
            trackedKeywords.Add(keyword, gameManager.mapProgression);
        }
        if (trackingMenu != null)
        {
            string newText = keyword + "\n" + trackingMenu.text;
            newText = newText.Substring(0, newText.LastIndexOf("\n"));
            trackingMenu.text = newText;
        }
    }
}
