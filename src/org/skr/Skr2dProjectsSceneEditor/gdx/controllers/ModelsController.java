package org.skr.Skr2dProjectsSceneEditor.gdx.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import org.skr.gdx.PhysWorld;
import org.skr.gdx.editor.Controller;
import org.skr.gdx.physmodel.BodyItem;
import org.skr.gdx.scene.PhysModelItem;
import org.skr.gdx.utils.RectangleExt;

/**
 * Created by rat on 31.08.14.
 */
public class ModelsController  extends Controller{


    Array<PhysModelItem> modelItems = new Array<PhysModelItem>();
    Array< Boolean > prevActiveStates = new Array<Boolean>();


    public ModelsController(Stage stage) {
        super(stage);
        setEnableBbControl( false );
    }

    public void clearSelection() {
        modelItems.clear();
        prevActiveStates.clear();
    }

    public boolean addModelItem( PhysModelItem  modelItem ) {

        int indexof = modelItems.indexOf( modelItem, true );

        if ( indexof >= 0 ) {
            modelItems.removeValue( modelItem, true );
            prevActiveStates.removeIndex( indexof );
            return false;
        }
        modelItems.add( modelItem );
        prevActiveStates.add( modelItem.isActive() );
        return true;
    }

    public Array<PhysModelItem> getModelItems() {
        return modelItems;
    }

    @Override
    protected void translateRendererToObject() {

    }

    @Override
    protected void draw() {
        getShapeRenderer().setColor(0, 1, 0, 1);
        getShapeRenderer().begin(ShapeRenderer.ShapeType.Line );

        for ( PhysModelItem mi : modelItems ) {

            for (BodyItem bi : mi.getModel().getBodyItems() ) {
                RectangleExt bb = bi.getBoundingBox();
                getShapeRenderer().rect(bb.getLeft(), bb.getBottom(),
                        bb.getWidth(), bb.getHeight());
            }
        }
        getShapeRenderer().end();

        drawControlPoints();

    }

    @Override
    protected Vector2 stageToObject(Vector2 stageCoord) {
        return stageCoord;
    }

    @Override
    protected void updateControlPointFromObject(ControlPoint cp) {

    }

    private boolean activeStatesChanged = false;

    @Override
    public boolean touchDown(Vector2 stageCoord) {
        boolean res = super.touchDown(stageCoord);
        if ( getSelectedControlPoint() == getPosControlPoint() ) {
            for ( int i = 0; i < modelItems.size; i++ ) {
                prevActiveStates.set(i, modelItems.get(i).isActive());
                modelItems.get(i).setActive( false );
            }

            activeStatesChanged = true;
        }

        return res;
    }

    @Override
    public boolean touchUp(Vector2 stageCoord, int button) {

        boolean res = super.touchUp(stageCoord, button);

        if ( activeStatesChanged  ) {

            for ( int i = 0; i < modelItems.size; i++ ) {
                modelItems.get(i).setActive( prevActiveStates.get(i) );
            }

            activeStatesChanged = false;
        }

        return res;
    }

    @Override
    protected void moveControlPoint(ControlPoint cp, Vector2 offsetLocal, Vector2 offsetStage) {

    }

    @Override
    protected void movePosControlPoint(ControlPoint cp, Vector2 offsetLocal, Vector2 offsetStage) {
        Vector2 offset = PhysWorld.get().viewToPhys( offsetStage );
        for ( PhysModelItem mi : modelItems ) {
            float x = mi.getBasePointPhysX();
            float y = mi.getBasePointPhysY();
            x += offset.x;
            y += offset.y;

            mi.translateBasePointTo_phys( x, y );
        }
    }

    @Override
    protected void rotateAtControlPoint(ControlPoint cp, float angle) {

    }

    @Override
    protected Object getControlledObject() {
        return null;
    }

    private static final Vector2 tVec1 = new Vector2();
    @Override
    protected void updatePosControlPointFromObject(ControlPoint cp) {

        tVec1.set( 0, 0 );

        for ( PhysModelItem mi :  modelItems ) {
            tVec1.add(mi.getBasePointViewX(), mi.getBasePointViewY());
        }
        tVec1.scl(1f / modelItems.size);

        cp.setPos( tVec1.x, tVec1.y );

    }

    public void translateModelsToViewX( float viewX ) {
        translateModelsToView( viewX, getPosControlPoint().getY() );
    }

    public void translateModelsToViewY( float viewY) {
        translateModelsToView( getPosControlPoint().getX(), viewY );
    }

    public void translateModelsToView( float viewX, float viewY) {

        float offsetViewX = viewX - getPosControlPoint().getX();
        float offsetViewY = viewY - getPosControlPoint().getY();

        for ( PhysModelItem mi : modelItems ) {
            float x = mi.getBasePointViewX();
            float y = mi.getBasePointViewY();
            x += offsetViewX;
            y += offsetViewY;

            mi.translateBasePointTo_view( x, y );
        }
    }

}
