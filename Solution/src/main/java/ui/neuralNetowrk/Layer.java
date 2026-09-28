package ui.neuralNetowrk;

import ui.Utils;

import java.util.ArrayList;
import java.util.List;

public class Layer {

    public int index;
    public int neurons;
    public Layer previousLayer;
    public List<List<Double>> w = new ArrayList<>();
    public List<Double> w0 = new ArrayList<>();
    public List<Double> y = new ArrayList<>();
    public boolean isLast = false;

    public void calculation(List<Double> input) {
        List<Double> neuronResult = new ArrayList<>(neurons);
        for (int i = 0; i < neurons; i++) {
            double result = w0.get(i);
            List<Double> weights = w.get(i);
            for (int j = 0; j < previousLayer.neurons; j++) {
                result += weights.get(j) * input.get(j);
            }
            neuronResult.add(result);
        }

        if (isLast) {
            y = neuronResult;
        } else {
            y = applySigmoid(neuronResult);
        }
    }

    private List<Double> applySigmoid(List<Double> input) {
        List<Double> result = new ArrayList<>(input.size());
        for (double x : input) {
            result.add(Utils.sigm(x));
        }
        return result;
    }

    @Override
    public String toString() {
        return "[INDEX: " + index + ", w: " + w + ", w0: " + w0 + "]";
    }
}
