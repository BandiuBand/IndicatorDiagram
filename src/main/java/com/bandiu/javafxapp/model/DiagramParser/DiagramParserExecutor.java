package com.bandiu.javafxapp.model.DiagramParser;

import com.bandiu.javafxapp.model.ExcelWriter.ExcelWriter;
import com.bandiu.javafxapp.model.ExcelWriter.PTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiagramParserExecutor {
    private static File tamplateDirectory =new File( getTemplatesFolder());
    private String file;
    private File targetFolder;


    private Parser parser;
    private static String getTemplatesFolder(){
        
        return "./app/template/";
    }

    public static void setTemplateDirectory(File templateDirectory) {
        DiagramParserExecutor.tamplateDirectory = templateDirectory;
    }

    public DiagramParserExecutor(String file, String targetFolder){
        this.file=file;
        this.targetFolder = new File(targetFolder);

        String content = MainExcecutor.openFile(file);
        parser = new Parser(content);


    }

    public String execute(){
        String folderName = new File(file).getName().replace(".XL","");
        File target = new File(targetFolder,folderName);
        if (target.mkdir()) {
            try {
                return makeFiles(file, target.getPath()).stream().collect(Collectors.joining("\n"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "Не вдалось створити папку для файлів у:"+targetFolder.getPath();
    }

    private List<String> makeFiles(String pathToXL, String targetDirectory){

        //E:\DiagramParser\2023\07\21071413_075scaw.XL
        File file = new File(pathToXL);
        String fileName = file.getName().replace(".XL","");
        String pathTo = MainExcecutor.makePathTargetFile(targetDirectory,fileName);
        File[] tamplates = tamplateDirectory.listFiles();

        ArrayList<String> results = new ArrayList<>();
        for (File tamplate:tamplates) {
            results.add(saveToXlsx(tamplate.getPath(),pathTo,tamplate.getName().replace(".xlsx","")));

        }
        return results;
    }
    private String saveToXlsx(String pathToXlsx, String pathTo, String sufix){

        String path = pathTo.replace(".xlsx","") + sufix+".xlsx";
        ExcelWriter writer = new ExcelWriter(pathToXlsx,path,0);
        PTable table = writer.getTable();
        MainExcecutor.addDataToTable(parser,table);
        writer.save();
        return path;
    }

}
