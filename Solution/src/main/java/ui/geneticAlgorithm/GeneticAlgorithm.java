package ui.geneticAlgorithm;

import ui.neuralNetowrk.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithm {

    public int popSize;
    public int elitismCount;
    public double mutationProbability;
    public double mutationScale;
    public int iterations;
    public List<NeuralNetwork> population = new ArrayList<>();
}
