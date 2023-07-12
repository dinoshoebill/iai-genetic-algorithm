package ui;

import ui.geneticAlgorithm.Optimization;

public class Solution {

    public static void main(String[] args) {

        try {

            Optimization optimization = new Optimization();

            int i = 0;
            do {
                switch (args[i].toLowerCase()) {
                    case "--train" -> {
                        optimization.trainData = Parser.parseCSV(args[++i]);
                        optimization.trainDataSize = optimization.trainData.size();
                    }
                    case "--test" -> {
                        optimization.testData = Parser.parseCSV(args[++i]);
                        optimization.testDataSize = optimization.testData.size();
                    }
                    case "--nn" -> optimization.networkConfiguration = args[++i];
                    case "--popsize" -> optimization.geneticAlgorithm.popSize = Integer.parseInt(args[++i]);
                    case "--elitism" -> optimization.geneticAlgorithm.elitismCount = Integer.parseInt(args[++i]);
                    case "--p" -> optimization.geneticAlgorithm.mutationProbability = Double.parseDouble(args[++i]);
                    case "--k" -> optimization.geneticAlgorithm.mutationScale = Double.parseDouble(args[++i]);
                    case "--iter" -> optimization.geneticAlgorithm.iterations = Integer.parseInt(args[++i]);
                    case "--in" -> optimization.nOfInputs = Integer.parseInt(args[++i]);
                }

                i++;
            } while (i < args.length);

            optimization.execute();
        } catch (Exception e) {
            System.out.print(e.getMessage());
            System.exit(1);
        }
    }
}