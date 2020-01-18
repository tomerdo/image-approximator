package tomerdo.dss.algorithms.hill_climbing;

import tomerdo.dss.algorithms.IAlgorithm;
import tomerdo.dss.approximation.ImageApproximation;
import tomerdo.dss.shapes.ShapeFactory;
import tomerdo.dss.utils.Constants;
import tomerdo.dss.utils.ImageUtils;
import tomerdo.dss.shapes.EShape;
import tomerdo.dss.shapes.SimpleShape;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * this class implements heuristic of optimizing in batches
 */
public class BatchingStochasticHillClimbingAlgorithm extends AbstractStochasticHillClimbing implements IAlgorithm {
    private static final int NUM_OF_SHAPES_IN_BATCH = 10;

    @Override
    public ImageApproximation findBestSolution(BufferedImage image, EShape shape) {
        shapeFactory = new ShapeFactory(shape);

        List<SimpleShape> initialShapes
                = shapeFactory.createSomeSimpleShapes(Constants.NUM_OF_SHAPES, image.getWidth(), image.getHeight());
        ImageApproximation imageApproximation = new ImageApproximation(initialShapes , Double.MAX_VALUE);


        int numOfBatches = Constants.NUM_OF_SHAPES / NUM_OF_SHAPES_IN_BATCH;

        BufferedImage currentBufferImage = ImageUtils.createCleanBufferImage(image);

        List<SimpleShape> result = new ArrayList<>(Constants.NUM_OF_SHAPES);

        for (int batchIndex = 0; batchIndex < numOfBatches ; batchIndex++ ){
            // get the ith batch
            List<SimpleShape> simpleShapesBatch
                    = imageApproximation.getSimpleShapeList()
                    .subList(batchIndex * NUM_OF_SHAPES_IN_BATCH, (batchIndex + 1) * NUM_OF_SHAPES_IN_BATCH);
            // running the stochastic hill climbing optimization
            ImageApproximation imageApproximationForBatch = optimize(simpleShapesBatch, image , currentBufferImage, imageApproximation.getScore(), 0,1, Constants.MAX_NUM_OF_ITERATION);

            imageApproximation.setScore(imageApproximationForBatch.getScore());
            result.addAll(imageApproximationForBatch.getSimpleShapeList());

            currentBufferImage =
                    ImageUtils.addShapeToCurrentImage(result, currentBufferImage);
            System.out.println("we are now working on batch #" + batchIndex + " the loss is " + imageApproximationForBatch.getScore());
        }

        // run optimization on all the image (with smaller shapes)
        System.out.println("num of shapes " + result.size());
        // another iteration with smaller shapes on all the shapes
        return optimize(result, currentBufferImage, image, imageApproximation.getScore(), 0, 0.25, Constants.MAX_NUM_OF_ITERATION);

    }

}
