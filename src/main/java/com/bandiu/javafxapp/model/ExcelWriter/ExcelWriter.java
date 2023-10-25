package com.bandiu.javafxapp.model.ExcelWriter;

import java.io.IOException;

public class ExcelWriter {
    private String pathFrom;
    private String pathTo;
    protected PTable table = new PTable();
    private int sheetIndex = 0;
    public ExcelWriter(String pathFileOpen,String pathFileWrite,int sheetIndex){
        pathFrom=pathFileOpen;
        pathTo=pathFileWrite;
        this.sheetIndex = sheetIndex;
    }
    public PTable getTable(){
        return table;
    }

    public void save(){
        try {
            ExcelCellWriter cellsWriter = new ExcelCellWriter(pathFrom,sheetIndex);
            cellsWriter.tableWrite(table);
            writeFile(cellsWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void save(String path){
        pathTo = path;
        try {
            ExcelCellWriter cellsWriter = new ExcelCellWriter(pathFrom,sheetIndex);
            cellsWriter.tableWrite(table);
            writeFile(cellsWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void writeFile(ExcelCellWriter writer){
        writer.save(pathTo);
    }
}
