package com.bandiu.javafxapp.model;

import com.bandiu.javafxapp.model.DiagramParser.Cyl;
import com.bandiu.javafxapp.model.DiagramParser.DiagramParserExecutor;
import com.bandiu.javafxapp.model.DiagramParser.Parser;

import java.util.ArrayList;

public class FileItem {
    private boolean isNew;
    private String dbPath;

    public FileItem(boolean isNew, String dbPath) {
        this.isNew = isNew;
        this.dbPath = dbPath;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getDbPath() {
        return dbPath;
    }

    public void setDbPath(String dbPath) {
        this.dbPath = dbPath;
    }

    public String getLabel() {

        return isNew ? "Новий: " + dbPath : dbPath;
    }
    private DiagramParserExecutor parserExcicutor = null;
    private ArrayList<Cyl> cyls = null;
    private Parser parser = null;
    private ChartData[] chartDatas = new ChartData[6];

    private void parseData (){
        parserExcicutor = new DiagramParserExecutor(dbPath);
        parser = parserExcicutor.getParser();
        cyls = parser.getCyls();
    }
}