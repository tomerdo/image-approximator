package tomerdo.dss.algorithms;

import tomerdo.dss.approximation.ImageApproximation;
import tomerdo.dss.shapes.EShape;
import tomerdo.dss.shapes.ShapeFactory;
import tomerdo.dss.shapes.SimpleShape;
import tomerdo.dss.utils.Constants;
import tomerdo.dss.utils.ImageUtils;
import tomerdo.dss.utils.RandomUtils;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * this class implementing simulating algorithm
 */
public class SimulatedAnnealing implements IAlgorithm {
    private ShapeFactory shapeFactory;

    private double temperature = 10_000; // default values
    private double coolingRate = 0.003; // default values

    public SimulatedAnnealing(){

    }

    public SimulatedAnnealing(double temperature , double coolingRate){
        this.temperature = temperature;
        this.coolingRate = coolingRate;
    }

    @Override
    public ImageApproximation findBestSolution(BufferedImage image, EShape shape) {
        shapeFactory = new ShapeFactory(shape);
        double initialTemp = temperature;
        // the heuristic is to start with big shapes and decrease the size
        List<SimpleShape> initialShapes
                = shapeFactory.createSomeSimpleShapes(Constants.NUM_OF_SHAPES , image.getWidth(), image.getHeight(), 0 , 1 );

        ImageApproximation imageApproximation = new ImageApproximation(initialShapes , Double.MAX_VALUE);
        int i = 0;
        while (temperature > 1.0){
            // random index
            int randomIndex = RandomUtils.drawRandom(imageApproximation.getSimpleShapeList().size());

            SimpleShape simpleShape = imageApproximation.getSimpleShapeList().get(randomIndex);

            // random changes
            SimpleShape mutatedShape = mutate(simpleShape , 0 , 1);

            imageApproximation.getSimpleShapeList().set(randomIndex, mutatedShape);
            double currentEnergy = imageApproximation.getScore();
            double neighbourEnergy = ImageUtils.calculateLoss(image, imageApproximation);

            // Decide if we should accept the neighbour
            if (acceptanceProbability(currentEnergy, neighbourEnergy, temperature) > Math.random()) {
                // the mutated is already updated
                imageApproximation.setScore(neighbourEnergy);
            }
            else { // revert
                imageApproximation.getSimpleShapeList().set(randomIndex, simpleShape);
            }

            // Cool system
            temperature *= 1-coolingRate;

            if (i % 100 == 0){
                System.out.println("iteration # " + i+ "the best score is " + imageApproximation.getScore() + " the temp is " + temperature);
            }
            i++;
        }
        // reverting - make it to be able to run another time
        temperature = initialTemp;
        return imageApproximation;
    }

    private double acceptanceProbability(double currentEnergy, double neighbourEnergy, double temperature) {
        // If the new solution is better, accept it
        if (neighbourEnergy < currentEnergy) {
            return 1.0;
        }
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((currentEnergy - neighbourEnergy) / temperature);
    }

    private SimpleShape mutate(SimpleShape simpleShape, double minSize , double maxSize) {
        return shapeFactory.randomMutate(simpleShape, minSize , maxSize);
    }

}
