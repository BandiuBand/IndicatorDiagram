package com.bandiu.javafxapp.model.ExcelWriter;

import java.io.IOException;

public class MainExcelWriter {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");
        ExcelWriter writer = new ExcelWriter("c:/1.xlsx","c:/proba.xlsx",0);
        PTable table = writer.getTable();
        table.setCell(0,0,"pizda");
        table.setCell(0,1,0.00089);
        writer.save();
    }
}