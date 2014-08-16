package org.skr.gdx.scene;

import com.badlogic.gdx.utils.Array;

/**
 * Created by rat on 02.08.14.
 */
public class PhysSceneDescription {
    String name = "";
    String textureAtlasPath = "";
    Array<String> modelFiles = new Array<String>();
    float viewCenterX = 0;
    float viewCenterY = 0;
    float viewLeft = -500;
    float viewRight = 500;
    float viewTop = 500;
    float viewBottom = -500;

    Array<LayerDescription> backLayerDescriptions = new Array<LayerDescription>();
    Array<LayerDescription> frontLayerDescriptions = new Array<LayerDescription>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTextureAtlasPath() {
        return textureAtlasPath;
    }

    public void setTextureAtlasPath(String textureAtlasPath) {
        this.textureAtlasPath = textureAtlasPath;
    }

    public Array<String> getModelFiles() {
        return modelFiles;
    }

    public void setModelFiles(Array<String> modelFiles) {
        this.modelFiles = modelFiles;
    }

    public float getViewLeft() {
        return viewLeft;
    }

    public void setViewLeft(float viewLeft) {
        this.viewLeft = viewLeft;
    }

    public float getViewRight() {
        return viewRight;
    }

    public void setViewRight(float viewRight) {
        this.viewRight = viewRight;
    }

    public float getViewTop() {
        return viewTop;
    }

    public void setViewTop(float viewTop) {
        this.viewTop = viewTop;
    }

    public float getViewBottom() {
        return viewBottom;
    }

    public void setViewBottom(float viewBottom) {
        this.viewBottom = viewBottom;
    }

    public float getViewCenterX() {
        return viewCenterX;
    }

    public void setViewCenterX(float viewCenterX) {
        this.viewCenterX = viewCenterX;
    }

    public float getViewCenterY() {
        return viewCenterY;
    }

    public void setViewCenterY(float viewCenterY) {
        this.viewCenterY = viewCenterY;
    }

    public Array<LayerDescription> getBackLayerDescriptions() {
        return backLayerDescriptions;
    }

    public void setBackLayerDescriptions(Array<LayerDescription> backLayerDescriptions) {
        this.backLayerDescriptions = backLayerDescriptions;
    }

    public Array<LayerDescription> getFrontLayerDescriptions() {
        return frontLayerDescriptions;
    }

    public void setFrontLayerDescriptions(Array<LayerDescription> frontLayerDescriptions) {
        this.frontLayerDescriptions = frontLayerDescriptions;
    }
}
