package ui.neuralNetowrk;

import ui.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NeuralNetwork {

    public UUID id;
    public List<Layer> layers = new ArrayList<>();
    public List<Integer> networkSize = new ArrayList<>();
    public int inputSize;
    public int outputSize;
    public int size;
    public double error;

    public NeuralNetwork(String networkConfiguration, int nOfInputs, int nOfOutputs, double deviation) {
        id = UUID.randomUUID();

        networkSize.add(nOfInputs);
        for (String token : networkConfiguration.replaceAll("\\D", " ").trim().split("\\s+")) {
            if (!token.isEmpty()) {
                networkSize.add(Integer.parseInt(token));
            }
        }
        networkSize.add(nOfOutputs);

        inputSize = networkSize.get(0);
        outputSize = networkSize.get(networkSize.size() - 1);
        size = networkSize.size();

        for (int i = 0; i < size; i++) {
            Layer layer = new Layer();
            layer.index = i;
            layer.neurons = networkSize.get(i);

            if (i == 0) {
                layers.add(layer);
                continue;
            }
            if (i == size - 1) {
                layer.neurons = outputSize;
                layer.isLast = true;
            }

            layer.previousLayer = layers.get(i - 1);

            for (int j = 0; j < layer.neurons; j++) {
                List<Double> neuronWeight = new ArrayList<>(layer.previousLayer.neurons);
                for (int k = 0; k < layer.previousLayer.neurons; k++) {
                    neuronWeight.add(Utils.distribution(deviation));
                }
                layer.w.add(neuronWeight);
                layer.w0.add(Utils.distribution(deviation));
            }

            layers.add(layer);
        }
    }

    public void pass(List<List<Double>> data) {
        double totalError = 0;
        Layer outputLayer = layers.get(size - 1);
        int inputFeatureCount = 0;

        for (List<Double> row : data) {
            if (inputFeatureCount == 0) {
                inputFeatureCount = row.size() - 1;
            }
            for (Layer layer : layers) {
                if (layer.index == 0) {
                    layer.y = row.subList(0, inputFeatureCount);
                } else {
                    layer.calculation(layer.previousLayer.y);
                }
            }
            totalError += Utils.calcSquareError(row.get(row.size() - 1), outputLayer.y.get(0));
        }

        error = totalError / data.size();
    }

    public double getError() {
        return error;
    }

    @Override
    public String toString() {
        return String.valueOf(error);
    }
}
