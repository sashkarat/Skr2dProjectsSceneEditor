package org.skr.gdx.scene;

import com.badlogic.gdx.utils.Array;

/**
 * Created by rat on 07.08.14.
 */
public class LayerDescription {

    float x;
    float y;
    float rotation;
    String name;

    Layer.LayerSettings layerSettings = new Layer.LayerSettings();
    GroupChildrenDescriptions groupChildrenDescriptions = new GroupChildrenDescriptions();

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Layer.LayerSettings getLayerSettings() {
        return layerSettings;
    }

    public void setLayerSettings(Layer.LayerSettings layerSettings) {
        this.layerSettings = layerSettings;
    }

    public GroupChildrenDescriptions getGroupChildrenDescriptions() {
        return groupChildrenDescriptions;
    }

    public void setGroupChildrenDescriptions(GroupChildrenDescriptions groupChildrenDescriptions) {
        this.groupChildrenDescriptions = groupChildrenDescriptions;
    }
}
