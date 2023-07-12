package ui;

import java.util.Random;

public class Utils {
    public static double sigm(double x) { // transitional function
        return 1 / (1 + Math.exp(-x));
    }

    public static double distribution(double deviation) { // returns normal distribution value with mean = 0 and set deviation
        Random rnd = new Random();
        return rnd.nextGaussian() * deviation;
    }

    public static double calcSquareError(double Yi, double Yj) {
        return Math.pow(Yi - Yj, 2);
    }

    public static double arithmeticMean(double x, double y) {
        return (x + y) / 2;
    }
}
