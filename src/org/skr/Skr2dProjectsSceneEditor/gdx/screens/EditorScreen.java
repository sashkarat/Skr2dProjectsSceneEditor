package org.skr.Skr2dProjectsSceneEditor.gdx.screens;

import com.badlogic.gdx.scenes.scene2d.Group;
import org.skr.gdx.editor.BaseScreen;
import org.skr.gdx.scene.PhysScene;

/**
 * Created by rat on 02.08.14.
 */
public class EditorScreen extends BaseScreen {

    public static class SceneHolder extends Group {

    }

    PhysScene scene;
    SceneHolder sceneHolder;

    public EditorScreen() {
        super();
        sceneHolder = new SceneHolder();
        getStage().addActor( sceneHolder );
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

    @Override
    protected void act(float delta) {
        if ( scene == null )
            return;
        scene.act( delta );

    }

    @Override
    protected void draw() {
    }
}
