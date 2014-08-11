package org.skr.gdx.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

/**
 * Created by rat on 06.08.14.
 */

public class Layer extends Group implements Comparable {

    public static class ActionSettings {
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
    Array<TiledActor> actors = new Array<TiledActor>();
    ActionSettings actionSettings = new ActionSettings();
    float zOrder = 0;



    OrthographicCamera camera;

    public Layer(PhysScene scene) {
        this.scene = scene;

    }

    public ActionSettings getActionSettings() {
        return actionSettings;
    }

    public void setActionSettings(ActionSettings actionSettings) {
        this.actionSettings = actionSettings;
    }

    public float getzOrder() {
        return zOrder;
    }

    public void setzOrder(float zOrder) {
        this.zOrder = zOrder;
    }

    public void addTiledActor( TiledActor ta ) {
        actors.add( ta );
        addActor( ta );
    }

    public void removeTiledActor( TiledActor ta ) {
        removeActor( ta );
        actors.removeValue( ta, true );
    }

    public Array<TiledActor> getActors() {
        return actors;
    }

    public void loadFromDescription( LayerDescription desc ) {
        setName( desc.getName() );
        setPosition( desc.getX(), desc.getY() );
        setRotation( desc.getRotation() );
        setActionSettings( desc.getActionSettings() );
        setzOrder( desc.getzOrder() );
        for ( TiledActorDescription td : desc.getTiledActorDescriptions() ) {
            TiledActor ta = new TiledActor( scene );
            ta.loadFromDescription( td );
            addTiledActor( ta );
        }
    }

    public LayerDescription getDescription() {
        LayerDescription desc = new LayerDescription();

        desc.setName( getName() );
        desc.setX(getX());
        desc.setY(getY());
        desc.setRotation(getRotation());
        desc.setActionSettings(getActionSettings());
        desc.setzOrder( getzOrder() );

        Array<TiledActorDescription> tdList = new Array<TiledActorDescription>();
        for ( TiledActor ta : actors )
            tdList.add( ta.getDescription());
        desc.setTiledActorDescriptions( tdList );

        return desc;
    }

    @Override
    public int compareTo(Object o) {
        if ( o.equals( this ))
            return 0;
        Layer l = (Layer) o;

        if ( zOrder > l.zOrder )
            return 1;
        if ( zOrder < l.zOrder)
            return -1;

        return 0;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if ( camera == null ) {
            camera = (OrthographicCamera) scene.getStage().getCamera();
        }

        if ( actionSettings.followCameraX ) {
            setX( camera.position.x * actionSettings.offsetAttenuationX );
            if ( actionSettings.enableOffsetLimitX ) {
                if ( getX() < actionSettings.offsetLimitXMin ) {
                    setX( actionSettings.offsetLimitXMin);
                } else if ( getX() > actionSettings.offsetLimitXMax ) {
                    setX( actionSettings.offsetLimitXMax );
                }
            }
        }

        if ( actionSettings.followCameraY ) {
            setY( camera.position.y * actionSettings.offsetAttenuationY );
            if ( actionSettings.enableOffsetLimitY ) {
                if ( getY() < actionSettings.offsetLimitYMin ) {
                    setY( actionSettings.offsetLimitYMin);
                } else if ( getY() > actionSettings.offsetLimitYMax ) {
                    setY( actionSettings.offsetLimitYMax );
                }
            }
        }

    }
}
