using System.Collections;
using UnityEngine;
using UnityEngine.Rendering;
using UnityEngine.Rendering.Universal;
using UnityEngine.SceneManagement;

public class MapManager : MonoBehaviour
{
    [SerializeField] private Transform sun;
    [SerializeField] private Volume postProcessVolume;
    private ColorAdjustments colorAdjustments;

    public static MapManager instance;
    private void Awake()
    {
        instance = this;
    }

    void Start()
    {
        // Find scene objects
        sun = GameObject.FindWithTag("Sun").transform;
        postProcessVolume = FindAnyObjectByType<Volume>();

        // Get post process stuff
        postProcessVolume.profile.TryGet(out colorAdjustments);
    }

    public void QuitGame()
    {
        Application.Quit();
    }

    public void CallLoadMap(string mapName)
    {
        StartCoroutine(LoadMap(mapName));
    }
    IEnumerator LoadMap(string mapName)
    {
        yield return StartCoroutine(FadeToBlack());
        yield return null;

        if (SceneManager.GetActiveScene().name == "MainMenu")
        {
            if (GenerateManager.singleton != null)
            {
                Destroy(GenerateManager.singleton);
            }
            if (GameManager.singleton != null)
            {
                Destroy(GameManager.singleton);
            }
        }

        // Check if ending should trigger
        if (GameManager.singleton.mapProgression >= GameManager.singleton.endingIndex)
        {
            if (TrackingManager.singleton.eatTheChild > GameManager.singleton.endingIndex * 2)
            {
                Debug.Log("trigger eat the child ending");
                mapName = "MapEnding_Eat";
            }
            else if (TrackingManager.singleton.trackingCounter > GameManager.singleton.endingIndex * 10)
            {
                Debug.Log("trigger dream ending");
                mapName = "MapEnding_Dream";
            }
            else
            {
                Debug.Log("trigger infinite bridge ending");
                mapName = "MapEnding_InfiniteBridgeDEV";
            }
        }

        SceneManager.LoadScene(mapName);
    }

    public void IncreaseMapProgression()
    {
        GameManager.singleton.mapProgression++;
    }

    public void CallQuickFade()
    {
        StartCoroutine(QuickFade());
    }
    IEnumerator QuickFade()
    {
        yield return StartCoroutine(FadeToBlack());
        yield return StartCoroutine(FadeFromBlack());
    }

    public void CallSleep()
    {
        StartCoroutine(Sleep());
    }
    IEnumerator Sleep()
    {
        // disable interactions to prevent spam

        yield return StartCoroutine(FadeToBlack());

        yield return new WaitForSeconds(1);
        sun.localEulerAngles -= new Vector3(0, 25, 0);

        yield return StartCoroutine(FadeFromBlack());

        // enable interactions

        // if slept 4 times, load level to next day
    }

    IEnumerator FadeToBlack()
    {
        yield return null;
        colorAdjustments.postExposure.value += -0.1f;
        if (colorAdjustments.postExposure.value >= -10)
        {
            yield return StartCoroutine(FadeToBlack());
        }
    }
    IEnumerator FadeFromBlack()
    {
        yield return null;
        colorAdjustments.postExposure.value += 0.1f;
        if (colorAdjustments.postExposure.value <= 0)
        {
            yield return StartCoroutine(FadeFromBlack());
        }
    }

    public void PauseGame()
    {

    }
    public void UnPauseGame()
    {

    }
}
