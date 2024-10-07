using System.Collections;
using System.Collections.Generic;
using UnityEditor;
using UnityEngine;
using UnityEngine.UI;

public class ObjectivesManager : MonoBehaviour
{
    [SerializeField] Toggle[] objectives;

    public void CompleteObjective(int i)
    {
        objectives[i].isOn = true;
    }
}
