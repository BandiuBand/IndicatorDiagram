package com.bandiu.javafxapp.model.DiagramParser;

import java.io.File;
import java.util.ArrayList;
import com.bandiu.javafxapp.model.ExcelWriter.*;
public class MainExcecutor {
    private static boolean testMode = true;
    protected static final String pathToXlsx = "c:/template.xlsx";


    public static boolean isTestMode() {
        return testMode;
    }

    public static void main(String[] args) {
        System.out.println("Write path to yours file");
        String path = ConsoleReader.scan();
        System.out.println("Write path to target Directory");
        String targetDir = ConsoleReader.scan();
        System.out.println("Have maden: "+ makeFile(path,targetDir));
        ConsoleReader.close();

    }

    public static String makeFile(String pathToXL, String targetDirectory){
        String content = openFile(pathToXL);
        Parser parser = new Parser(content);
        //E:\DiagramParser\2023\07\21071413_075scaw.XL
        File file = new File(pathToXL);
        String fileName = file.getName().replace(".XL","");
        String pathTo = makePathTargetFile(targetDirectory,fileName);
        ExcelWriter writer = new ExcelWriter(pathToXlsx,pathTo,0);
        PTable table = writer.getTable();
        addDataToTable(parser,table);
        writer.save();
        return pathTo;
    }
    protected static String makePathTargetFile(String targetDirectory, String fileName){
        return targetDirectory + "/"+fileName+".xlsx";
    }
    public static void addDataToTable(Parser parser, PTable table){
        ArrayList<Cyl> cyls = parser.getCyls();

        addPressureCurve(cyls,table);

        addPressureCurveWithFilter(cyls,table);

        addCylsData(cyls,table);

        addPeakPressure(cyls,table,40);

    }

    private static void addCylsData(ArrayList<Cyl> cyls, PTable table){
        int amountOfCyl = cyls.size();
        int rowStart = 1;
        addBanner(rowStart,table);


        for (int i = 0; i < amountOfCyl; i++) {
            table.setCell(i+1+rowStart,20,i);
            addDataOfCyl(table,cyls.get(i),i+1+rowStart);
        }

    }

    private static void addDataOfCyl(PTable table, Cyl cyl, int rowStart){
        double pTDC = cyl.getPressure(180);

        double apmax = cyl.getOptimizedMaxPressureAngle();
        double pmax = cyl.getAveragePeakPressure();
        double pexp = 0;//todo
        double pscav = cyl.getP_Scav();
        double rpm = cyl.getRpm();
        double MIP = 0;//todo
        double pInd = cyl.getIndicatedPower();
        int cellIndex = 21;
        table.setCell(rowStart,cellIndex,pTDC);
        cellIndex++;
        table.setCell(rowStart,cellIndex,pmax);
        cellIndex++;
        table.setCell(rowStart,cellIndex,apmax);
        cellIndex++;
        table.setCell(rowStart,cellIndex,pexp);
        cellIndex++;
        table.setCell(rowStart,cellIndex,pscav);
        cellIndex++;
        table.setCell(rowStart,cellIndex,rpm);
        cellIndex++;
        table.setCell(rowStart,cellIndex,MIP);
        cellIndex++;
        table.setCell(rowStart,cellIndex,pInd);
    }
    private static void addBanner(int rowStart, PTable table){
        int cellIndex = 20;
        table.setCell(rowStart,cellIndex,"CYL");
        cellIndex++;
        table.setCell(rowStart,cellIndex,"pTDC");
        cellIndex++;
        table.setCell(rowStart,cellIndex,"pmax mean");
        cellIndex++;
        table.setCell(rowStart,cellIndex,"apmax");
        cellIndex++;
        table.setCell(rowStart,cellIndex,"pexp");
        cellIndex++;
        table.setCell(rowStart,cellIndex,"pscav");
        cellIndex++;
        table.setCell(rowStart,cellIndex,"rpm");
        cellIndex++;
        table.setCell(rowStart,cellIndex,"MIP");
        cellIndex++;
        table.setCell(rowStart,cellIndex,"Pind");
    }

    private static void addPressureCurve(ArrayList<Cyl> cyls, PTable table){
        int amountOfCyl = cyls.size();

        int nAngls = cyls.get(0).getPressures().length;
        double k = ((double)(nAngls+1))/((double)360);
        for (int i = 0; i < nAngls; i++) {
            double curAngl = ((double) i/k)-(180.00/k);
            table.setCell(i+1,0,curAngl);
        }
        for (int i = 0; i < nAngls; i++) {
            for (int j = 0; j < amountOfCyl; j++) {
                table.setCell(i+1,j+1,cyls.get(j).getPressure(i));
            }
        }
    }
    private static void addPeakPressure(ArrayList<Cyl> cyls, PTable table,int shift){
        int amountOfCyl = cyls.size();

        int samples = cyls.get(0).getPeakPressures().size();

        for (int i = 0; i < samples; i++) {
            table.setCell(i+1,shift,i+1);
        }
        for (int i = 0; i < samples; i++) {
            for (int j = 0; j < amountOfCyl; j++) {
                table.setCell(i+1,j+1+shift,cyls.get(j).getPeakPressures().get(i));
            }
        }
    }

    private static void addPressureCurveWithFilter(ArrayList<Cyl> cyls, PTable table){
        int amountOfCyl = cyls.size();
        addAnglesWithFilterFor(cyls.get(0),table);
        for (int i = 0; i < amountOfCyl; i++) {
            addPressureCurveWithFilterForOneCyl(cyls.get(i),table,i);
        }
    }
    private static void addAnglesWithFilterFor(Cyl cyl,PTable table){
        double[] angles = cyl.getAnglesAfterFilter();
        for (int i = 0; i < angles.length; i++) {
            table.setCell(i+1,35,angles[i]);
        }
    }
    private static void addPressureCurveWithFilterForOneCyl(Cyl cyl,PTable table,int shift){
        double[] pressures = cyl.getPressuresAfterFilter();
        for (int i = 0; i < pressures.length; i++) {
            table.setCell(i+1,36+shift,pressures[i]);
        }
    }
    protected static String openFile(String path){
        return Reader.read(path);
    }
    private static String openFile(){
        String path = ConsoleReader.scan();
        path = checkPath(path);
        ConsoleReader.close();
        return Reader.read(path);
    }
    private static String checkPath(String path){
        File file = new File(path);
        if (file.isDirectory()){
            System.out.println("This is directory, please write name of file");
            String fileName = ConsoleReader.scan();
            path = path + "\\" + fileName;
            path = checkPath(path);
        } else if (!path.endsWith(".XL"))
            path = path + ".XL";

            if(!file.exists()||!file.canRead())
            throw new RuntimeException("Can`t read file: "+path);
        return path;
    }
}