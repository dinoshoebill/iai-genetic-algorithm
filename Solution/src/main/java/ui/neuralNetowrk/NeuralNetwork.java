package ui.neuralNetowrk;

import ui.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class NeuralNetwork {
    public UUID id; // unique ID
    public List<Layer> layers = new ArrayList<>(); // layer list containing neurons
    public List<Integer> networkSize = new ArrayList<>(); // neuron count for each layer, maybe redundant? Class layers has int neuronCount variable
    public int inputSize;
    public int outputSize;
    public int size; // total number of layers
    public double error; // calculated error

    public NeuralNetwork(String networkConfiguration, int nOfInputs, int nOfOutputs, double deviation) {
        id = UUID.randomUUID(); // assign unique ID

        networkSize.add(nOfInputs); // set number of inputs for input layer
        Arrays.stream(networkConfiguration
                .replaceAll("\\D", "")
                .split(" ")).forEach((value) -> networkSize.add(Integer.parseInt(value)));  // add hidden layer sizes
        networkSize.add(nOfOutputs); // add output layer output size

        inputSize = networkSize.get(0); // set first layer input size
        outputSize = networkSize.get(networkSize.size() - 1); // set last layer output size
        size = networkSize.size(); // set network size

        for (int i = 0; i < size; i++) { // for each layer
            Layer layer = new Layer(); // define layer
            layer.index = i; // set layer index
            layer.neurons = networkSize.get(i); // set neuron count

            if (i == 0) { // first layer doesn't have weights or previous layer
                layers.add(layer);
                continue;
            } else if (i == size - 1) { // put lastLayer flag to true
                layer.neurons = outputSize;
                layer.isLast = true;
            }

            layer.previousLayer = layers.get(i - 1); // set parent layer

            for (int j = 0; j < layer.neurons; j++) { // for each neuron in current layer
                List<Double> neuronWeight = new ArrayList<>();
                for (int k = 0; k < layer.previousLayer.neurons; k++) // for each input
                    neuronWeight.add(Utils.distribution(deviation)); // set initial weight

                layer.w.add(neuronWeight); // add weights
                layer.w0.add(Utils.distribution(deviation)); // set initial bias
            }

            layers.add(layer); // add layer
        }
    }

    public void pass(NeuralNetwork neuralNetwork, List<List<Double>> data) {

        double error = 0;
        for (List<Double> input : data) { // for each entry
            for (Layer layer : neuralNetwork.layers) // for each layer
                if (layer.index == 0)
                    layer.y = input.subList(0, input.size() - 1); // set input for input layer
                else
                    layer.calculation(layer.previousLayer.y); // do calculation

            error += (Utils.calcSquareError(input.get(input.size() - 1), neuralNetwork.layers.get(neuralNetwork.size - 1).y.get(0)));
        }

        neuralNetwork.error = error / data.size();
    }

    public double getError() {
        return this.error;
    }

    @Override
    public String toString() {
        return String.valueOf(error);
    }
}
