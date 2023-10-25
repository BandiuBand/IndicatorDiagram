package com.bandiu.javafxapp.model;

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
}