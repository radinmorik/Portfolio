using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.XR.Interaction.Toolkit;

public class SetTurnType : MonoBehaviour
{

    public ActionBasedSnapTurnProvider snapTurn;
    public ActionBasedContinuousTurnProvider continuousTurn;

    private void Awake()
    {
        snapTurn = FindObjectOfType<ActionBasedSnapTurnProvider>();
        continuousTurn = FindObjectOfType<ActionBasedContinuousTurnProvider>();
    }

    public void SetTypeFromToggle(bool value)
    {
        if ( value == true )
        {
            snapTurn.enabled = true;
            continuousTurn.enabled = false;
        }
        else if ( value == false )
        {
            snapTurn.enabled = false;
            continuousTurn.enabled = true;
        }

    }

}
