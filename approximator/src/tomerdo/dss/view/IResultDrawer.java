package tomerdo.dss.view;

import tomerdo.dss.approximation.ImageApproximation;

import java.awt.image.BufferedImage;

public interface IResultDrawer {

    /**
     *
     * @param output output of the algorithm
     * @param imageInput  input image
     * @param inputFilePath input path
     */
    void draw(ImageApproximation output, BufferedImage imageInput, String inputFilePath);
}
