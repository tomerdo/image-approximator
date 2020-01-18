package tomerdo.dss.algorithms.genetic_algorithm;

import tomerdo.dss.approximation.ImageApproximation;
import tomerdo.dss.shapes.EShape;
import tomerdo.dss.shapes.ShapeFactory;
import tomerdo.dss.shapes.SimpleShape;
import tomerdo.dss.utils.Constants;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * this class represents a population of {@link tomerdo.dss.approximation.ImageApproximation}
 * used in Genetic Algorithm -> for compute fittest and make all the GA framework
 * where each {@link ImageApproximation} is individual
 */
public class Population {

    private ImageApproximation fittest;
    private List<ImageApproximation> population;
    private ShapeFactory shapeFactory;


    public Population(int numOfIndividuals, int width, int height, ShapeFactory shapeFactory){
        this.shapeFactory = shapeFactory;
        population = new ArrayList<>(numOfIndividuals);
        for (int i = 0; i < numOfIndividuals; i++) {
            // creating the genome for each individual
            List<SimpleShape> someSimpleShapes = shapeFactory.createSomeSimpleShapes(Constants.NUM_OF_SHAPES, width, height);
            population.add(new ImageApproximation(someSimpleShapes));
        }
    }

    public Population(List<ImageApproximation> imageApproximations){
        population = imageApproximations;
    }

    public List<ImageApproximation> getPopulation(){
        return population;
    }

    public ImageApproximation getFittest(){
        return fittest;
    }

    public void computeFitness(BufferedImage image) {
        double bestFitness = Double.MAX_VALUE;

        for (ImageApproximation imageApproximation : population){
            double currFitness = imageApproximation.computeFitness(image);

            if (currFitness < bestFitness){
                bestFitness = currFitness;
                fittest = imageApproximation;
            }
        }
    }
}
