package org.skr.gdx.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
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

    AnimatedActorGroup aag;
    float aagPosX = 0;
    float aagPosY = 0;
    float spaceX = 0;
    float spaceY = 0;
    int numberX = 4;
    int numberY = 4;
    Type type = Type.BRICK;

    RectangleExt boundingRect = new RectangleExt();
    float prevRot = -99999;
    float prevWidth = -99999999;
    float prevHeight = -9999999;


    public TiledActor( PhysScene scene  ) {
        this.scene = scene;
    }

    public AnimatedActorGroup getAag() {
        return aag;
    }

    public void setAag(AnimatedActorGroup aag) {
        this.aag = aag;
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

        if ( desc.getAagDescription() != null )
            setAag(new AnimatedActorGroup(desc.getAagDescription(), scene.getAtlas()));
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

        if ( aag != null )
            desc.setAagDescription(aag.getDescription());
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

    private void updateBoundingRect() {

        prevRot = aag.getRotation();

        prevWidth = aag.getWidth();
        prevHeight = aag.getHeight();

        boundingRect.set( 0 , 0, aag.getWidth(), aag.getHeight() );
        boundingRect.set(Utils.getBBox( boundingRect, aag.getWidth() /2,
                aag.getHeight() /2, aag.getRotation() ) );
    }

    @Override
    public void act(float delta) {


        if ( aag == null )
            return;
        aag.act(delta);
        aag.setPosition(0, 0);

        if ( Math.abs( prevRot - aag.getRotation() ) > 4 ||
                prevWidth != aag.getWidth() ||
                prevHeight != aag.getHeight() ) {
            updateBoundingRect();

        }
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {

        if ( aag == null )
            return;

        float dX = boundingRect.getWidth() + spaceX;
        float dY = boundingRect.getHeight() + spaceY;

        float dx2 = dX/2;

        for ( int v = 0; v < numberY; v++) {
            float y = v * dY + aagPosY;
            for ( int u = 0; u < numberX; u++) {
                float x = u * dX + aagPosX;

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

//                if ( scene.getCameraController().getViewRect().contains( boundingRect ) ||
//                        scene.getCameraController().getViewRect().overlaps( boundingRect ) ) {
//                }
                aag.setPosition(x, y);
                aag.draw(batch, parentAlpha);
            }
        }


    }

}
