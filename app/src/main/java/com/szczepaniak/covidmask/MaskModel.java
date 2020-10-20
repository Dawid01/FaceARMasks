package com.szczepaniak.covidmask;

import com.google.ar.sceneform.math.Vector3;

public class MaskModel {

    private int modelResource;
    private int textureResource;
    private Vector3 localPosition;
    private Vector3 localScale;


    public MaskModel() {
    }

    public MaskModel(int modelResource, int textureResource, Vector3 localPosition, Vector3 localScale) {
        this.modelResource = modelResource;
        this.textureResource = textureResource;
        this.localPosition = localPosition;
        this.localScale = localScale;
    }

    public int getModelResource() {
        return modelResource;
    }

    public void setModelResource(int modelResource) {
        this.modelResource = modelResource;
    }

    public int getTextureResource() {
        return textureResource;
    }

    public void setTextureResource(int textureResource) {
        this.textureResource = textureResource;
    }

    public Vector3 getLocalPosition() {
        return localPosition;
    }

    public void setLocalPosition(Vector3 localPosition) {
        this.localPosition = localPosition;
    }

    public Vector3 getLocalScale() {
        return localScale;
    }

    public void setLocalScale(Vector3 localScale) {
        this.localScale = localScale;
    }
}
