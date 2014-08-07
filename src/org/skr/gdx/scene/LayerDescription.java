package org.skr.gdx.scene;

import com.badlogic.gdx.utils.Array;

/**
 * Created by rat on 07.08.14.
 */
public class LayerDescription {
    Array<TiledActorDescription> tiledActorDescriptions;

    float x;
    float y;
    float rotation;
    String name;

    boolean backdrop = true;



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
}
