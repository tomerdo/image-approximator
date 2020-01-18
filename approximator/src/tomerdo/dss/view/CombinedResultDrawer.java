package tomerdo.dss.view;

import tomerdo.dss.approximation.ImageApproximation;
import tomerdo.dss.utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import static tomerdo.dss.utils.Constants.*;

/**
 * used for drawing the output of the algorithm
 * the rational was to draw the input and output together (not implemented due time constrains)
 */
public class CombinedResultDrawer implements IResultDrawer {

    @Override
    public void draw(ImageApproximation output, BufferedImage imageInput, String inputFilePath) {
        BufferedImage bufferedImage = ImageUtils.fromImageApproximationToImage(output, imageInput);
        try {
            ImageIO.write(bufferedImage, FILE_TYPE, new File(createOutputFilePath(inputFilePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createOutputFilePath(String inputFilePath){
        String[] split = inputFilePath.split(DOT_REGEX);
        String newName = split[1] + OUTPUT_SUFFIX + System.currentTimeMillis();
        return "." + newName + "." + split[2];
    }
}
