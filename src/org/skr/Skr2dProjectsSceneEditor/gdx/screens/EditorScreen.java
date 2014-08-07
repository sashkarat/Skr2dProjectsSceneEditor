package org.skr.Skr2dProjectsSceneEditor.gdx.screens;

import org.skr.gdx.editor.BaseScreen;
import org.skr.gdx.scene.PhysScene;

/**
 * Created by rat on 02.08.14.
 */
public class EditorScreen extends BaseScreen {

    PhysScene scene;

    public PhysScene getScene() {
        return scene;
    }

    public void setScene(PhysScene scene) {
        this.scene = scene;
        this.scene.initializeScene( getStage() );
    }

    @Override
    protected void act(float delta) {
        if ( scene == null )
            return;
        scene.act( delta );

    }

    @Override
    protected void draw() {
        if ( scene == null )
            return;

        scene.draw( getStage().getBatch(), 1 );
    }
}
