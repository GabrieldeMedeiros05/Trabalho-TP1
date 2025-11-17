package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class CsvParser {

    private String path;
    private String separator = ";";
    private boolean header = false;

    public List<String[]> read(String path) {
        List<String[]> registros = new ArrayList<String[]>();
        try (Scanner scanner = new Scanner(new File(path))) {

            if (scanner.hasNextLine()) {
                String header = scanner.nextLine();
                String[] headers = header.split(separator);
                if (this.header) {
                    registros.add(headers);
                }
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(separator);
                registros.add(fields);
            }

        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado: " + path);
        }
        return registros;
    }
}