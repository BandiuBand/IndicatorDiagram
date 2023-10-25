package com.bandiu.javafxapp.model.DiagramParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
    private static boolean testMode = MainExcecutor.isTestMode();
    public static String read(String fileName) {

        String fileContent = null;
        if (testMode)
            System.out.println("Open file: " + fileName);
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            if (testMode)
                System.out.println("Reading content...");
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n"); // Додати роздільник для кожного рядка (перенос рядка)
            }

            fileContent = stringBuilder.toString();
            if (testMode){
            System.out.println("Content:");
            System.out.println(fileContent);}
        } catch (IOException e) {
            System.err.println("Read file error: " + e.getMessage());
        }
        return fileContent;
    }

}
