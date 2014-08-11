package org.skr.gdx.scene;

import com.badlogic.gdx.utils.Array;

/**
 * Created by rat on 07.08.14.
 */
public class LayerDescription {
    Array<TiledActorDescription> tiledActorDescriptions = new Array<TiledActorDescription>();

    float x;
    float y;
    float rotation;
    String name;

    Layer.ActionSettings actionSettings = new Layer.ActionSettings();
    float zOrder;




    public Array<TiledActorDescription> getTiledActorDescriptions() {
        return tiledActorDescriptions;
    }

    public void setTiledActorDescriptions(Array<TiledActorDescription> tiledActorDescriptions) {
        this.tiledActorDescriptions = tiledActorDescriptions;
    }

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

    public Layer.ActionSettings getActionSettings() {
        return actionSettings;
    }

    public void setActionSettings(Layer.ActionSettings actionSettings) {
        this.actionSettings = actionSettings;
    }

    public float getzOrder() {
        return zOrder;
    }

    public void setzOrder(float zOrder) {
        this.zOrder = zOrder;
    }
}
