package ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Parser {

    public static List<List<Double>> parseCSV(String fileName) throws FileNotFoundException {

        Scanner scanner = new Scanner(new File(fileName));

        List<List<Double>> data = new ArrayList<>();

        if(scanner.hasNextLine()) {
            scanner.nextLine();
        }

        scanner.useDelimiter("[,\n]");

        while(scanner.hasNextLine()) {
            String s = scanner.nextLine().replaceAll("\\s", "");

            List<Double> values = new ArrayList<>();

            for(String value : s.split(",")) {
                values.add(Double.parseDouble(value));
            }

            data.add(values);
        }

        scanner.close();

        return data;
    }
}
