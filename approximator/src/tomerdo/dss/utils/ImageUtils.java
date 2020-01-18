package tomerdo.dss.utils;

import tomerdo.dss.approximation.ImageApproximation;
import tomerdo.dss.shapes.SimpleShape;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * this class used to wrap all the operations that done with images
 */
public class ImageUtils {


    private static JFrame frame;

    /**
     * this function calculate the loss function
     * @param image - input image
     * @param imageApproximation - representation of image approximation
     * @return
     */
    public static double calculateLoss(BufferedImage image, ImageApproximation imageApproximation){
        return calculateLoss(image, fromImageApproximationToImage(imageApproximation, image));
    }


    /**
     * plot the results
     * @param approximation
     * @param originalImage
     */
    public static void showResult(ImageApproximation approximation , BufferedImage originalImage) {
        BufferedImage image = fromImageApproximationToImage(approximation, originalImage);
        if (frame == null) {
            frame = new JFrame();
        }
        frame.getContentPane().removeAll();
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * this function calculate the loss function of two image
     * sum of the difference in all channels, normalized by num of pixels
     * @param image
     * @param imageApproximationAsImage
     * @return
     */
    public static double calculateLoss(BufferedImage image, BufferedImage imageApproximationAsImage){
        int height = image.getHeight();
        int width = image.getWidth();
        long diff = 0;

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int rgb1 = image.getRGB(x, y);
                int rgb2 = imageApproximationAsImage.getRGB(x, y);
                int r1 = (rgb1 >> 16) & 0xff;
                int g1 = (rgb1 >>  8) & 0xff;
                int b1 = (rgb1      ) & 0xff;
                int r2 = (rgb2 >> 16) & 0xff;
                int g2 = (rgb2 >>  8) & 0xff;
                int b2 = (rgb2      ) & 0xff;
                diff += Math.abs(r1 - r2);
                diff += Math.abs(g1 - g2);
                diff += Math.abs(b1 - b2);
            }
        }

        double n = width * height * 3;
        double p = diff / n / 255.0;
        return p * 100;
    }

    public static double calculateLoss(BufferedImage image, ImageApproximation imageApproximation, BufferedImage currentApproximatedImage) {
        if (currentApproximatedImage == null){
            return calculateLoss(image, imageApproximation);
        }
        return calculateLoss(image , addShapeToCurrentImage(imageApproximation.getSimpleShapeList() , currentApproximatedImage));
    }

    /**
     *
     * @param imageApproximation
     * @param image
     * @return
     */
    public static BufferedImage fromImageApproximationToImage(ImageApproximation imageApproximation , BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage bufferedImage = new BufferedImage(width, height, image.getType());


        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();


        for (SimpleShape simpleShape : imageApproximation.getSimpleShapeList()) {
            //drawing the circle
            AffineTransform old = g.getTransform();
            g.rotate(Math.toRadians(simpleShape.getOrientation() * 10));

            g.setColor(simpleShape.getColor());
            g.fillOval(simpleShape.getX() , simpleShape.getY(), simpleShape.getXSize() , simpleShape.getYSize());
            g.setTransform(old);

        }
        return bufferedImage;
    }

    public static BufferedImage createCleanBufferImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, image.getType());
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
        return bufferedImage;
    }


    public static BufferedImage addShapeToCurrentImage(List<SimpleShape> approximation, BufferedImage currentBufferImage) {
        Graphics2D g = (Graphics2D) currentBufferImage.getGraphics();

        for (SimpleShape simpleShape : approximation) {
            simpleShape.draw(g);
        }

        return currentBufferImage;
    }
}
