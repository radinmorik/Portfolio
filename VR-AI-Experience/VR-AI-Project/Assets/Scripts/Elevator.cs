using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Elevator : MonoBehaviour
{
    [SerializeField] Transform doorLeft, doorRight;

    bool closed;

    MapManager gameManager;

    private void Start()
    {
        gameManager = MapManager.instance;

        StartCoroutine(DelayedOpenDoors());
    }

    public void CallGoToFloor(string mapName)
    {
        StartCoroutine(GoToFloor(mapName));
    }

    IEnumerator GoToFloor(string mapName)
    {
        yield return StartCoroutine(CloseDoors());
        gameManager.CallLoadMap(mapName);
    }

    public void CallCloseDoors()
    {
        if (closed == false)
        {
            closed = true;
            StartCoroutine(CloseDoors());
        }
        else
        {
            closed = false;
            StartCoroutine(OpenDoors());
        }
    }

    IEnumerator CloseDoors()
    {
        yield return new WaitForSeconds(0.01f);

        doorLeft.localPosition -= new Vector3(0.005f, 0, 0);
        doorRight.localPosition += new Vector3(0.005f, 0, 0);
        if (doorLeft.localPosition.x >= 0.5f)
        {
            yield return StartCoroutine(CloseDoors());
        }
    }

    IEnumerator DelayedOpenDoors()
    {
        yield return new WaitForSeconds(2);
        StartCoroutine(OpenDoors());
    }

    IEnumerator OpenDoors()
    {
        yield return new WaitForSeconds(0.01f);

        doorLeft.localPosition += new Vector3(0.005f, 0, 0);
        doorRight.localPosition -= new Vector3(0.005f, 0, 0);
        if (doorLeft.localPosition.x <= 1.4f)
        {
            yield return StartCoroutine(OpenDoors());
        }
    }
}
