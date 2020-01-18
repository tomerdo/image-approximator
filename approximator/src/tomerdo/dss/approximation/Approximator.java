package tomerdo.dss.approximation;

import tomerdo.dss.shapes.EShape;
import tomerdo.dss.algorithms.IAlgorithm;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

/**
 * this class computing the approximation of given image, using {@link IAlgorithm optimation algorithm }
 * and returns {@link ImageApproximation as outout} through the function approximate
 */
public class Approximator {
    private static final double MAX_THRESHOLD = 50.0; // if it's worse then 50%
    private IAlgorithm algorithm;

    public Approximator(IAlgorithm algorithm){
        this.algorithm = algorithm;
    }

    /**
     *
     * @param image - input image we want to approximate
     * @return optional of {@link ImageApproximation} - empty if the algorithm did'nt converge ,else -> the result
     */
    public Optional<ImageApproximation> approximate(BufferedImage image){
        System.out.println("the size of the image is " + image.getWidth() + " * "  + image.getHeight() );
        ImageApproximation result = algorithm.findBestSolution(image, EShape.ELLIPSE);

        if (result.getSimpleShapeList().size() == 0 || result.getScore() > MAX_THRESHOLD){
            System.out.println("the best solution is not good enough , the result is " + result.getScore());
            return Optional.empty();
        }

        return Optional.ofNullable(result);
    }
}
