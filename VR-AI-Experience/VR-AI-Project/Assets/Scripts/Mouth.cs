using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Mouth : MonoBehaviour
{
    GameObject currentOutfit;

    private void OnTriggerEnter(Collider other)
    {
        if (other.GetComponent<Edible>())
        {
            other.GetComponent<Edible>().eaten.Invoke();
            Destroy(other.gameObject);
        }
        else if (other.GetComponent<Wearable>())
        {
            other.GetComponent<Wearable>().equiped.Invoke();

            if (currentOutfit != null)
            {
                currentOutfit.SetActive(true);
            }

            currentOutfit = other.gameObject;

            currentOutfit.SetActive(false);
        }
    }
}
