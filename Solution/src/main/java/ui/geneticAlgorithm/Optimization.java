package ui.geneticAlgorithm;

import ui.Utils;
import ui.neuralNetowrk.Layer;
import ui.neuralNetowrk.NeuralNetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Optimization {

    public List<List<Double>> trainData = new ArrayList<>();
    public List<List<Double>> testData = new ArrayList<>();
    public int nOfInputs;
    public int nOfOutputs = 1;
    public double deviation = 0.01;
    public int printCycle = 2000;
    public String networkConfiguration;
    public GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();

    public void execute() {
        createInitialPopulation();
        for (int i = 0; i <= geneticAlgorithm.iterations; i++) {
            evaluate(trainData);
            geneticAlgorithm.population.sort(Comparator.comparingDouble(NeuralNetwork::getError));

            if (i % printCycle == 0) {
                printTrainErrorResult(i);
            }

            keepElite();

            List<Double> selectionLine = getSelectionLine();
            while (geneticAlgorithm.population.size() != geneticAlgorithm.popSize) {
                NeuralNetwork parent1 = selectParent(selectionLine);
                NeuralNetwork parent2 = selectParent(selectionLine);
                NeuralNetwork child = cross(parent1, parent2);
                mutate(child);
                geneticAlgorithm.population.add(child);
            }
        }

        evaluate(testData);
        printTestErrorResult();
    }

    public List<Double> getSelectionLine() {
        double totalError = 0;
        for (NeuralNetwork neuralNetwork : geneticAlgorithm.population) {
            totalError += neuralNetwork.error;
        }

        List<Double> selectionLine = new ArrayList<>(geneticAlgorithm.population.size());
        for (NeuralNetwork neuralNetwork : geneticAlgorithm.population) {
            selectionLine.add(neuralNetwork.error / totalError);
        }

        selectionLine.sort(Collections.reverseOrder());
        return selectionLine;
    }

    public void createInitialPopulation() {
        for (int i = 0; i < geneticAlgorithm.popSize; i++) {
            geneticAlgorithm.population.add(
                    new NeuralNetwork(networkConfiguration, nOfInputs, nOfOutputs, deviation));
        }
    }

    public void evaluate(List<List<Double>> data) {
        for (NeuralNetwork neuralNetwork : geneticAlgorithm.population) {
            neuralNetwork.pass(data);
        }
    }

    public void keepElite() {
        if (geneticAlgorithm.elitismCount == 0) {
            geneticAlgorithm.population = new ArrayList<>();
            return;
        }
        geneticAlgorithm.population = new ArrayList<>(
                geneticAlgorithm.population.subList(0, geneticAlgorithm.elitismCount));
    }

    public NeuralNetwork selectParent(List<Double> selectionLine) {
        double point = ThreadLocalRandom.current().nextDouble();
        double lowerBoundary = 0;
        int index = 0;
        while (index < selectionLine.size()) {
            double segment = selectionLine.get(index);
            if (point >= lowerBoundary && point < lowerBoundary + segment) {
                break;
            }
            lowerBoundary += segment;
            index++;
        }
        return geneticAlgorithm.population.get(index);
    }

    public NeuralNetwork cross(NeuralNetwork parent1, NeuralNetwork parent2) {
        NeuralNetwork child = new NeuralNetwork(networkConfiguration, nOfInputs, nOfOutputs, deviation);

        for (int i = 1; i < child.size; i++) {
            Layer layer = child.layers.get(i);
            Layer parentLayer1 = parent1.layers.get(i);
            Layer parentLayer2 = parent2.layers.get(i);
            for (int j = 0; j < layer.neurons; j++) {
                for (int k = 0; k < layer.previousLayer.neurons; k++) {
                    layer.w.get(j).set(k, Utils.arithmeticMean(
                            parentLayer1.w.get(j).get(k),
                            parentLayer2.w.get(j).get(k)));
                }
                layer.w0.set(j, Utils.arithmeticMean(parentLayer1.w0.get(j), parentLayer2.w0.get(j)));
            }
        }

        return child;
    }

    public void mutate(NeuralNetwork mutant) {
        double probability = geneticAlgorithm.mutationProbability;
        double scale = geneticAlgorithm.mutationScale;
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 1; i < mutant.size; i++) {
            Layer layer = mutant.layers.get(i);
            for (int j = 0; j < layer.neurons; j++) {
                for (int k = 0; k < layer.previousLayer.neurons; k++) {
                    if (random.nextDouble() <= probability) {
                        layer.w.get(j).set(k, layer.w.get(j).get(k) + Utils.distribution(scale));
                    }
                }
                if (random.nextDouble() <= probability) {
                    layer.w0.set(j, layer.w0.get(j) + Utils.distribution(scale));
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
