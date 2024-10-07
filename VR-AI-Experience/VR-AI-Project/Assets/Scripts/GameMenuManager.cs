
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;

public class GameMenuManager : MonoBehaviour
{
    public Transform head;
    public float spawnDistance = 2;
    public GameObject menu;
    public InputActionProperty showButton;

    private bool menuSpawned = false;

    void Start()
    {
       head = Camera.main.transform;
    }

    void Update()
    {
        if (showButton.action.WasPressedThisFrame())
        {
            menu.SetActive(!menu.activeSelf);

            // Calculate spawn position
            Vector3 spawnPosition = head.position + new Vector3(head.forward.x, 0, head.forward.z).normalized * spawnDistance;
            menu.transform.position = spawnPosition;

            // Look at the player's head position
            menu.transform.LookAt(new Vector3(head.position.x, menu.transform.position.y, head.position.z));

            // Reset menu rotation to upright
            menu.transform.rotation = Quaternion.Euler(0, head.rotation.eulerAngles.y, 0);
        }

    }

    public void MainMenu()
    {
        MapManager.instance.CallLoadMap("MainMenu");
    }

    void ToggleMenu()
    {
        menu.SetActive(menuSpawned);

        if (menuSpawned )
        {
            spawnMenu();
            menuSpawned = true;
        }
    }

    void spawnMenu()
    {

        if (menuSpawned == true)
        {
            menu.transform.forward *= 1;

        }
    }
}
