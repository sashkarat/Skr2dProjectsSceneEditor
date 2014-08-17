package org.skr.Skr2dProjectsSceneEditor;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by rat on 04.08.14.
 */
public class SceneTreeNode extends DefaultMutableTreeNode {
    public static enum Type {
        ROOT,
        MODELS,
        MODEL_DESC_HANDLER,
        MODEL_ITEM,
        LAYERS_GROUP,
        LAYER,
        TILED_ACTOR,
        AAG
        }

    private Type type;

    public SceneTreeNode(Type type, Object object ) {
        this.type = type;
        setUserObject( object );
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        switch ( type ) {

            case ROOT:
                break;
            case MODELS:
                return ": MODELS ";
            case MODEL_DESC_HANDLER:
                break;
            case MODEL_ITEM:
                break;
            case LAYERS_GROUP:
                break;
            case LAYER:
                break;
            case TILED_ACTOR:
                break;
            case AAG:
                break;
        }

        if ( getUserObject() != null )
            return getUserObject().toString();
        return super.toString();
    }
}
