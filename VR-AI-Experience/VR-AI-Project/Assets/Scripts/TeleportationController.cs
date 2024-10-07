using System.Collections;
using System.Collections.Generic;
using UnityEngine;
//Allows us to use the Input System to get values for the thumbstick
using UnityEngine.InputSystem;
//Allows us to use the Interaction Toolkit to enable and disable our rays
using UnityEngine.XR.Interaction.Toolkit;



public class TeleportationController : MonoBehaviour
{
    static private bool _teleportIsActive = false;

    public enum ControllerType
    {
        RightHand,
        LeftHand
    }

    public ControllerType targetController;

    public InputActionAsset inputAction;

    public XRRayInteractor rayInteractor;

    public TeleportationProvider teleportationProvider;

    private InputAction _triggerAction;

    void Start()
    {
        rayInteractor.enabled = false;

        _triggerAction = inputAction.FindActionMap("XRI " + targetController.ToString()).FindAction("ActivateTeleport");
        _triggerAction.Enable();
        _triggerAction.performed += OnTeleportActivate;
    }

    private void OnDestroy()
    {
        _triggerAction.performed -= OnTeleportActivate;
    }

    void Update()
    {
        if (!_teleportIsActive || !rayInteractor.enabled)
        {
            return;
        }

        if (_triggerAction.triggered)
        {
            if (!rayInteractor.TryGetCurrent3DRaycastHit(out RaycastHit raycastHit))
            {
                rayInteractor.enabled = false;
                _teleportIsActive = false;
                return;
            }

            TeleportRequest teleportRequest = new TeleportRequest()
            {
                destinationPosition = raycastHit.point,
            };

            teleportationProvider.QueueTeleportRequest(teleportRequest);

            rayInteractor.enabled = false;
            _teleportIsActive = false;
        }
    }

    private void OnTeleportActivate(InputAction.CallbackContext context)
    {
        if (!_teleportIsActive)
        {
            rayInteractor.enabled = true;
            _teleportIsActive = true;
        }
    }
}