package org.skr.gdx.scene;

import com.badlogic.gdx.Gdx;
import org.skr.gdx.physmodel.PhysModel;

/**
 * Created by rat on 03.08.14.
 */
public class PhysModelDescriptionHandler {

    String internalFilePath = "";
    PhysModel.Description modelDesc = null;


    public PhysModelDescriptionHandler() {
    }

    public boolean loadDescription( String internalFilePath ) {
        setInternalFilePath( internalFilePath );
        return loadDescription();
    }

    public boolean loadDescription() {
        if ( this.internalFilePath.isEmpty() )
            return false;
        modelDesc = PhysModel.loadModelDescription(Gdx.files.internal( this.internalFilePath) );
        if ( modelDesc != null )
            return true;
        return false;
    }


    public String getInternalFilePath() {
        return internalFilePath;
    }

    public void setInternalFilePath(String internalFilePath) {
        this.internalFilePath = internalFilePath;
    }

    public PhysModel.Description getModelDesc() {
        return modelDesc;
    }

    @Override
    public String toString() {
        if ( modelDesc == null ) {
            return super.toString();
        }
        return "PhMDH: " + modelDesc.getName();
    }
}
