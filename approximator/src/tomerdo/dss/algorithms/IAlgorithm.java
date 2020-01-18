package tomerdo.dss.algorithms;

import tomerdo.dss.approximation.ImageApproximation;
import tomerdo.dss.shapes.EShape;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * the core of the project, each algorithm that implement this interface
 * will implement optimization algorithm that finds the approximation for @image with shape of type @shape
 */
public interface IAlgorithm {
    ImageApproximation findBestSolution(BufferedImage image, EShape shape);
}
