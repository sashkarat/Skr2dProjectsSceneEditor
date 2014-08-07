package org.skr.gdx.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by rat on 07.08.14.
 */
public class Utils {

    private static final float [] xc = new float[4];
    private static final float [] yc = new float[4];
    private static final RectangleExt rectTmp = new RectangleExt();

    public static RectangleExt getBBox( Rectangle rect, float originX, float originY, float rotation ) {
        float r = (float)Math.toRadians(rotation);
        float c = (float)Math.cos(r);
        float s = (float)Math.sin(r);

        float x = rect.getX();
        float y = rect.getY();
        float width = rect.getWidth();
        float height = rect.getHeight();




        xc[1] = x + c * (0 - originX) + -s * (0 - originY) + originX;
        yc[1] = y + s * (0 - originX) + c * (0 - originY) + originY;

        xc[2]= x + c * (width - originX) + -s * (0 - originY) + originX;
        yc[2] = y + s * (width - originX) + c * (0 - originY) + originY;

        xc[3]= x + c * (width - originX) + -s * (height - originY) + originX;
        yc[3] = y + s * (width - originX) + c * (height - originY) + originY;

        xc[4] = x + c * (0 - originX) + -s * (height - originY) + originX;
        yc[4] = y + s * (0 - originX) + c * (height - originY) + originY;

        float xmin = 99999999;
        float xmax = -99999999;

        float ymin = 9999999;
        float ymax = -9999999;

        for ( int i = 0; i < 4; i++) {
            xmin = Math.min( xmin, xc[i] );
            xmax = Math.max(xmax, xc[i]);

            ymin = Math.min( ymin, yc[i] );
            ymax = Math.max( ymax, yc[i] );
        }

        rectTmp.set( xmin, ymin, xmax - xmin, ymax - ymin );

        return rectTmp;
    }

}
