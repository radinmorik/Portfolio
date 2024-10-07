using System;
using UnityEngine;

[Serializable]
public class MeshData
{
    public string meshName;
    public Mesh mesh;
    public Texture2D texture;
    public float targetHeight;
    public bool kinematic;
    public bool pickupable;
    public bool edible;
}
