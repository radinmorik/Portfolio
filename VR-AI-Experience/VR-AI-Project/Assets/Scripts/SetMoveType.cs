using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.XR.Interaction.Toolkit;

public class SetMoveType : MonoBehaviour
{
    public ActionBasedContinuousMoveProvider continuousMoveProvider;
    public TeleportationProvider teleportationProvider;
    public GameObject teleportationRay;

    private void Awake()
    {
        continuousMoveProvider = FindObjectOfType<ActionBasedContinuousMoveProvider>();
        teleportationProvider = FindObjectOfType<TeleportationProvider>();
        teleportationRay = GameObject.FindWithTag("TeleportationRay");
        teleportationRay.SetActive(false);
    }

    public void setTypeFromToggle(bool value)
    {
        if (value == true)
        {
            teleportationProvider.enabled = true;
            continuousMoveProvider.enabled = false;
            teleportationRay.SetActive(true);
        }

        else if (value == false)
        {
            teleportationProvider.enabled = false;
            continuousMoveProvider.enabled = true;
            teleportationRay.SetActive(false);
        }

    }

}
