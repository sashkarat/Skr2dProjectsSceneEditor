package org.skr.gdx.scene;

import org.skr.gdx.physmodel.animatedactorgroup.AagDescription;
import org.skr.gdx.physmodel.animatedactorgroup.AnimatedActorGroup;

/**
 * Created by rat on 06.08.14.
 */
public class TiledActorDescription {
    AagDescription aagBaseDesc;
    float aagPosX;
    float aagPosY;
    float spaceX;
    float spaceY;
    int numberX;
    int numberY;
    TiledActor.Type type;
    String name;

    public AagDescription getAagBaseDesc() {
        return aagBaseDesc;
    }

    public void setAagBaseDesc(AagDescription aagBaseDesc) {
        this.aagBaseDesc = aagBaseDesc;
    }

    public float getAagPosX() {
        return aagPosX;
    }

    public void setAagPosX(float aagPosX) {
        this.aagPosX = aagPosX;
    }

    public float getAagPosY() {
        return aagPosY;
    }

    public void setAagPosY(float aagPosY) {
        this.aagPosY = aagPosY;
    }

    public float getSpaceX() {
        return spaceX;
    }

    public void setSpaceX(float spaceX) {
        this.spaceX = spaceX;
    }

    public float getSpaceY() {
        return spaceY;
    }

    public void setSpaceY(float spaceY) {
        this.spaceY = spaceY;
    }

    public int getNumberX() {
        return numberX;
    }

    public void setNumberX(int numberX) {
        this.numberX = numberX;
    }

    public int getNumberY() {
        return numberY;
    }

    public void setNumberY(int numberY) {
        this.numberY = numberY;
    }

    public TiledActor.Type getType() {
        return type;
    }

    public void setType(TiledActor.Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
