package tomerdo.dss.algorithms.hill_climbing;

import tomerdo.dss.algorithms.IAlgorithm;
import tomerdo.dss.approximation.ImageApproximation;
import tomerdo.dss.shapes.EShape;
import tomerdo.dss.shapes.ShapeFactory;
import tomerdo.dss.shapes.SimpleShape;

import java.awt.image.BufferedImage;

import static tomerdo.dss.utils.ImageUtils.calculateLoss;

/**
 * moving the params on by one
 */
public class NaiveHillClimbingAlgorithm implements IAlgorithm {
    private static final double EPSILON = 1.0;
    private static final int MAX_ITERATIONS = 100000;
    private static final int MAX_NUM_OF_SHAPES = 100;
    private ShapeFactory shapeFactory;

    @Override
    public ImageApproximation findBestSolution(BufferedImage image, EShape shape) {
        shapeFactory = new ShapeFactory(shape);

        int numOfIteration = 0;
        ImageApproximation result = new ImageApproximation();
        SimpleShape firstShape =
                shapeFactory.createSimpleShape(image.getWidth(null) , image.getHeight(null), 0, 1);
        result.addShape(firstShape);
        double bestLossScore = calculateLoss(image, result);
        result.setScore(bestLossScore);
        System.out.println("score is: " + bestLossScore );
        // we will use steepest ascent hill climbing
        while (bestLossScore > EPSILON && numOfIteration < MAX_ITERATIONS && result.getSimpleShapeList().size() < MAX_NUM_OF_SHAPES){
            System.out.println("starting iteration " + numOfIteration);
            boolean addShape = false;
            ImageApproximation currentApproximation = new ImageApproximation(result);

            // all the change operations
            SimpleShape lastShape = currentApproximation.getLastAddedShape();
            SimpleShape bestNextShape = lastShape;

            // change the last size
            SimpleShape newShapeWithSize = shapeFactory.nextSize(lastShape);
            currentApproximation.replaceLastShape(newShapeWithSize);
            double changeSizeLoss = calculateLoss(image, currentApproximation);

            if (changeSizeLoss < bestLossScore){
                System.out.println("the increase in the size improve the approx " + changeSizeLoss);
                bestLossScore = changeSizeLoss;
                bestNextShape = newShapeWithSize;
            }


            // change the color
            SimpleShape newShapeColor = shapeFactory.nextColor(lastShape);
            currentApproximation.replaceLastShape(newShapeColor);
            double changeColorLoss = calculateLoss(image , currentApproximation);

            if (changeColorLoss  < bestLossScore){
                System.out.println("changing the position improve the approx " + changeSizeLoss);
                bestNextShape = newShapeColor;
                bestLossScore = changeColorLoss;
            }


            // return to the last shape
            currentApproximation.replaceLastShape(lastShape);

            // keep the last insertion and add a new one
            SimpleShape anotherShape = shapeFactory.createSimpleShape(image.getWidth(null) , image.getHeight(null), 0, 1);
            currentApproximation.addShape(anotherShape);
            double addShapeLoss = calculateLoss(image , currentApproximation);

            if (addShapeLoss < bestLossScore){
                System.out.println("adding another improve the approx " + addShapeLoss);
                bestNextShape = lastShape;
                bestLossScore = addShapeLoss;
                addShape = true;
            }

            // now we should decide in which way to go

            // if there is no hill climbing - return the best result
            if (bestLossScore == result.getScore()){
                System.out.println("the climbing hill is stuck");
                return result;
            }

            // one of the modification made the solution better
            if (!addShape){
                result.replaceLastShape(bestNextShape);
                result.setScore(bestLossScore);
            }
            else { // adding another default shape will improve better then changing the last shape
                result.addShape(anotherShape);
                result.setScore(bestLossScore);
            }

            System.out.println("finishing iteration: " + numOfIteration + "number of shapes: "
                    + result.getSimpleShapeList().size()
                    + " the score is " + result.getScore());
            numOfIteration++;
        }

        return result;
    }

}
