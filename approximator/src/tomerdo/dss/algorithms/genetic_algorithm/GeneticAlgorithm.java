package tomerdo.dss.algorithms.genetic_algorithm;

import tomerdo.dss.algorithms.IAlgorithm;
import tomerdo.dss.approximation.ImageApproximation;
import tomerdo.dss.shapes.EShape;
import tomerdo.dss.shapes.Ellipse;
import tomerdo.dss.shapes.ShapeFactory;
import tomerdo.dss.shapes.SimpleShape;
import tomerdo.dss.utils.Constants;
import tomerdo.dss.utils.RandomUtils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static tomerdo.dss.utils.RandomUtils.pickNRandomElements;

/**
 * implementation of genetic algorithm to compute the best approximation
 * based on the general GA meta heuristic (some methods names)
 */
public class GeneticAlgorithm implements IAlgorithm {
    private final static int NUMBER_OF_GENERATIONS = 200;
    private double mutationRate = 0.05;
    private int generationSize = 200;
    private double selectionRatio = 0.5;
    private boolean removeBest = true;
    private ShapeFactory shapeFactory;

    public GeneticAlgorithm(){
        // default values
    }

    public GeneticAlgorithm(double mutationRate, int generationSize , boolean removeBest , double selectionRatio){
        this.mutationRate = mutationRate;
        this.generationSize = generationSize;
        this.removeBest = removeBest;
        this.selectionRatio = selectionRatio;
    }


    @Override
    public ImageApproximation findBestSolution(BufferedImage image, EShape shape) {
        shapeFactory = new ShapeFactory(shape);

        Population population = new Population(generationSize , image.getWidth(), image.getHeight(), shapeFactory);
        population.computeFitness(image);

        int genIndex = 0;
        double timeFactor = 50.0 / generationSize;
        int numberOfGenerations = (int) Math.floor(NUMBER_OF_GENERATIONS * timeFactor);
        int reproductionSize = (int) Math.floor(generationSize * selectionRatio);

        System.out.println("the fittest in the init population: "
                + population.getFittest().getScore() + " we are going to do "
                + numberOfGenerations + " generations with reproduction size of " + reproductionSize);

        while (genIndex < numberOfGenerations){
            List<ImageApproximation> selected = selection(population, reproductionSize, generationSize, ESelectionType.TOURNAMENT , removeBest );

            // creating fresh generation
            population = createGeneration(selected, generationSize);
            // we have a new population - lets measure it
            population.computeFitness(image);

            System.out.println("generation # " + genIndex + " the fittest score is " + population.getFittest().getScore());
            genIndex++;
        }
        return population.getFittest();

    }

    /**
     * this function returns the selected individuals
     * @param population - the current population
     * @param removeBest - flag for tournament selection (explained below)
     * @return list of the new selected individuals
     */
    public List<ImageApproximation> selection(Population population, int reproductionSize, int generationSize, ESelectionType selectionType, boolean removeBest) {
        List<ImageApproximation> selected = new ArrayList<>();
        List<ImageApproximation> copyOfPopulation = new ArrayList<>(population.getPopulation());
        for (int i=0; i < reproductionSize; i++) {
            if (selectionType == ESelectionType.ROULETTE) {
                selected.add(rouletteSelection(population, generationSize));
            }
            else if (selectionType == ESelectionType.TOURNAMENT) {
                selected.add(tournamentSelection(copyOfPopulation, reproductionSize,removeBest));
            }
        }

        return selected;
    }

    /**
     * not used in this project
     * @param population - current population
     * @param generationSize - the size of the generation
     * @return
     */
    public ImageApproximation rouletteSelection(Population population , int generationSize) {
         double totalFitness = population.getPopulation().stream().map(ImageApproximation::getScore).mapToDouble(Double::doubleValue).sum();

        // We pick a random value - a point on our roulette wheel
        Random random = new Random();
        int selectedValue = random.nextInt((int) totalFitness);

        // Because we're doing minimization, we need to use reciprocal
        // value so the probability of selecting a genome would be
        // inversely proportional to its fitness - the smaller the fitness
        // the higher the probability
        float recValue = (float) 1/selectedValue;

        // We add up values until we reach out recValue, and we pick the
        // genome that crossed the threshold
        float currentSum = 0;
        for (ImageApproximation genome : population.getPopulation()) {
            currentSum += (float) 1/genome.getScore();
            if (currentSum >= recValue) {
                return genome;
            }
        }

        // In case the return didn't happen in the loop above, we just
        // select at random
        int selectRandom = random.nextInt(generationSize);
        return population.getPopulation().get(selectRandom);
    }


    /**
     * tournament selection strategy
     * @param population - the current population
     * @param tournamentSize - the size of the selected
     * @param removeBest - true if we don't want to select the minimum duplicate times
     * @return
     */
    public ImageApproximation tournamentSelection(List<ImageApproximation> population, int tournamentSize, boolean removeBest) {
        List<ImageApproximation> selected = pickNRandomElements(population, tournamentSize);
        ImageApproximation fittest=
                Collections.min(selected, (imageApproximation1,
                                          imageApproximation2) -> (int) (imageApproximation1.getScore() - imageApproximation2.getScore()));
        if (removeBest){
            population.remove(fittest);
        }
        return fittest;
    }


    /**
     * crossover (mating) between parent1 and parent2
     * @param parent1
     * @param parent2
     * @return - list of two children
     */
    public List<ImageApproximation> crossover(ImageApproximation parent1, ImageApproximation parent2) {
        Random random = new Random();
        int breakpoint = random.nextInt(Constants.NUM_OF_SHAPES);

        List<ImageApproximation> children = new ArrayList<>();


        List<SimpleShape> parent1Genome = parent1.getSimpleShapeList();
        List<SimpleShape> parent2Genome = parent2.getSimpleShapeList();

        List<SimpleShape> children1Genome = new ArrayList<>(parent1Genome.size());
        List<SimpleShape> children2Genome = new ArrayList<>(parent1Genome.size());

        // taking the genes till the break point
        for (int i = 0; i < breakpoint; i++) {
            children1Genome.add(new Ellipse(parent1Genome.get(i)));
            children2Genome.add(new Ellipse(parent2Genome.get(i)));
        }

        // Creating the last part of the genes. opposite parents genes
        for (int i = breakpoint; i < parent1Genome.size(); i++) {
            // need to be refactored - to specific
            children1Genome.add(new Ellipse(parent2Genome.get(i)));
            children2Genome.add(new Ellipse(parent1Genome.get(i)));
        }

        // for now still don't have fitness updated value
        children.add(new ImageApproximation(children1Genome));
        children.add(new ImageApproximation(children2Genome));

        return children;
    }

    /**
     * make mutation to the child imageApproximation (if the odds decided to mutate)
     * @param imageApproximation
     * @return
     */
    public ImageApproximation mutate(ImageApproximation imageApproximation) {
        Random random = new Random();
        float mutate = random.nextFloat();
        if (mutate < mutationRate) {
            return imageApproximationMutate(imageApproximation);
        }
        return imageApproximation;
    }

    public ImageApproximation imageApproximationMutate(ImageApproximation imageApproximation){
        List<SimpleShape> simpleShapeList = imageApproximation.getSimpleShapeList();

        // random index
        int randomIndex = RandomUtils.drawRandom(simpleShapeList.size());
        SimpleShape simpleShape = simpleShapeList.get(randomIndex);

        // random changes
        SimpleShape mutatedShape = shapeFactory.randomMutate(simpleShape , 0.0 , 1.0);

        List<SimpleShape> mutatedShapeList = new ArrayList<>(simpleShapeList);
        mutatedShapeList.set(randomIndex , mutatedShape);
        return new ImageApproximation(mutatedShapeList);
    }
    /**
     *
     * @param population - list of {@link ImageApproximation} the selected ones
     * @param generationSize - the size of the generation
     * @return
     */
    public Population createGeneration(List<ImageApproximation> population, int generationSize) {
        List<ImageApproximation> generation = new ArrayList<>();
        int currentGenerationSize = 0;

        while (currentGenerationSize < generationSize) {
            List<ImageApproximation> parents = pickNRandomElements(population, 2);
            List<ImageApproximation> children = crossover(parents.get(0) , parents.get(1));
            children.set(0, mutate(children.get(0)));
            children.set(1, mutate(children.get(1)));
            generation.addAll(children);
            currentGenerationSize += 2;
        }
        Population newPopulation = new Population(generation);
        return newPopulation;
    }
}
