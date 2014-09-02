package org.skr.Skr2dProjectsSceneEditor.gdx.controllers;

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

    public ModelsController(Stage stage) {
        super(stage);
        setEnableBbControl( false );
    }


    public void clearSelection() {
        modelItems.clear();
    }

    public boolean addModelItem( PhysModelItem  modelItem ) {
        if ( modelItems.contains( modelItem, true ) ) {
            modelItems.removeValue( modelItem, true );
            return false;
        }
        modelItems.add( modelItem );
        return true;
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
}
