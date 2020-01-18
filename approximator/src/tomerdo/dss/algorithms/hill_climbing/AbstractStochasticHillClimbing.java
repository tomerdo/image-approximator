package tomerdo.dss.algorithms.hill_climbing;

import tomerdo.dss.approximation.ImageApproximation;
import tomerdo.dss.shapes.ShapeFactory;
import tomerdo.dss.shapes.SimpleShape;
import tomerdo.dss.utils.Constants;
import tomerdo.dss.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.util.List;

import static tomerdo.dss.utils.RandomUtils.drawRandom;

public abstract class AbstractStochasticHillClimbing {

    protected ShapeFactory shapeFactory;

    protected ImageApproximation optimize(List<SimpleShape> simpleShapesBatch, BufferedImage currentApproximatedImage, BufferedImage image, double bestLoss, double minSize, double maxSize, int maxIterations) {
        ImageApproximation result = new ImageApproximation(simpleShapesBatch, bestLoss);

        int numOfNotImproving = 0;

        for (int i = 0; i < maxIterations && numOfNotImproving < Constants.NUM_OF_NOT_IMPROVE_THRESHOLD ; i++) {
            // random index
            int randomIndex = drawRandom(simpleShapesBatch.size());
            SimpleShape simpleShape = simpleShapesBatch.get(randomIndex);

            // random changes
            SimpleShape mutatedShape = mutate(simpleShape , minSize , maxSize);

            // check score
            result.getSimpleShapeList().set(randomIndex, mutatedShape);
            double currentLoss = ImageUtils.calculateLoss(image, result, currentApproximatedImage);

            // if improve -> update
            if (currentLoss < result.getScore()){
                result.setScore(currentLoss);
                numOfNotImproving = 0;
            }
            else{
                numOfNotImproving ++;
                // returning the last shape
                result.getSimpleShapeList().set(randomIndex, simpleShape);
            }
            if (i % 100 == 0){
                System.out.println("we are now in iteration " + i + " the best lost is " + result.getScore());
            }
        }

        return result;
    }

    private SimpleShape mutate(SimpleShape simpleShape, double minSize , double maxSize) {
        return shapeFactory.randomMutate(simpleShape, minSize , maxSize);
    }

}
