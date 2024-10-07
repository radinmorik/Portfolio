using System.Collections;
using System.Collections.Generic;
using System.Linq;
using TMPro;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.XR.Interaction.Toolkit;

public class DistanceGrabInteractor : XRBaseControllerInteractor
{
    #region member variables

    List<XRBaseInteractable> validTargets = new List<XRBaseInteractable>();
    XRBaseInteractable currentNearestObject;

    //settings
    private float grabbingTreshold = .98f;
    public GameObject cursor;
    public Transform fwdVector;

    private List<XRBaseInteractable> grabbableItems;
    private SphereCollider coll;
    
    #endregion


    // Start is called before the first frame update
    void Start()
    {
        //create a collider and make it a trigger
        if (!coll)
        {
            coll = gameObject.AddComponent<SphereCollider>();
        }
        coll.radius = .1f;
        coll.isTrigger = true;

        grabbableItems = new List<XRBaseInteractable>();
        //cursor
        cursor = Instantiate(cursor);
        cursor.SetActive(false);

        //cache list of grabbables
        //make a list of objects of type XRBaseInteractable
        XRBaseInteractable[] interactableArray = FindObjectsOfType<XRBaseInteractable>();
        string layer;
        Debug.Log("test: " + interactableArray.Length);
        foreach (XRBaseInteractable interactable in interactableArray)
        {
            InteractionLayerMask mask = interactable.interactionLayers;
            layer = mask.value.ToBinaryString().Reverse().ToArray().ArrayToString().Substring(6, 1);
            Debug.Log(layer);
            if (layer == "1")
            {
                grabbableItems.Add(interactable);
            }
        }
        // Print the count of interactables in the list
        Debug.Log("Total XRBaseInteractable objects in the scene: " + grabbableItems.Count);

    }

    protected  List<XRBaseInteractable> ValidTargets { get { return validTargets; } }

    public override void ProcessInteractor(XRInteractionUpdateOrder.UpdatePhase updatePhase)
    {
        base.ProcessInteractor(updatePhase);

        //GetValidTargets(validTargets);
    }

    public override void GetValidTargets(List<IXRInteractable> validTargets)
    {
        
        validTargets.Clear();

        //find best grabbable by using min algorithm and dot product
        float bestGuess = 0;
        XRBaseInteractable selectable = null;
        foreach (XRBaseInteractable obj in grabbableItems)
        {
            //if obj is further away from fwdVector than x
            if (Vector3.Distance(obj.transform.position, fwdVector.transform.position) < 2)
            {
                Vector3 dir = (obj.transform.position - fwdVector.position).normalized;
                float currentGuess = Vector3.Dot(fwdVector.forward, dir);

                if (currentGuess > grabbingTreshold && currentGuess > bestGuess)
                {
                    bestGuess = currentGuess;
                    selectable = obj;
                    currentNearestObject = selectable;
                    coll.center = transform.InverseTransformPoint(selectable.transform.position);

                    validTargets.Add(selectable);
                }
            }
        }

        if (selectable)
        {
            cursor.SetActive(true);
            cursor.transform.position = selectable.transform.position;
        }
        else
        {
            coll.center = Vector3.zero;
            cursor.SetActive(false);
        } 
        


        //base.GetValidTargets(validTargets);
    }

    public override bool CanSelect(IXRSelectInteractable interactable)
    {
        bool selectActivated = currentNearestObject == interactable || base.CanSelect(interactable);
        return selectActivated && (selectTarget == null || selectTarget == interactable);
    }
}