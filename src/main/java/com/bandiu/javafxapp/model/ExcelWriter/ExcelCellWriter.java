package com.bandiu.javafxapp.model.ExcelWriter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class ExcelCellWriter {
    private Workbook workbook = null;
    private Sheet sheet = null;
    public ExcelCellWriter(String path,int sheetIndex) throws IOException {
        // Відкрити існуючий Excel-документ
        FileInputStream fis = new FileInputStream(path);

             Workbook workbookNative = new XSSFWorkbook(fis);
            workbook = workbookNative;
            sheet = workbook.getSheetAt(sheetIndex); // 0 - це індекс аркуша



    }
    protected void changeSheet(int sheetIndex)
    {
        sheet = workbook.getSheetAt(sheetIndex);
    }

    public void cellWrite(int rowIndex,int cellIndex,String value){



        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }


        Cell cell = row.createCell(cellIndex);

        if (value!=null) {
            cell.setCellValue(value);
        }
    }
    public void cellWrite(int rowIndex,int cellIndex,double value){



        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }


        Cell cell = row.createCell(cellIndex);

             cell.setCellValue(value);
    }
    public void tableWrite(PTable table){
        Iterator<Map.Entry<Integer, PTable.TableRowBandiu>> iterator = table.getRowIterator();
        while (iterator.hasNext())        {
            Map.Entry<Integer, PTable.TableRowBandiu> entry = iterator.next();
            int rowIndex = entry.getKey();
            rowWrite(rowIndex,table);
        }
    }
    public void rowWrite(int index, PTable table){

        PTable.TableRowBandiu rowFrom = table.getRow(index);
        Row rowTo = sheet.getRow(index);
        if (rowTo==null){
            rowTo = sheet.createRow(index);
        }

        Iterator<Map.Entry<Integer, PTable.TableRowBandiu.TableCellBandiu>> iterator = rowFrom.getCellIterator();

        while (iterator.hasNext()){
            Map.Entry<Integer, PTable.TableRowBandiu.TableCellBandiu> entry = iterator.next();
            int cellIndex = entry.getKey();
            PTable.TableRowBandiu.TableCellBandiu cell = entry.getValue();
            if (cell.getIsDouble()){
                Cell cellTo = rowTo.createCell(cellIndex);
                cellTo.setCellValue((double)cell.getValue());
            }else {
                Cell cellTo = rowTo.createCell(cellIndex);
                cellTo.setCellValue((String)cell.getValue());
            }

        }

    }

    public boolean save(String path){
        try (FileOutputStream fos = new FileOutputStream(path)) {
            workbook.write(fos);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
