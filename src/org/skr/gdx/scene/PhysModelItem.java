package org.skr.gdx.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import org.skr.gdx.PhysWorld;
import org.skr.gdx.physmodel.BodyItem;
import org.skr.gdx.physmodel.PhysModel;

/**
 * Created by rat on 03.08.14.
 */
public class PhysModelItem extends Group {

    public static class BasePoint {
        float physX = 0;
        float physY = 0;
        float angleRad = 0;

        public BasePoint() {
        }

        public BasePoint( BasePoint cpy) {
            this.physX = cpy.physX;
            this.physY = cpy.physY;
            this.angleRad = cpy.angleRad;
        }
    }

    int id = -1;
    PhysModel model = null;
    PhysScene scene = null;
    PhysModelDescriptionHandler descriptionHandler;
    BasePoint basePoint = new BasePoint();
    boolean liveBasePoint = false;

    public PhysModelItem(  PhysScene scene, boolean newlyCreated ) {
        this.scene = scene;
        setName("");
        if ( newlyCreated )
            id = scene.genModelItemId();
    }


    public void clear() {

        if ( scene == null )
            return;
        if ( model == null )
            return;

        for (BodyItem bi : model.getBodyItems() ) {
            PhysWorld.getPrimaryWorld().destroyBody( bi.getBody() );
            removeActor( bi );
        }

        model = null;
    }

    public boolean isActive() {
        if ( model == null )
            return false;
        if ( model.getBodyItems().size == 0 )
            return false;
        return model.getBodyItems().get(0).getBody().isActive();
    }

    public void setActive( boolean active ) {
        if ( model.getBodyItems().size == 0 )
            return;
        for( BodyItem bi : model.getBodyItems() )
            bi.getBody().setActive( active );
    }


    public boolean isLiveBasePoint() {
        return liveBasePoint;
    }

    public void setLiveBasePoint(boolean liveBasePoint) {
        this.liveBasePoint = liveBasePoint;
    }

    public PhysModelDescriptionHandler getDescriptionHandler() {
        return descriptionHandler;
    }

    public void setDescriptionHandler(PhysModelDescriptionHandler descriptionHandler) {
        this.descriptionHandler = descriptionHandler;
    }

    public BasePoint getBasePoint() {
        return basePoint;
    }

    public void setBasePoint(BasePoint basePoint) {
        this.basePoint.physX = basePoint.physX;
        this.basePoint.physY = basePoint.physY;
        this.basePoint.angleRad = basePoint.angleRad;
    }

    public PhysModel getModel() {
        return model;
    }

    public void load( PhysModelDescriptionHandler physModelDescriptionHandler ) {

        this.descriptionHandler = physModelDescriptionHandler;

        load();
    }

    public void load() {

        clear();

        model = new PhysModel( scene.getWorld(), scene.getAtlas()  );
        model.uploadFromDescription( descriptionHandler.getModelDesc() );
        model.setModelItem( this );
        for ( BodyItem bi : model.getBodyItems() ) {
            addActor( bi );
        }
        if ( getName().isEmpty() ) {
            setName( "item" + model.getName() );
        }

        translateToBasePoint();

    }

    Matrix3 tmpMtx = new Matrix3();

    private void translateToBasePoint() {
        tmpMtx.setToTranslation( basePoint.physX, basePoint.physY );

        for ( BodyItem bi : model.getBodyItems() ) {
            Vector2 v = bi.getBody().getPosition();
            v.mul( tmpMtx );
            bi.getBody().setTransform( v, bi.getBody().getAngle() );
        }
    }

    public void translateBasePointTo_phys( float x, float y ) {
        float offsetX =  x - basePoint.physX;
        float offsetY = y - basePoint.physY;

        tmpMtx.setToTranslation( offsetX, offsetY );


        for ( BodyItem bi : model.getBodyItems() ) {
            Vector2 v = bi.getBody().getPosition();
            v.mul( tmpMtx );
            bi.getBody().setTransform( v, bi.getBody().getAngle() );
        }

        basePoint.physX += offsetX;
        basePoint.physY += offsetY;

    }


    public void translateBasePointTo_view( float x, float y ) {
        translateBasePointTo_phys(PhysWorld.get().toPhys(x),
                PhysWorld.get().toPhys(y));
    }

    public void translateBasePointXTo_view( float x ) {
        translateBasePointXTo_phys( PhysWorld.get().toPhys( x ) );
    }

    public void translateBasePointXTo_phys( float x) {
        translateBasePointTo_phys( x, basePoint.physY );
    }

    public void translateBasePointYTo_view( float y ) {
        translateBasePointYTo_phys( PhysWorld.get().toPhys( y ) );
    }

    public void translateBasePointYTo_phys( float y ) {
        translateBasePointTo_phys( basePoint.physX, y );
    }

    public float getBasePointViewX() {
        return PhysWorld.get().toView( basePoint.physX );
    }

    public float getBasePointViewY() {
        return PhysWorld.get().toView( basePoint.physY );
    }

    public float getBasePointPhysX() {
        return basePoint.physX;
    }

    public float getBasePointPhysY() {
        return basePoint.physY;
    }

    public int getId() {
        return id;
    }

    public void loadFromDescription( PhysModelItemDescription desc ) {
        String uuidString = desc.getModelUuid();

        PhysModelDescriptionHandler mdh = scene.findModelDescriptionHandler( uuidString );
        if ( mdh == null ) {
            Gdx.app.error("PhysModelItem.loadFromDescription", " Model Description Handler not found");
            return;
        }
        setName(desc.getName());
        this.id = desc.getId();
        if ( id < 0 )
            id = scene.genModelItemId();
        setBasePoint( desc.getBasePoint() );
        load(mdh);
    }

    public PhysModelItemDescription getDescription() {
        PhysModelItemDescription desc = new PhysModelItemDescription();
        desc.setModelUuid( model.getUuid().toString() );
        desc.setName( getName() );
        desc.setBasePoint( basePoint );
        return desc;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if ( liveBasePoint ) {
            basePoint.physX = 0;
            basePoint.physY = 0;
            for ( BodyItem bi : model.getBodyItems() ) {
                Vector2 c = bi.getBody().getWorldCenter();
                basePoint.physX += c.x;
                basePoint.physY += c.y;
            }
            basePoint.physX /= model.getBodyItems().size;
            basePoint.physY /= model.getBodyItems().size;
        }
    }
}
