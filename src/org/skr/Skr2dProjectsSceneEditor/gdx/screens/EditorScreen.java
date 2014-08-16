package org.skr.Skr2dProjectsSceneEditor.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import org.skr.Skr2dProjectsSceneEditor.gdx.controllers.AagController;
import org.skr.gdx.editor.BaseScreen;
import org.skr.gdx.editor.controller.Controller;
import org.skr.gdx.physmodel.animatedactorgroup.AnimatedActorGroup;
import org.skr.gdx.scene.PhysScene;
import org.skr.gdx.utils.ModShapeRenderer;

/**
 * Created by rat on 02.08.14.
 */
public class EditorScreen extends BaseScreen {

    public static enum DebugResolution {
        R_NONE,
        R_800x600,
        R_960x540,
        R_1280x768,
        R_1920x1080,
        R_2560x1600
    }

    public static class SceneHolder extends Group {

    }


    DebugResolution debugViewRectResolution = DebugResolution.R_NONE;

    PhysScene scene;

    SceneHolder sceneHolder;


    Controller currentController = null;
    AagController aagController = null;


    public EditorScreen() {
        super();
        sceneHolder = new SceneHolder();
        getStage().addActor( sceneHolder );
        aagController = new AagController( getStage() );

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

    public void setControllableObject( Object object ) {

        if ( aagController.getAag() != null ) {
            aagController.getAag().setRenderableUserObject( null );
        }

        if ( object instanceof AnimatedActorGroup ) {
            aagController.setAag( (AnimatedActorGroup) object  );
            currentController = aagController;
            return;
        }
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
        shapeRenderer.setProjectionMatrix( getStage().getBatch().getProjectionMatrix() );
        shapeRenderer.setTransformMatrix( getStage().getBatch().getTransformMatrix() );
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.2f, 0.8f, 1f, 1);
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
        shapeRenderer.end();





    }

    private void drawViewRect(float w, float h) {
        OrthographicCamera camera = getCamera();
        shapeRenderer.rect( camera.position.x - w/2 - 1, camera.position.y - h/2 - 1,
                w+2, h+2);
    }

    private static final Vector2 coordV = new Vector2();

    @Override
    protected void clicked(int screenX, int screenY, int button) {
        if ( currentController != null ) {
            coordV.set( screenX, screenY );
            currentController.mouseClicked( getStage().screenToStageCoordinates(coordV), button );

        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean res = super.touchDown( screenX, screenY, pointer, button);
        if ( button == Input.Buttons.LEFT && currentController != null ) {
            coordV.set( screenX, screenY );
            currentController.touchDown(getStage().screenToStageCoordinates(coordV));
        }
        return res;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boolean res = super.touchUp( screenX,screenY, pointer, button );

        if ( button == Input.Buttons.LEFT && currentController != null ) {
            coordV.set( screenX, screenY );
            currentController.touchUp( getStage().screenToStageCoordinates(coordV), button );
        }
        return res;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        boolean res = super.touchDragged( screenX, screenY, pointer );

        if ( Gdx.input.isButtonPressed( Input.Buttons.LEFT ) && currentController != null ) {
            coordV.set( screenX, screenY );
            currentController.touchDragged( getStage().screenToStageCoordinates(coordV) );
        }
        return res;
    }
}
