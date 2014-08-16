package org.skr.gdx.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import org.skr.gdx.utils.RectangleExt;

/**
 * Created by rat on 06.08.14.
 */
public class CameraController {

    OrthographicCamera camera;
    PhysScene scene;

    boolean holdCameraInsideBorders = true;

    RectangleExt viewRect = new RectangleExt();

    public CameraController( PhysScene scene ) {
        this.scene = scene;
        this.camera = (OrthographicCamera) scene.getStage().getCamera();
    }

    public boolean isHoldCameraInsideBorders() {
        return holdCameraInsideBorders;
    }

    public void setHoldCameraInsideBorders(boolean holdCameraInsideBorders) {
        this.holdCameraInsideBorders = holdCameraInsideBorders;
    }

    protected void controlSceneBorders() {

        float w = camera.viewportWidth * camera.zoom;
        float h = camera.viewportHeight * camera.zoom;

        if ( !holdCameraInsideBorders) {
            viewRect.set( camera.position.x - w/2 ,
                    camera.position.y - h/2, w, h);
            return;
        }

        float vW = scene.getViewRight() - scene.getViewLeft();
        float vH = scene.getViewTop() - scene.getViewBottom();

        if ( w > vW ) {
            camera.zoom = vW / camera.viewportWidth;
            h = camera.viewportHeight * camera.zoom;
            w = vW;
        }

        float w2 = w / 2f;
        float h2 = h / 2f;

        float x = camera.position.x;
        float y = camera.position.y;

        if ( (x - w2) < scene.getViewLeft() ) {
            camera.position.set( scene.getViewLeft() + w2, y, 0 );
        } else if ( ( x + w2 ) > scene.getViewRight() ) {
            camera.position.set( scene.getViewRight() - w2, y, 0 );
        }

        if ( ( y - h2 ) < scene.getViewBottom() ) {
            camera.position.set( x, scene.getViewBottom() + h2, 0);
        } else if ( ( y + h2 ) > scene.getViewTop() ) {
            camera.position.set( x, scene.getViewTop() - h2, 0 );
        }

        viewRect.set( camera.position.x - w2,
                camera.position.y - h2, w, h);

    }

    public void act( float delta ) {
        if ( holdCameraInsideBorders )
            controlSceneBorders();

    }

    public RectangleExt getViewRect() {
        return viewRect;
    }
}
