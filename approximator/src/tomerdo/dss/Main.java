package tomerdo.dss;

import tomerdo.dss.algorithms.genetic_algorithm.GeneticAlgorithm;
import tomerdo.dss.algorithms.IAlgorithm;
import tomerdo.dss.algorithms.hill_climbing.SizeDecreasingHillClimbingAlgorithm;
import tomerdo.dss.approximation.Approximator;
import tomerdo.dss.approximation.ImageApproximation;
import tomerdo.dss.view.CombinedResultDrawer;
import tomerdo.dss.view.IResultDrawer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        List<String> inputFilesPath = new ArrayList<>();
        List<IAlgorithm> algorithms = new ArrayList<>();

        initInputsAndAlgorithms(inputFilesPath , algorithms);

        inputFilesPath.forEach(inputFile ->
                algorithms.forEach(
                        algorithm -> runApproximation(algorithm , inputFile))
        );

    }

    private static void runApproximation(IAlgorithm algorithm , String inputFilePath){
        // init the approximator with the given algorithm
        Approximator approximator = new Approximator(algorithm);
        // init the result drawer
        IResultDrawer resultDrawer = new CombinedResultDrawer();

        Optional<BufferedImage> inputImage = Optional.empty();

        // init the image input
        try {
            inputImage = Optional.ofNullable(ImageIO.read(new File(inputFilePath)));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        if (inputImage.isEmpty()){
            System.out.println("something went wrong with the input " + inputFilePath + " shutting down the process");
            System.exit(1);
        }

        long start = System.currentTimeMillis();

        System.out.println("starting to run algorithm on " + inputFilePath);

        // the image is ready, running the approximation algorithm
        Optional<ImageApproximation> result = approximator.approximate(inputImage.get());

        if (result.isEmpty()){
            System.out.println("the algorithm failed to approximate the image with sufficient accuracy " + inputFilePath
                    + "using the algorithm " + algorithm.toString() + " shutting down the process");
            System.exit(1);
        }

        long durationInMillis = System.currentTimeMillis() - start;

        System.out.println(computationTimeAsString(durationInMillis));

        // all went good -> time to show the results
        resultDrawer.draw(result.get(), inputImage.get(), inputFilePath);
        System.out.println("the program finished it's job! the accuracy is " + (100 - result.get().getScore()));
    }

    private static void initInputsAndAlgorithms(List<String> inputFilesPath, List<IAlgorithm> algorithms) {
        inputFilesPath.add("./resources/image4.jpg");
        inputFilesPath.add("./resources/suz.jpg");
        inputFilesPath.add("./resources/Mona_Lisa_o.jpg");

        algorithms.add(new GeneticAlgorithm(0.1, 100 , true, 0.5));
        algorithms.add(new GeneticAlgorithm(0.1, 50 , true, 0.4));
        algorithms.add(new GeneticAlgorithm(0.1, 50 , true , 0.3));


        algorithms.add(new SizeDecreasingHillClimbingAlgorithm(0.0 , 0.0));
//        algorithms.add(new SizeDecreasingHillClimbingAlgorithm(0.3 , 0.6));
//        algorithms.add(new SizeDecreasingHillClimbingAlgorithm(0.2 , 0.7));
//        algorithms.add(new SizeDecreasingHillClimbingAlgorithm(0.3 , 0.7));
        algorithms.add(new SizeDecreasingHillClimbingAlgorithm(0.05 , 0.5));
        algorithms.add(new SizeDecreasingHillClimbingAlgorithm(0.05 , 0.6));
//        algorithms.add(new SimulatedAnnealing(10_0000 , 0.003));
    }

    private static String computationTimeAsString(long durationInMillis) {
        return "the computation took "
                + TimeUnit.MILLISECONDS.toMinutes(durationInMillis) + ":" + TimeUnit.MILLISECONDS.toSeconds(durationInMillis);
    }
}
