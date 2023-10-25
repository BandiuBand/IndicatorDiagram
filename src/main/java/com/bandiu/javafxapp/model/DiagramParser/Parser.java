package com.bandiu.javafxapp.model.DiagramParser;

import java.util.ArrayList;

public class Parser {
    private static boolean testMode = MainExcecutor.isTestMode();
    private String file = TestContent.getContent();
private static String regexBetweenData = ";";
private String[] lines = null;

private ArrayList<Cyl> cyls = new ArrayList<>();
private String date = null;
private String time = null;
public Parser(String inputText){
	file = inputText;
    start();
}
public Parser(){
    if(testMode)
        start();
}

private void start(){
    splitToArrayList();
    parseDataTime();
    parseAmountOfCyl();
    parsePressureCurve();
    parseIndicatedPower();
    parsePeakPressure();
}
private void splitToArrayList(){
	lines = file.split("\n");
    if (testMode) {
        System.out.println("Split lines...");
        System.out.println("Amount of lines: " + lines.length);
        System.out.println("__________________________________");


    }
}

private void parseAmountOfCyl(){
    int index = finderIndex("CYLINDERS");


        while (isEmptyLine(lines[index])) {
            index++;
            if (index > 2000)
                throw new RuntimeException("Have not number after - CYLINDERS");
        }

    int amountOfCyl = 0;
    try{
        amountOfCyl  = Integer.parseInt(lines[index]);
    }catch (Exception e){
        throw new RuntimeException("Cant parse amount of cyl from line " + lines[index]);
    }

        if (testMode){
            System.out.println("Amount of cyl is " + amountOfCyl);
        }

    for (int i = 1; i <= amountOfCyl; i++) {

        Cyl cyl = new Cyl(i);
        cyls.add(cyl);

        if (testMode){
            System.out.println("Create cyl#"+i);}
    }

    if (testMode) {
        System.out.println("Creating of cyls successful");
    }
}
public ArrayList<Cyl> getCyls (){
    return cyls;
}
private void parsePressureCurve(){
    int startIndex = finderIndex("PRESSURE CURVE");
    while (!isContainDataWidthSplitter(lines[startIndex])){
        startIndex++;
        if (startIndex>2000)
            throw new RuntimeException("Have not data or data is not correct");
    }
    if (testMode)
        System.out.println("Start parsing Pressure curve\n______________________________________________________________");
    for (int i = 0; i < 359; i++) {
        parseLinePressureCurve(lines[i+startIndex],i);
    }
    if (testMode)
        System.out.println("_____________________________________________________________\nFinish parsing");

}

private void parseLinePressureCurve(String line,int angle) {
    double[] pressures = null;
    if(testMode){
        System.out.println("line - \""+line+"\".          For angle - " + angle + "; Start the line parsing..... ");
    }
    try {
        String[] stringPressures = line.split(regexBetweenData);
        if (stringPressures.length != cyls.size())
            throw new RuntimeException("Amount values in line - \""+line+"\" is not equals amount of cyls = "+(cyls.size()));
        for (int i = 0; i < stringPressures.length; i++) {
            double pressure =Double.parseDouble(stringPressures[i]);
            cyls.get(i).setPressure(pressure,angle);
        }
        if(testMode){
            System.out.println("Finish the line parsing.");
            System.out.print("Result - ");
            for (int i = 0; i < cyls.size(); i++) {
                System.out.print(cyls.get(i).getPressure(angle)+"  ");
            }
            System.out.println("\n");
        }

    }
    catch (Exception e) {
        throw new RuntimeException(e);
    }

}

private void parseIndicatedPower(){
    int index = finderIndex("INDICATED POWER");
    while (isEmptyLine(lines[index])||!isContainDataWidthSplitter(lines[index])){
        index++;
        if (index>2000)
            throw new RuntimeException("Can`t find indicated power line");
    }

    String line = lines[index];
    if (testMode) {
        System.out.println("Start parsing indicated power. line - \"" + line+"\"");
    }

    try {
        String[] stringPressures = line.split(regexBetweenData);
        if (stringPressures.length != cyls.size())
            throw new RuntimeException("Amount values in line - \"" + line + "\" is not equals amount of cyls = " + (cyls.size()));
        for (int i = 0; i < stringPressures.length; i++) {
            int pressure = Integer.parseInt(stringPressures[i]);
            cyls.get(i).setIndicatedPower(pressure);
        }
        if (testMode) {
            System.out.println("Finish parsing.");
            System.out.print("Result - ");
            for (int i = 0; i < cyls.size(); i++) {
                System.out.print(cyls.get(i).getIndicatedPower() + "  ");
            }
            System.out.println("\n");
        }
    }catch (Exception e) {
        throw new RuntimeException(e);
    }

    }


    private void parsePeakPressure() {

        if (testMode)
            System.out.println("Start parsing peak pressure ....");

        int startIndex = finderIndex("PEAK PRESSURES");

        while (isEmptyLine(lines[startIndex])) {
            startIndex++;
            if (startIndex > 2000)
                throw new RuntimeException("Can`t find peak pressure data");
        }
        for (int i = 0; i < cyls.size(); i++) {
            String line = lines[i+startIndex];
            if (testMode)
                System.out.println("Start parsing for line - \"" + line);
            parseLinePeakPressure(line,i);
        }
        if (testMode)
            System.out.println("Finish parsing");
    }

private void parseLinePeakPressure(String line,int cyl){
    int strokes= parseStrokes();
    try {
        String[] stringPressures = line.split(regexBetweenData);
        if (stringPressures.length != strokes)
            throw new RuntimeException("Amount values in line - \""+line+"\" is not equals amount of strokes = "+strokes);
        for (int i = 0; i < stringPressures.length; i++) {
            int pressure =Integer.parseInt(stringPressures[i]);
            cyls.get(cyl).addPeakPressures(pressure);
        }
        if(testMode){
            System.out.println("Finish the line parsing.");
            System.out.print("Result - ");
            for (int i = 0; i < strokes; i++) {
                System.out.print(cyls.get(cyl).getPeakPressures().get(i)+"  ");
            }
            System.out.println("\n");
        }

    }
    catch (Exception e) {
        throw new RuntimeException(e);
    }

}

private int parseStrokes(){
    if(testMode)
        System.out.println("Start parsing strokes");
    int index = finderIndex("STROKES");
    while (isEmptyLine(lines[index])){
        index++;
        if (index>2000)
            throw new RuntimeException("Can`t find line of Strokes");
    }
    int strokes = 0;
    try {
        strokes = Integer.parseInt(lines[index]);
        if (strokes<=0)
            throw new RuntimeException("Strokes can`t be less than 1");
    } catch (Exception e)
    {
        throw e;
    }
    return strokes;
}
private void parseDataTime(){
    if (testMode)
        System.out.println("Start parsing date");
    int index = finderIndex("DATE");
    while (isEmptyLine(lines[index])) {
        index++;
        if (index>2000)
            throw new RuntimeException("Can`t find line of date");
    }
    date = lines[index];//todo
    if (testMode)
        System.out.println("The date was: " + date);

    if (testMode)
        System.out.println("Start parsing time");
     index = finderIndex("TIME");
    while (isEmptyLine(lines[index])) {
        index++;
        if (index>2000)
            throw new RuntimeException("Can`t find line of time");
    }
    time = lines[index];//todo
    if (testMode)
        System.out.println("The time was: " + time);
}
private static boolean isEmptyLine(String line)
{//todo
    return false;
}

private static boolean isContainDataWidthSplitter(String line){
    return line.contains(regexBetweenData);
}

private int finderIndex(String regex){
    for (int i = 0; i < lines.length; i++) {
        if (lines[i].startsWith(regex))
            return i+1;
    }
    throw new RuntimeException("No find - "+regex);
}

}