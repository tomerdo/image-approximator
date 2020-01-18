package tomerdo.dss.shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Ellipse extends SimpleShape {

    public Ellipse(int width, int height) {
        super(width, height);
    }

    public Ellipse(SimpleShape other){
        super(other);
    }

    @Override
    public void draw(Graphics2D g) {
        //drawing the ellipse
        AffineTransform old = g.getTransform();
        g.setColor(getColor());
        g.rotate(Math.toRadians(getOrientation() * 10));
        //draw shape/image (will be rotated)
        g.fillOval(getX() , getY(), getXSize() , getYSize());
        g.setTransform(old);
    }
}
