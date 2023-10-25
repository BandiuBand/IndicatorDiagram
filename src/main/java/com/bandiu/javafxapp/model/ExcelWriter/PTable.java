package com.bandiu.javafxapp.model.ExcelWriter;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class PTable {
    public PTable() {

    }

    public void setCell(int rowIndex, int cellIndex, String value) {
        TableRowBandiu.TableCellBandiu cell = getCell(rowIndex, cellIndex);
        cell.setValue(value);
    }

    public void setCell(int rowIndex, int cellIndex, double value) {
        TableRowBandiu.TableCellBandiu cell = getCell(rowIndex, cellIndex);
        cell.setValue(value);
    }

    private TableRowBandiu.TableCellBandiu getCell(int rowIndex, int cellIndex) {
        TableRowBandiu row = getRow(rowIndex);
        TableRowBandiu.TableCellBandiu cell = row.getCell(cellIndex);
        return cell;
    }

    public TableRowBandiu getRow(int index) {
        if (rows.containsKey(index))
            return rows.get(index);
        else {
            rows.put(index, new TableRowBandiu());
            return rows.get(index);
        }
    }





    protected Map<Integer,TableRowBandiu> rows = new TreeMap<>();

    public Iterator<Map.Entry<Integer,TableRowBandiu>> getRowIterator(){

        return rows.entrySet().iterator();
    }



    protected class TableRowBandiu {
        protected Map<Integer, TableCellBandiu> cells = new TreeMap<>();

        protected Iterator<Map.Entry<Integer, TableCellBandiu>> getCellIterator(){
            return cells.entrySet().iterator();
        }

        public class TableCellBandiu {
            private boolean isDouble = false;
            private double doubleValue = 0;
            private String stringValue = null;
            public TableCellBandiu(String value){
                isDouble = false;
                stringValue = value;

            }


            public TableCellBandiu(double value){
                isDouble = true;
                doubleValue = value;
            }
            public TableCellBandiu(){}

            public void setValue(String value)
            {
                isDouble = false;
                stringValue = value;
            }

            public void setValue(double value)
            {
                isDouble = true;
                doubleValue = value;
            }
            public Object getValue(){
                if (isDouble){
                    return doubleValue;
                }
                return stringValue;
            }

            public boolean getIsDouble(){
                return isDouble;
            }
        }
        public TableCellBandiu getCell(int index){
            if(cells.containsKey(index))
                return cells.get(index);
            else{
                cells.put(index,new TableCellBandiu());
                return cells.get(index);
            }
        }
    }
}
