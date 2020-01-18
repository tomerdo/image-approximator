package tomerdo.dss.approximation;

import tomerdo.dss.shapes.Ellipse;
import tomerdo.dss.shapes.SimpleShape;
import tomerdo.dss.utils.ImageUtils;
import tomerdo.dss.utils.RandomUtils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static tomerdo.dss.utils.RandomUtils.drawRandom;

public class ImageApproximation {
    private List<SimpleShape> simpleShapeList;
    private double score;
    Random random =  new Random(1971603567);


    public ImageApproximation(){
        simpleShapeList = new ArrayList<>();
        score = Double.MAX_VALUE;
    }

    public ImageApproximation(List<SimpleShape> simpleShapes){
        simpleShapeList = simpleShapes;
        score = Double.MAX_VALUE;
    }

    public ImageApproximation(ImageApproximation otherImageApproximation){
        setScore(otherImageApproximation.getScore());
        otherImageApproximation.getSimpleShapeList();
        ArrayList<SimpleShape> newList = new ArrayList<>(otherImageApproximation.getSimpleShapeList().size());
        otherImageApproximation.getSimpleShapeList().forEach(shape ->
                newList.add(new Ellipse(shape)));
        setSimpleShapeList(newList);
    }

    public ImageApproximation(List<SimpleShape> result, double batchBestLoss) {
        this(result);
        setScore(batchBestLoss);
    }

    public List<SimpleShape> getSimpleShapeList() {
        return simpleShapeList;
    }

    public void addShape(SimpleShape shape){
        simpleShapeList.add(shape);
    }

    public SimpleShape getLastAddedShape(){
        return simpleShapeList.get(simpleShapeList.size()-1);
    }

    public void replaceLastShape(SimpleShape newShape){
        simpleShapeList.set(simpleShapeList.size()-1,newShape);
    }

    // getter and setters
    public ImageApproximation setSimpleShapeList(List<SimpleShape> simpleShapeList) {
        this.simpleShapeList = simpleShapeList;
        return this;
    }


    public double getScore() {
        return score;
    }

    public ImageApproximation setScore(double score) {
        this.score = score;
        return this;
    }

    // Used for genetic algorithm implementation
    public double computeFitness(BufferedImage image) {
        double fitness = ImageUtils.calculateLoss(image , this);
        setScore(fitness);
        return fitness;
    }
//
//    public ImageApproximation mutate(){
//        // random index
//        int randomIndex = drawRandom(simpleShapeList.size());
//        SimpleShape simpleShape = simpleShapeList.get(randomIndex);
//
//        // random changes
//        SimpleShape mutatedShape = randomMutate(simpleShape , 0.0 , 1.0);
//
//        List<SimpleShape> mutatedShapeList = new ArrayList<>(simpleShapeList);
//        mutatedShapeList.set(randomIndex , mutatedShape);
//        return new ImageApproximation(mutatedShapeList);
//    }
//
//    public SimpleShape randomMutate(SimpleShape simpleShape, double minSize, double maxSize) {
//        SimpleShape mutatedShape = new SimpleShape(simpleShape);
//
//        int largeNumber = RandomUtils.drawLargeInt();
//        int choice = largeNumber % 4;
//        switch (choice){
//            case 1:
//                // mutate color
//                mutatedShape.mutateColor();
//                break;
//            case 2:
//                // mutate size
//                mutatedShape.mutateSize(minSize, maxSize);
//                break;
//            case 3:
//                // mutate orientation
//                mutatedShape.mutateOrientation();
//                break;
//            default: // case 0 - mutate position
//                mutatedShape.mutatePosition();
//        }
//        return mutatedShape;
//    }


}
