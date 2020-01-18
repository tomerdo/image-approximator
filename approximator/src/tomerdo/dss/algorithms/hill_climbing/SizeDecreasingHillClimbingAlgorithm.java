package tomerdo.dss.algorithms.hill_climbing;

import tomerdo.dss.algorithms.IAlgorithm;
import tomerdo.dss.approximation.ImageApproximation;
import tomerdo.dss.shapes.EShape;
import tomerdo.dss.shapes.ShapeFactory;
import tomerdo.dss.shapes.SimpleShape;
import tomerdo.dss.utils.Constants;
import tomerdo.dss.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * this is hill climber , with heuristic that we should optimize first with big values and later with
 * decreased size shapes
 */
public class SizeDecreasingHillClimbingAlgorithm extends AbstractStochasticHillClimbing implements IAlgorithm {
    private double lower = 0.1;
    private double medium = 0.5;

    /**
     * consturctor with params on the ceilings
     * @param lower - ceil of the lower size (in percentage)
     * @param medium - ceil of the medium size (in percentage)
     */
    public SizeDecreasingHillClimbingAlgorithm(double lower, double medium) {
        this.lower = lower;
        this.medium = medium;
    }

    @Override
    public ImageApproximation findBestSolution(BufferedImage image, EShape shape) {
        shapeFactory = new ShapeFactory(shape);
        // the heuristic is to start with big shapes and decrease the size
        List<SimpleShape> initialShapes
                = shapeFactory.createSomeSimpleShapes(Constants.NUM_OF_SHAPES , image.getWidth(), image.getHeight(), medium , 1 );

        ImageApproximation imageApproximation = new ImageApproximation(initialShapes , Double.MAX_VALUE);

        int totalNumOfIterations = 10000;

        // distributing the total time equally between the sizes
        int numOfIterations
                = medium > 0 ?
                lower > 0 ? totalNumOfIterations / 3 : totalNumOfIterations / 2
                : lower > 0 ? totalNumOfIterations / 3 : totalNumOfIterations;



        imageApproximation = optimize(imageApproximation.getSimpleShapeList() , null , image, imageApproximation.getScore() , medium,1 , numOfIterations);
        System.out.println("the score after the larger shapes is " + imageApproximation.getScore());
        ImageUtils.showResult(imageApproximation, image);

        // option to split the computation to parts by sizes
        if (medium > 0){
            imageApproximation = optimize(imageApproximation.getSimpleShapeList() , null , image, imageApproximation.getScore() , lower , medium , numOfIterations);
            System.out.println("the score after the medium shapes is " + imageApproximation.getScore());
            ImageUtils.showResult(imageApproximation, image);
        }

        // the lower part
        if (lower > 0){
            imageApproximation = optimize(imageApproximation.getSimpleShapeList() , null, image, imageApproximation.getScore() , 0,lower , numOfIterations);
            System.out.println("the score after the small shapes is " + imageApproximation.getScore());
            ImageUtils.showResult(imageApproximation, image);
        }

        return imageApproximation;
    }
}
