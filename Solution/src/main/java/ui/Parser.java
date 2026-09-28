package ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {

    public static List<List<Double>> parseCSV(String fileName) throws FileNotFoundException {
        List<List<Double>> data = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().replaceAll("\\s", "");
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                List<Double> values = new ArrayList<>(parts.length);
                for (String value : parts) {
                    values.add(Double.parseDouble(value));
                }
                data.add(values);
            }
        }
        return data;
    }
}
