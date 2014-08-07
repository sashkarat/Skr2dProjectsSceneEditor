package org.skr.gdx.scene;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.skr.gdx.utils.RectangleExt;
import org.skr.gdx.physmodel.animatedactorgroup.AnimatedActorGroup;
import org.skr.gdx.utils.Utils;

/**
 * Created by rat on 06.08.14.
 */
public class TiledActor extends Actor {
    public static enum Type {
        GRID,
        BRICK
    }

    PhysScene scene ;

    AnimatedActorGroup aagBase;
    float aagPosX = 0;
    float aagPosY = 0;
    float spaceX = 0;
    float spaceY = 0;
    int numberX = 4;
    int numberY = 4;
    Type type = Type.BRICK;

    RectangleExt boundingRect = new RectangleExt();
    float prevRot = -99999;


    public TiledActor( PhysScene scene  ) {
        this.scene = scene;
    }

    public AnimatedActorGroup getAagBase() {
        return aagBase;
    }

    public void setAagBase(AnimatedActorGroup aagBase) {
        this.aagBase = aagBase;
    }

    public float getAagPosX() {
        return aagPosX;
    }

    public void setAagPosX(float aagPosX) {
        this.aagPosX = aagPosX;
    }

    public float getAagPosY() {
        return aagPosY;
    }

    public void setAagPosY(float aagPosY) {
        this.aagPosY = aagPosY;
    }

    public float getSpaceX() {
        return spaceX;
    }

    public void setSpaceX(float spaceX) {
        this.spaceX = spaceX;
    }

    public float getSpaceY() {
        return spaceY;
    }

    public void setSpaceY(float spaceY) {
        this.spaceY = spaceY;
    }

    public int getNumberX() {
        return numberX;
    }

    public void setNumberX(int numberX) {
        this.numberX = numberX;
    }

    public int getNumberY() {
        return numberY;
    }

    public void setNumberY(int numberY) {
        this.numberY = numberY;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void loadFromDescription( TiledActorDescription desc ) {

        if ( desc.getAagBaseDesc() != null )
            setAagBase( new AnimatedActorGroup( desc.getAagBaseDesc(), scene.getAtlas() ) );
        setAagPosX( desc.getAagPosX() );
        setAagPosY( desc.getAagPosY() );
        setSpaceX( desc.getSpaceX() );
        setSpaceY( desc.getSpaceY() );
        setNumberX( desc.getNumberX() );
        setNumberY( desc.getNumberY() );
        setType( desc.getType() );
        setName( desc.getName() );

    }

    public TiledActorDescription getDescription() {
        TiledActorDescription desc = new TiledActorDescription();

        if ( aagBase != null )
            desc.setAagBaseDesc( aagBase.getDescription() );
        desc.setAagPosX( getAagPosX() );
        desc.setAagPosY( getAagPosY() );
        desc.setSpaceX( getSpaceX() );
        desc.setSpaceY( getSpaceY() );
        desc.setNumberX( getNumberX() );
        desc.setNumberY( getNumberY() );
        desc.setType( getType() );
        desc.setName( getName() );

        return desc;
    }

    @Override
    public void act(float delta) {
        if ( aagBase == null )
            return;
        aagBase.act(delta);
        aagBase.setPosition(0, 0);

        if ( Math.abs( prevRot - aagBase.getRotation() ) > 4 ) {
            prevRot = aagBase.getRotation();

            boundingRect.set( 0 , 0, aagBase.getWidth(), aagBase.getHeight() );
            if (Math.abs( prevRot ) > 4 ) {
                boundingRect.set(Utils.getBBox( boundingRect, aagBase.getWidth() /2,
                        aagBase.getHeight() /2, aagBase.getRotation() ) );
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if ( aagBase == null )
            return;

        float dX = aagBase.getWidth() + spaceX;
        float dY = aagBase.getHeight() + spaceY;

        float dx2 = dX/2;

        for ( int v = 0; v < numberY; v++) {
            float y = v * dY;
            for ( int u = 0; u < numberX; u++) {
                float x = u * dX;

                switch ( type ) {

                    case GRID:
                        break;
                    case BRICK:
                        if ( ( v & 0x01) == 1 ) {
                            x+= dx2;
                        }

                        break;
                }

                boundingRect.setX( x - boundingRect.getWidth() / 2 );
                boundingRect.setY( y - boundingRect.getHeight() / 2 );

                if ( !scene.getCameraController().getViewRect().overlaps( boundingRect ) )
                    continue;

                aagBase.setPosition( x, y );
                aagBase.draw( batch, parentAlpha );
            }
        }


    }

}
