package org.skr.gdx.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

/**
 * Created by rat on 06.08.14.
 */

public class Layer extends Group {

    public static class LayerSettings {
        boolean followCameraX = false;
        boolean followCameraY = false;
        float offsetAttenuationX = 1;
        float offsetAttenuationY = 1;
        boolean enableOffsetLimitX = false;
        boolean enableOffsetLimitY = false;
        float offsetLimitXMin = 0;
        float offsetLimitXMax = 0;
        float offsetLimitYMin = 0;
        float offsetLimitYMax = 0;

        public void set( LayerSettings l ) {
            this.followCameraX = l.followCameraX;
            this.followCameraY = l.followCameraY;
            this.offsetAttenuationX = l.offsetAttenuationX;
            this.offsetAttenuationY = l.offsetAttenuationY;
            this.enableOffsetLimitX = l.enableOffsetLimitX;
            this.enableOffsetLimitY = l.enableOffsetLimitY;
            this.offsetLimitXMin = l.offsetLimitXMin;
            this.offsetLimitXMax = l.offsetLimitXMax;
            this.offsetLimitYMin = l.offsetLimitYMin;
            this.offsetLimitYMax = l.offsetLimitYMax;
        }

        public boolean isFollowCameraX() {
            return followCameraX;
        }

        public void setFollowCameraX(boolean followCameraX) {
            this.followCameraX = followCameraX;
        }

        public boolean isFollowCameraY() {
            return followCameraY;
        }

        public void setFollowCameraY(boolean followCameraY) {
            this.followCameraY = followCameraY;
        }

        public float getOffsetAttenuationX() {
            return offsetAttenuationX;
        }

        public void setOffsetAttenuationX(float offsetAttenuationX) {
            this.offsetAttenuationX = offsetAttenuationX;
        }

        public float getOffsetAttenuationY() {
            return offsetAttenuationY;
        }

        public void setOffsetAttenuationY(float offsetAttenuationY) {
            this.offsetAttenuationY = offsetAttenuationY;
        }

        public boolean isEnableOffsetLimitX() {
            return enableOffsetLimitX;
        }

        public void setEnableOffsetLimitX(boolean enableOffsetLimitX) {
            this.enableOffsetLimitX = enableOffsetLimitX;
        }

        public boolean isEnableOffsetLimitY() {
            return enableOffsetLimitY;
        }

        public void setEnableOffsetLimitY(boolean enableOffsetLimitY) {
            this.enableOffsetLimitY = enableOffsetLimitY;
        }

        public float getOffsetLimitXMin() {
            return offsetLimitXMin;
        }

        public void setOffsetLimitXMin(float offsetLimitXMin) {
            this.offsetLimitXMin = offsetLimitXMin;
        }

        public float getOffsetLimitXMax() {
            return offsetLimitXMax;
        }

        public void setOffsetLimitXMax(float offsetLimitXMax) {
            this.offsetLimitXMax = offsetLimitXMax;
        }

        public float getOffsetLimitYMin() {
            return offsetLimitYMin;
        }

        public void setOffsetLimitYMin(float offsetLimitYMin) {
            this.offsetLimitYMin = offsetLimitYMin;
        }

        public float getOffsetLimitYMax() {
            return offsetLimitYMax;
        }

        public void setOffsetLimitYMax(float offsetLimitYMax) {
            this.offsetLimitYMax = offsetLimitYMax;
        }
    }


    PhysScene scene;
    LayerSettings layerSettings = new LayerSettings();

    OrthographicCamera camera;

    public Layer(PhysScene scene) {
        this.scene = scene;
    }

    public LayerSettings getLayerSettings() {
        return layerSettings;
    }

    public void setLayerSettings(LayerSettings layerSettings) {
        this.layerSettings = layerSettings;
    }

    public void addTiledActor( TiledActor ta ) {
        addActor( ta );
    }

    public void removeTiledActor( TiledActor ta ) {
        removeActor(ta);
    }

    public void loadFromDescription( LayerDescription desc ) {
        setName(desc.getName());
        setPosition(desc.getX(), desc.getY());
        setRotation(desc.getRotation());
        setLayerSettings(desc.getLayerSettings());

        GroupChildrenDescriptions gcd =  desc.getGroupChildrenDescriptions();

        gcd.beginLoading( scene );

        while ( gcd.hasNextActor() ) {
            Actor a = gcd.getNextActor();
            if ( a == null )
                break;
            addActor( a );
        }

        gcd.endLoading();
    }

    public LayerDescription getDescription() {
        LayerDescription desc = new LayerDescription();

        desc.setName(getName());
        desc.setX(getX());
        desc.setY(getY());
        desc.setRotation(getRotation());
        desc.setLayerSettings(getLayerSettings());

        for ( Actor a : getChildren() ) {
            desc.getGroupChildrenDescriptions().addChild( a );
        }

        return desc;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if ( camera == null ) {
            camera = (OrthographicCamera) scene.getStage().getCamera();
        }

        if ( layerSettings.followCameraX ) {
            setX( camera.position.x * layerSettings.offsetAttenuationX );
            if ( layerSettings.enableOffsetLimitX ) {
                if ( getX() < layerSettings.offsetLimitXMin ) {
                    setX( layerSettings.offsetLimitXMin);
                } else if ( getX() > layerSettings.offsetLimitXMax ) {
                    setX( layerSettings.offsetLimitXMax );
                }
            }
        }

        if ( layerSettings.followCameraY ) {
            setY( camera.position.y * layerSettings.offsetAttenuationY );
            if ( layerSettings.enableOffsetLimitY ) {
                if ( getY() < layerSettings.offsetLimitYMin ) {
                    setY( layerSettings.offsetLimitYMin);
                } else if ( getY() > layerSettings.offsetLimitYMax ) {
                    setY( layerSettings.offsetLimitYMax );
                }
            }
        }

    }
}
