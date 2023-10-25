package com.bandiu.javafxapp.model.DiagramParser;

import java.util.Scanner;

import static java.lang.System.*;

public class ConsoleReader {
    private static Scanner scanner = null;
    private static ConsoleReader consoleReader = new ConsoleReader();
    public static String scan() {



        String userInput = scanner.nextLine();




        return userInput;
    }
    public static void close(){
        scanner.close();
    }
    private ConsoleReader(){
        scanner = new Scanner(in);
    }
}
