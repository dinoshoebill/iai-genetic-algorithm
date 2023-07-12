package ui.neuralNetowrk;

import ui.Utils;

import java.util.ArrayList;
import java.util.List;

public class Layer {
    public int index; // index of layer in neural network
    public int neurons; // number of neurons in layer
    public Layer previousLayer; // previous layer
    public List<List<Double>> w = new ArrayList<>(); // input neuron weights
    public List<Double> w0 = new ArrayList<>(); // bias
    public List<Double> y = new ArrayList<>();
    public boolean isLast = false; // isLast flag for output neuron

    public Layer() {}

    public void calculation(List<Double> input) {

        List<Double> neuronResult = new ArrayList<>();
        for (int i = 0; i < neurons; i++) { // for each neuron
            double result = 0;
            for(int j = 0; j < previousLayer.neurons; j++) { // for each neuron weight
                result += w.get(i).get(j) * input.get(j); // Wi * Xi
            }

            result += w0.get(i); // add final weight
            neuronResult.add(result); // add neuron result
        }

        // if layer is last
        if(isLast)
            y = neuronResult; // return neuronResult as output
        else
            y = transitionalCalculation(neuronResult); // otherwise, do transitional function
    }

    public List<Double> transitionalCalculation(List<Double> input) {
        List<Double> result = new ArrayList<>();
        input.stream().forEach(x -> result.add(Utils.sigm(x)));
        return result;
    }

    @Override
    public String toString() {
        return "[INDEX: " + index + ", w: " + w + ", w0: " + w0 + "]";
    }
}
