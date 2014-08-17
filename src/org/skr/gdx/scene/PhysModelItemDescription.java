package org.skr.gdx.scene;

/**
 * Created by rat on 03.08.14.
 */
public class PhysModelItemDescription {
    String modelUuid;
    String name = "";
    PhysModelItem.BasePoint basePoint = new PhysModelItem.BasePoint();

    public String getModelUuid() {
        return modelUuid;
    }

    public void setModelUuid(String modelUuid) {
        this.modelUuid = modelUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PhysModelItem.BasePoint getBasePoint() {
        return basePoint;
    }

    public void setBasePoint(PhysModelItem.BasePoint basePoint) {
        this.basePoint = basePoint;
    }
}
