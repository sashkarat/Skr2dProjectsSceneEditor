package org.skr.Skr2dProjectsSceneEditor.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import org.skr.Skr2dProjectsSceneEditor.gdx.controllers.AagController;
import org.skr.gdx.editor.BaseScreen;
import org.skr.gdx.editor.controller.Controller;
import org.skr.gdx.physmodel.animatedactorgroup.AnimatedActorGroup;
import org.skr.gdx.scene.PhysScene;

/**
 * Created by rat on 02.08.14.
 */
public class EditorScreen extends BaseScreen {

    public static class SceneHolder extends Group {

    }

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
