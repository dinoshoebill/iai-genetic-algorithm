package ui.geneticAlgorithm;

import ui.neuralNetowrk.NeuralNetwork;
import ui.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Optimization {
    public List<List<Double>> trainData = new ArrayList<>();
    public int trainDataSize;
    public List<List<Double>> testData = new ArrayList<>();
    public int testDataSize;
    public int nOfInputs;
    public int nOfOutputs = 1;
    public double deviation = 0.01;
    public int printCycle = 2000;
    public String networkConfiguration;
    public GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();

    public void execute() {

        createInitialPopulation(); // day zero population
        for (int i = 0; i <= geneticAlgorithm.iterations; i++) { // iterate through genetic algorithm

            evaluate(trainData); // evaluate current population on train data
            geneticAlgorithm.population.sort(Comparator.comparingDouble(NeuralNetwork::getError)); // sort according to error

            if (i % printCycle == 0)
                printTrainErrorResult(i); // print train error every iteration cycle

            keepElite(); // keep elite

            List<Double> selectionLine = getSelectionLine(); // selection line
            while (geneticAlgorithm.population.size() != geneticAlgorithm.popSize) {
                NeuralNetwork parent1 = selectParent(selectionLine); // first parent
                NeuralNetwork parent2 = selectParent(selectionLine); // second parent
                NeuralNetwork child = cross(parent1, parent2); // cross parents
                mutate(child); // mutate child
                geneticAlgorithm.population.add(child); // add child to new population
            }
        }

        evaluate(testData); // evaluate on test data
        printTestErrorResult();
    }

    public List<Double> getSelectionLine() {

        double totalError = 0;
        for (NeuralNetwork neuralNetwork : geneticAlgorithm.population)
            totalError += neuralNetwork.error; // add each neural network error

        List<Double> selectionLine = new ArrayList<>(); // selection line scale for percentages
        for (NeuralNetwork neuralNetwork : geneticAlgorithm.population)
            selectionLine.add((neuralNetwork.error / totalError)); // add percentage to selection line

        selectionLine.sort(Collections.reverseOrder()); // reverse percentage order

        return selectionLine;
    }

    // creates initial population with randomly set neuron weight values
    public void createInitialPopulation() {
        for (int i = 0; i < geneticAlgorithm.popSize; i++) {
            geneticAlgorithm.population.add(new NeuralNetwork(networkConfiguration, nOfInputs, nOfOutputs, deviation));
        }
    }

    // evaluates each neural network by doing a forward pass
    public void evaluate(List<List<Double>> data) {
        geneticAlgorithm.population.stream().forEach(neuralNetwork -> neuralNetwork.pass(neuralNetwork, data));
    }

    // keeps elite citizens
    public void keepElite() {
        if (geneticAlgorithm.elitismCount == 0)
            geneticAlgorithm.population = new ArrayList<>();

        geneticAlgorithm.population = geneticAlgorithm.population.subList(0, geneticAlgorithm.elitismCount); // return population sublist
    }

    // selects child parent
    public NeuralNetwork selectParent(List<Double> selectionLine) {

        double point = Math.random(); // define random point in range [0, 1>
        double lowerBoundary = 0; // define lower boundary
        int index = 0;
        while (index < selectionLine.size()) { // for every point
            if (point >= lowerBoundary && point < lowerBoundary + selectionLine.get(index)) // if point is in range
                break;

            lowerBoundary += selectionLine.get(index); // raise lower boundary by current boundary
            index++;
        }

        return geneticAlgorithm.population.get(index); // return new parent's index
    }

    // this is where Adam and Eve made Cain
    public NeuralNetwork cross(NeuralNetwork parent1, NeuralNetwork parent2) {

        NeuralNetwork child = new NeuralNetwork(networkConfiguration, nOfInputs, nOfOutputs, deviation); // define new child

        for (int i = 1; i < child.size; i++) { // for each layer except first
            for (int j = 0; j < child.layers.get(i).neurons; j++) { // for each neuron
                for (int k = 0; k < child.layers.get(i).previousLayer.neurons; k++) {
                    child.layers.get(i).w.get(j).set(k, Utils.arithmeticMean(parent1.layers.get(i).w.get(j).get(k), parent2.layers.get(i).w.get(j).get(k))); // put arithmetic mean
                }
                child.layers.get(i).w0.set(j, Utils.arithmeticMean(parent1.layers.get(i).w0.get(j), parent2.layers.get(i).w0.get(j))); // arithmetic mean of bias
            }
        }

        return child;
    }

    // this is why Cain slew Abel
    public  void mutate(NeuralNetwork mutant) {
        for (int i = 1; i < mutant.size; i++) { // for each layer except first
            for (int j = 0; j < mutant.layers.get(i).neurons; j++) { // for each neuron
                for (int k = 0; k < mutant.layers.get(i).previousLayer.neurons; k++) { // for each input weight
                    if (Math.random() <= geneticAlgorithm.mutationProbability) { // should mutate?
                        mutant.layers.get(i).w.get(j).set(k, mutant.layers.get(i).w.get(j).get(k) + Utils.distribution(geneticAlgorithm.mutationScale)); // mutate weight
                    }
                }

                if (Math.random() <= geneticAlgorithm.mutationProbability) { // should mutate?
                    mutant.layers.get(i).w0.set(j, mutant.layers.get(i).w0.get(j) + Utils.distribution(geneticAlgorithm.mutationScale)); // mutate bias
                }
            }
        }
    }

    public void printTrainErrorResult(int iteration) {
        System.out.println("[Train error @" + iteration + "]: " + geneticAlgorithm.population.get(0).error);
    }

    public void printTestErrorResult() {
        System.out.println("[Test error]: " + geneticAlgorithm.population.get(0).error);
    }
}
