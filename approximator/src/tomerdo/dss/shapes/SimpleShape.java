package tomerdo.dss.shapes;

import java.awt.*;

/**
 * this class represents a simple shape, to the use of the algorithms
 *
 */
public abstract class SimpleShape {

    public SimpleShape(){

    }

    /**
     * copy constructor
     * @param otherShape - other shape we want to copy
     */
    public SimpleShape(SimpleShape otherShape){
        this(otherShape.width,otherShape.height);
        setX(otherShape.getX());
        setY(otherShape.getY());
        setColor(otherShape.getColor());
        setOpacity(otherShape.getOpacity());
        setOrientation(otherShape.getOrientation());
        setXSize(otherShape.getXSize());
        setYSize(otherShape.getYSize());
    }
    // x coordinate of the center
    private int x;

    // y coordinate of the center
    private int y;

    // the size of the shape
    private int xSize;

    private int ySize;

    // the orientation of the shape (in degrees)
    private int orientation;

    // the color of the shape
    private Color color;

    // in percentages
    private double opacity;

    private int width;

    private int height;

    public SimpleShape(int width, int height) {
        this.width = width;
        this.height = height;
    }

    // Getters and Setters (the setters are fluent)

    public int getX() {
        return x;
    }

    public SimpleShape setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public SimpleShape setY(int y) {
        this.y = y;
        return this;
    }

    public int getXSize() {
        return xSize;
    }

    public SimpleShape setXSize(int xSize) {
        this.xSize = xSize;
        return this;
    }

    public int getOrientation() {
        return orientation;
    }

    public SimpleShape setOrientation(int orientation) {
        this.orientation = orientation;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public SimpleShape setColor(Color color) {
        this.color = color;
        return this;
    }

    public double getOpacity() {
        return opacity;
    }

    public SimpleShape setOpacity(double opacity) {
        this.opacity = opacity;
        return this;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getYSize() {
        return ySize;
    }

    public SimpleShape setYSize(int ySize) {
        this.ySize = ySize;
        return this;
    }

    public void mutatePosition() {
        int x = Math.toIntExact(Math.round(Math.random() * width));
        int y = Math.toIntExact(Math.round(Math.random() * height));
        setX(x);
        setY(y);
    }

    public void mutateColor() {
        mutateColor(false);
    }

    public void mutateColor(boolean lowResolution) {
        int boundRed = 255;
        int boundBlue = 255;
        int boundGreen = 255;
        int boundAlpha = 255;

        if (lowResolution){
            boundRed = 10;
            boundBlue = 10;
            boundGreen = 10;
            boundAlpha = 10;
        }

        int red = Math.toIntExact(Math.round(Math.random() * boundRed));
        int blue = Math.toIntExact(Math.round(Math.random() * boundBlue));
        int green = Math.toIntExact(Math.round(Math.random() * boundGreen));
        int alpha = Math.toIntExact(Math.round(Math.random() * boundAlpha));
        Color color;

        if (lowResolution){
            color = new Color(5 + red * 25, 5 + green * 25, 5 + blue * 25, 5 + alpha * 25);
        }else {
            color = new Color(red,green ,blue , alpha);
        }

        setColor(color);
    }

    public void mutateSize(double minSizeBound, double maxSizeBound) {
        double maxWidth = getWidth() * maxSizeBound;
        double maxHeight = getHeight() * maxSizeBound;
        double minWidth = getWidth() * minSizeBound;
        double minHeight = getWidth() * minSizeBound;

        int randomX = (int) Math.round(minWidth + Math.random() * (maxWidth - minWidth));
        int randomY = (int) Math.round(minHeight + Math.random() * (maxHeight - minHeight) );

        setXSize(randomX);
        setYSize(randomY);
    }

    public void mutateOrientation(){
         int orientation = (int) Math.round(Math.random() * 36);
        setOrientation(orientation);
    }

    /**
     * Visitor pattern
     * @param g - graphic to draw the shape on
     */
    public abstract void draw(Graphics2D g);
}
