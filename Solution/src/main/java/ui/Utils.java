package ui;

import java.util.concurrent.ThreadLocalRandom;

public final class Utils {

    private Utils() {
    }

    public static double sigm(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public static double distribution(double deviation) {
        return ThreadLocalRandom.current().nextGaussian() * deviation;
    }

    public static double calcSquareError(double expected, double actual) {
        double diff = expected - actual;
        return diff * diff;
    }

    public static double arithmeticMean(double x, double y) {
        return (x + y) * 0.5;
    }
}
