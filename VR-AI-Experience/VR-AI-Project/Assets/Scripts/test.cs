using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;
using Unity.VisualScripting;
using System.Linq;

public class test : MonoBehaviour
{
    public TMP_Text text;
    public TrackingManager V = TrackingManager.singleton;
    public Keys keys = new Keys();
    public string cKey;
    public int cVal;
    public string tekst;

    // Start is called before the first frame update
    void Start()
    {
        tekst = "";
    }

    // Update is called once per frame
    void Update()
    {
        if (tekst != null) 
        {
            tekst = getMax(V.trackedKeywords);

        }
        if (text.text != tekst)
        {

            Print(tekst);
        }
    }

    void Print(string dir)
    {
        text.text = dir;
    }
    public string getMax(Dictionary<string, int> dict)
    {
        int i = 0;
        string key = "";
        foreach (KeyValuePair<string, int> entry in dict)
        {
            if (entry.Value>i)
            {
                key = entry.Key;
                i = entry.Value;
            }
        }
        return key + ": " + i;
    }

    public class Keys
    {
        public string Key; 
        public string Value;
    }
}
