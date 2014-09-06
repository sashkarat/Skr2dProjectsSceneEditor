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
        AAG,
        SELECTION_GROUPS,
        SELECTION_GROUP
        }

    private Type type;
    private String name = null;

    public SceneTreeNode(Type type, Object object ) {
        this.type = type;
        setUserObject( object );
    }

    public SceneTreeNode(Type type, Object object, String name ) {
        this.type = type;
        this.name = name;
        setUserObject( object );
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {

        if ( name != null ) {
            if ( !name.isEmpty() ) {
                return name;
            }
        }

        if ( getUserObject() != null )
            return getUserObject().toString();
        return super.toString();
    }
}
