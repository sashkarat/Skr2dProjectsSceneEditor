package org.skr.Skr2dProjectsSceneEditor.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import org.skr.Skr2dProjectsSceneEditor.gdx.controllers.AagController;
import org.skr.Skr2dProjectsSceneEditor.gdx.controllers.ModelsController;
import org.skr.gdx.PhysWorld;
import org.skr.gdx.editor.BaseScreen;
import org.skr.gdx.editor.Controller;
import org.skr.gdx.physmodel.BodyItem;
import org.skr.gdx.physmodel.PhysModel;
import org.skr.gdx.physmodel.animatedactorgroup.AnimatedActorGroup;
import org.skr.gdx.scene.Layer;
import org.skr.gdx.scene.PhysModelItem;
import org.skr.gdx.scene.PhysScene;
import org.skr.gdx.scene.TiledActor;
import org.skr.gdx.utils.ModShapeRenderer;
import org.skr.gdx.utils.RectangleExt;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * Created by rat on 02.08.14.
 */
public class EditorScreen extends BaseScreen {

    public enum ControllableObjectType {
        NONE,
        AAG,
        TILED_ACTOR,
        LAYER,
        MODEL_ITEM,
        SELECTION_ARRAY
    }

    public static enum SelectionMode {
        DISABLED,
        MODEL_ITEM
    }

    public static enum DebugResolution {
        R_NONE,
        R_800x600,
        R_960x540,
        R_1280x768,
        R_1920x1080,
        R_2560x1600
    }

    public interface SelectionListener {
        void itemsSelected( Object object, boolean ctrl );
    }

    public static class SceneHolder extends Group {

    }

    SelectionMode selectionMode = SelectionMode.DISABLED;

    Layer currentLayer = null;

    DebugResolution debugViewRectResolution = DebugResolution.R_NONE;

    PhysScene scene;

    SceneHolder sceneHolder;

    SelectionListener selectionListener = null;

    Controller currentController = null;
    AagController aagController = null;
    ModelsController modelsController = null;


    public EditorScreen() {
        super();
        sceneHolder = new SceneHolder();
        getStage().addActor( sceneHolder );
        aagController = new AagController( getStage() );
        modelsController = new ModelsController( getStage() );

    }

    public SelectionListener getSelectionListener() {
        return selectionListener;
    }

    public void setSelectionListener(SelectionListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
    }

    public PhysScene getScene() {
        return scene;
    }

    public void setScene(PhysScene scene) {

        if ( this.scene != null )
            sceneHolder.removeActor( this.scene );
        this.scene = scene;
        this.scene.initializeScene( getStage() );

        this.scene.getCameraController().setHoldCameraInsideBorders( false );

        sceneHolder.addActor( scene );
    }

    public AagController getAagController() {
        return aagController;
    }

    public void setAagController(AagController aagController) {
        this.aagController = aagController;
    }

    public ModelsController getModelsController() {
        return modelsController;
    }

    public void setModelsController(ModelsController modelsController) {
        this.modelsController = modelsController;
    }

    public void setControllableObject( Object object, ControllableObjectType type ) {

        if ( aagController.getAag() != null ) {
            aagController.getAag().setUserObject( null );
        }

        if ( object == null ) {
            currentController = null;
            currentLayer = null;
            return;
        }

        switch ( type ) {

            case NONE:
                currentController = null;
                currentLayer = null;
                break;
            case AAG:
                aagController.setAag((AnimatedActorGroup) object);
                currentController = aagController;
                currentLayer = findParentLayer((Actor) object);
                break;
            case TILED_ACTOR:
                currentController = null;
                currentLayer = findParentLayer((Actor) object);
                break;
            case LAYER:
                currentLayer = (Layer) object;
                break;
            case MODEL_ITEM:
                modelsController.addModelItem((PhysModelItem) object);
                currentController = modelsController;
                currentLayer = null;
                break;
            case SELECTION_ARRAY:
                Array<PhysModelItem> modelItems = (Array<PhysModelItem>) object;
                for ( PhysModelItem mi : modelItems )
                    modelsController.addModelItem( mi );
                currentLayer = null;
                currentController = modelsController;
                break;
        }



        if ( currentLayer != null ) {
            Gdx.app.log("EditorScreen.setControllableObject", "Layer: " + currentLayer.getName() );
        }
    }

    Layer findParentLayer( Actor actor ) {
        Group gp = actor.getParent();
        while ( gp != null ) {
            if ( !(gp instanceof Layer) ) {
                gp = gp.getParent();
            } else {
                return (Layer) gp;
            }
        }
        return null;
    }

    public DebugResolution getDebugViewRectResolution() {
        return debugViewRectResolution;
    }

    public void setDebugViewRectResolution(DebugResolution debugViewRectResolution) {
        this.debugViewRectResolution = debugViewRectResolution;
    }

    @Override
    protected void act(float delta) {
        if ( scene == null )
            return;
        scene.act( delta );

    }

    @Override
    protected void draw() {
        if ( currentController  != null ) {
            currentController.setCameraZoom(getCamera().zoom);
            currentController.render();
        }
    }

    @Override
    protected void debugRender() {

        PhysWorld.get().debugRender( getStage() );

        getShapeRenderer().setProjectionMatrix( getStage().getBatch().getProjectionMatrix() );
        getShapeRenderer().setTransformMatrix( getStage().getBatch().getTransformMatrix() );
        getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        getShapeRenderer().setColor(0.2f, 0.8f, 1f, 1);
        switch (debugViewRectResolution) {

            case R_NONE:
                break;
            case R_800x600:
                drawViewRect(800,600);
                break;
            case R_960x540:
                drawViewRect(960,540);
                break;
            case R_1280x768:
                drawViewRect(1280,768);
                break;
            case R_1920x1080:
                drawViewRect(1920,1080);
                break;
            case R_2560x1600:
                drawViewRect(2560,1600);
                break;
        }
        getShapeRenderer().end();
    }

    private void drawViewRect(float w, float h) {
        OrthographicCamera camera = getCamera();
        getShapeRenderer().rect( camera.position.x - w/2 - 1, camera.position.y - h/2 - 1,
                w+2, h+2);
    }

    private static final Vector2 coordV = new Vector2();

    @Override
    protected boolean clicked(int screenX, int screenY, int button) {
        boolean res = false;
        if ( currentController != null ) {
            coordV.set( screenX, screenY );
            res = currentController.mouseClicked( getStage().screenToStageCoordinates(coordV), button );
        }

        if ( res )
            return res;
        if ( button == Input.Buttons.LEFT )  {
            coordV.set( screenX, screenY );
            res = processSelection(getStage().screenToStageCoordinates(coordV));
        }

        if ( res )
            return res;
//        Gdx.app.log("EditorScreen.clicked", "Event unhandled");
        return false;
    }


    void itemSelected( PhysModelItem mi ) {
        boolean ctrl = false;

        if ( Gdx.input.isKeyPressed( Input.Keys.CONTROL_LEFT) ) {
           ctrl = true;
        }
        if ( selectionListener != null ) {
            selectionListener.itemsSelected(mi, ctrl);
        }
    }

    boolean processSelection( Vector2 stageCoord ) {

        switch (selectionMode) {
            case DISABLED:
                return false;
            case MODEL_ITEM:
                return processModelItemSelection( stageCoord );
        }

        return false;

    }

    boolean processModelItemSelection( Vector2 stageCoord ) {
        for ( Actor a : scene.getChildren() ) {
            if ( !(a instanceof PhysModelItem ) )
                continue;

            PhysModelItem mi = (PhysModelItem) a;

            for (BodyItem  bi : mi.getModel().getBodyItems() ) {
                RectangleExt bb = bi.getBoundingBox();
                if ( !bb.contains( stageCoord ) )
                    continue;
//                Gdx.app.log("EditorScreen.processSelection", "ModelItem: " + mi);
                itemSelected( mi );
                return true;
            }
        }

        return false;
    }

    @Override
    protected boolean doubleClicked(int screenX, int screenY, int button) {

        if ( currentController != null ) {
            coordV.set( screenX, screenY );

            return currentController.mouseDoubleClicked( getStage().screenToStageCoordinates(coordV), button );
        }

        return false;
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean res = super.touchDown( screenX, screenY, pointer, button);
        if ( res )
            return res;
        if ( button == Input.Buttons.LEFT && currentController != null ) {
            coordV.set( screenX, screenY );
            res =  currentController.touchDown(getStage().screenToStageCoordinates(coordV));
        }
        if ( res )
            return res;

//        Gdx.app.log("EditorScreen.touchDown", "Event unprocessed");

        return res;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boolean res = super.touchUp( screenX,screenY, pointer, button );

        if ( res )
            return res;

        if ( button == Input.Buttons.LEFT && currentController != null ) {
            coordV.set( screenX, screenY );
            res = currentController.touchUp( getStage().screenToStageCoordinates(coordV), button );
        }

        if ( res )
            return res;

//        Gdx.app.log("EditorScreen.touchUp", "Event unprocessed");

        return res;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        boolean res = super.touchDragged( screenX, screenY, pointer );

        if ( res )
            return res;

        if ( Gdx.input.isButtonPressed( Input.Buttons.LEFT ) && currentController != null ) {
            coordV.set( screenX, screenY );
            res = currentController.touchDragged( getStage().screenToStageCoordinates(coordV) );
        }
        if ( res )
            return res;

//        Gdx.app.log("EditorScreen.touchDragged", "Event unhandled");

        return res;
    }
}
