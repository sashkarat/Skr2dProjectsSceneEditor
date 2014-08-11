package org.skr.Skr2dProjectsSceneEditor.PropertiesTableModel;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import org.skr.gdx.physmodel.animatedactorgroup.AnimatedActorGroup;
import org.skr.gdx.scene.PhysScene;

import javax.swing.*;

/**
 * Created by rat on 09.08.14.
 */
public class AagPropertiesTableModel extends PropertiesBaseTableModel {


    private enum Property_ {

        Name(PropertyType.STRING),
        Region(PropertyType.SELECTOR),
        FrameDuration(PropertyType.NUMBER),
        PlayMode(PropertyType.SELECTOR),
        Width(PropertyType.NUMBER, DataRole.VIEW_COORDINATES),
        Height(PropertyType.NUMBER, DataRole.VIEW_COORDINATES),
        KeepAspectRatio(PropertyType.BOOLEAN),
        PosX(PropertyType.NUMBER, DataRole.VIEW_COORDINATES),
        PosY(PropertyType.NUMBER, DataRole.VIEW_COORDINATES),
        Rotation(PropertyType.NUMBER),
        Drawable(PropertyType.BOOLEAN);


        private PropertyType propertyType;
        private DataRole dataRole = DataRole.DEFAULT;
        private boolean editable = true;

        Property_(PropertyType propertyType) {
            this.propertyType = propertyType;
        }

        Property_(PropertyType propertyType, DataRole dataRole) {
            this.propertyType = propertyType;
            this.dataRole = dataRole;
        }

        Property_(PropertyType propertyType, boolean editable) {
            this.propertyType = propertyType;
            this.editable = editable;
        }

        Property_(PropertyType propertyType, DataRole dataRole, boolean editable) {
            this.propertyType = propertyType;
            this.dataRole = dataRole;
            this.editable = editable;
        }

        static Property_[] values = Property_.values();
    }

    private static Property_ getProperty( int index ) {
        return Property_.values[index];
    }

    AnimatedActorGroup aag;
    PhysScene scene;

    public AagPropertiesTableModel(JTree modelJTree) {
        super(modelJTree);
    }

    public AnimatedActorGroup getAag() {
        return aag;
    }

    public void setAag(AnimatedActorGroup aag) {
        this.aag = aag;
    }

    public PhysScene getScene() {
        return scene;
    }

    public void setScene(PhysScene scene) {
        this.scene = scene;
    }

    @Override
    public Object getComboSelectedObject(int rowIndex) {
        if ( aag == null )
            return null;
        switch( getProperty(rowIndex) ) {
            case Region:
                return aag.getTextureName();
            case PlayMode:
                return aag.getPlayMode();
        }
        return null;
    }

    private static final Array<Object> playModesArray = new Array<Object>( Animation.PlayMode.values() );
    private Array<Object> regionNames = new Array<Object>();

    @Override
    public Array<Object> getSelectorArray(int rowIndex) {
        if ( scene == null )
            return null;
        switch (getProperty( rowIndex )) {
            case Region:
                regionNames.clear();
                regionNames.addAll( scene.getTextureRegionNames() );
                return regionNames;
            case PlayMode:
                return playModesArray;
        }

        return null;
    }

    @Override
    public PropertyType getPropertyType(int rowIndex) {
        return getProperty( rowIndex ).propertyType;
    }

    @Override
    public DataRole getDataRole(int rowIndex) {
        return getProperty( rowIndex ).dataRole;
    }

    @Override
    public int getPropertiesCount() {
        return Property_.values.length;
    }

    @Override
    public boolean isPropertyEditable(int rowIndex) {
        return getProperty( rowIndex ).editable;
    }

    @Override
    public Object getPropertyName(int rowIndex) {
        return getProperty( rowIndex ).name();
    }

    @Override
    public Object getPropertyValue(int rowIndex) {

        if ( aag == null )
            return null;
        if ( scene == null )
            return null;

        switch ( getProperty( rowIndex ) ) {

            case Name:
                return aag.getName();
            case Region:
                return aag.getTextureName();
            case FrameDuration:
                return  aag.getFrameDuration();
            case PlayMode:
                return aag.getPlayMode();
            case Width:
                return aag.getWidth();
            case Height:
                return aag.getHeight();
            case KeepAspectRatio:
                return aag.isKeepAspectRatio();
            case PosX:
                return aag.getX();
            case PosY:
                return aag.getY();
            case Rotation:
                return aag.getRotation();
            case Drawable:
                return aag.isDrawable();
        }

        return null;
    }

    @Override
    public void setProperty(Object aValue, int rowIndex) {
        if ( aag == null )
            return;
        if ( scene == null )
            return;

        switch ( getProperty( rowIndex ) ) {

            case Name:
                aag.setName((String) aValue );
                break;
            case Region:
                aag.setTextureName((String) regionNames.get((Integer) aValue ));
                aag.updateTextures( scene.getAtlas() );
                break;
            case FrameDuration:
                aag.setFrameDuration((Float) aValue);
                break;
            case PlayMode:
                aag.setPlayMode((Animation.PlayMode) playModesArray.get((Integer) aValue));
                break;
            case Width:
                aag.setWidth((Float) aValue);
                break;
            case Height:
                aag.setHeight((Float) aValue );
                break;
            case KeepAspectRatio:
                aag.setKeepAspectRatio((Boolean) aValue);
                break;
            case PosX:
                aag.setX((Float) aValue);
                break;
            case PosY:
                aag.setY((Float) aValue);
                break;
            case Rotation:
                aag.setRotation((Float) aValue );
                break;
            case Drawable:
                aag.setDrawable((Boolean) aValue);
                break;
        }

    }
}
