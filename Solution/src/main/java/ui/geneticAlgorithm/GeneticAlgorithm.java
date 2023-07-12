package ui.geneticAlgorithm;

import ui.neuralNetowrk.NeuralNetwork;

import java.util.*;

public class GeneticAlgorithm {
    public int popSize; // population in each iteration
    public int elitismCount; // number of elite
    public double mutationProbability; // probability to mutate each weight
    public double mutationScale; // scale of weight mutation
    public int iterations; // number of algorithm iterations
    public List<NeuralNetwork> population = new ArrayList<>(); // respective population
}