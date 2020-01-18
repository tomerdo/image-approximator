package tomerdo.dss.shapes;


import tomerdo.dss.utils.RandomUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * this class used to wrap the shapes creation
 */
public class ShapeFactory{
    private RandomUtils randomUtils = new RandomUtils();
    private EShape shape;

    public ShapeFactory(EShape shape){
        this.shape = shape;
    }

    public SimpleShape createSimpleShape(int width, int height, double minSizeBound, double maxSizeBound){
        SimpleShape simpleShape;

        switch (shape){
            case ELLIPSE:
                // int the future will be added with more shapes
            default:
                simpleShape = new Ellipse(width , height);
        }
        int x = Math.toIntExact(Math.round(Math.random() * width));
        int y = Math.toIntExact(Math.round(Math.random() * height));
        simpleShape.setX(x);
        simpleShape.setY(y);

        simpleShape.mutateSize(minSizeBound , maxSizeBound);

        Color color = new Color(0, 0, 0, 0 );
        simpleShape.setColor(color);

        // random the color
        simpleShape.mutateColor();

        simpleShape.mutateOrientation();
        return simpleShape;
    }

    public  SimpleShape nextSize(SimpleShape simpleShape){
        SimpleShape result;
        switch (shape){
            case ELLIPSE:
                // int the future will be added with more shapes
            default:
                result = new Ellipse(simpleShape);
        }
        result.setXSize(simpleShape.getXSize() + 1);
        return result;
    }


    public SimpleShape nextColor(SimpleShape simpleShape){
        SimpleShape result;
        switch (shape){
            case ELLIPSE:
                // int the future will be added with more shapes
            default:
                result = new Ellipse(simpleShape);
        }
        Color oldColor = simpleShape.getColor();
        int newRed = oldColor.getRed() + 10;
        int newBlue = oldColor.getBlue() + 10;
        int newGreen = oldColor.getGreen() + 10;
        result.setColor(new Color(newRed, newGreen, newBlue));
        return result;
    }

    public List<SimpleShape> createSomeSimpleShapes(int numOfShapes, int width, int height) {
        return createSomeSimpleShapes(numOfShapes,width,height , 0 , 1);
    }

    /**
     * mutates one of the shape parameters with equally probability
     * @param simpleShape
     * @param minSize
     * @param maxSize
     * @return
     */
    public SimpleShape randomMutate(SimpleShape simpleShape, double minSize, double maxSize) {
        SimpleShape mutatedShape;
        switch (shape){
            case ELLIPSE:
                // int the future will be added with more shapes
            default:
                mutatedShape = new Ellipse(simpleShape);
        }

        int largeNumber = randomUtils.drawLargeInt();
        int choice = largeNumber % 4;
        switch (choice){
            case 1:
                // mutate color
                mutatedShape.mutateColor();
                break;
            case 2:
                // mutate size
                mutatedShape.mutateSize(minSize, maxSize);
                break;
            case 3:
                // mutate orientation
                mutatedShape.mutateOrientation();
                break;
            default: // case 0 - mutate position
                mutatedShape.mutatePosition();
        }
        return mutatedShape;
    }


    public  List<SimpleShape> createSomeSimpleShapes(int numOfShapes, int width, int height, double minSizeBound, double maxSizeBound) {
        List<SimpleShape> shapeList = new ArrayList<>(numOfShapes);
        for (int i = 0; i < numOfShapes; i++) {
            shapeList.add(createSimpleShape(width, height,minSizeBound,maxSizeBound));
        }
        return shapeList;
    }

}
