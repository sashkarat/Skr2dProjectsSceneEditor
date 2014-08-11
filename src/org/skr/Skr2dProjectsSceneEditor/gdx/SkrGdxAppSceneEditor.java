package org.skr.Skr2dProjectsSceneEditor.gdx;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import org.skr.Skr2dProjectsSceneEditor.gdx.screens.EditorScreen;
import org.skr.gdx.PhysWorld;
import org.skr.gdx.SkrGdxApplication;
import org.skr.gdx.scene.PhysScene;

/**
 * Created by rat on 30.05.14.
 */
public class SkrGdxAppSceneEditor extends SkrGdxApplication {

    EditorScreen editorScreen;


    public EditorScreen getEditorScreen() {
        return editorScreen;
    }

    @Override
    protected void onCreate() {
        editorScreen = new EditorScreen();
        toggleEditorScreen();
    }


    public void toggleEditorScreen() {
        toggleCurrentScreen( editorScreen );
    }


    @Override
    protected void onDispose() {
        editorScreen.dispose();
    }


}
