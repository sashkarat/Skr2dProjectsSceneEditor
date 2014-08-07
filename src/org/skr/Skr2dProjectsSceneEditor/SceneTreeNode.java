package org.skr.Skr2dProjectsSceneEditor;

import org.skr.gdx.scene.PhysScene;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by rat on 04.08.14.
 */
public class SceneTreeNode extends DefaultMutableTreeNode {
    public static enum Type {
        ROOT,
        MODEL_DESC_HANDLER,
        MODEL_ITEM,
        LAYERS_GROUP,
        LAYER
        }

    private Type type;

    public SceneTreeNode(Type type ) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        switch ( type ) {

            case ROOT:
                break;
            case MODEL_DESC_HANDLER:
                break;
            case MODEL_ITEM:
                break;
            case LAYERS_GROUP:
                PhysScene scene = (PhysScene) getUserObject();
                return scene.getName() + " : LAYERS ";
            case LAYER:
                break;
        }

        if ( getUserObject() != null )
            return getUserObject().toString();
        return super.toString();
    }
}
