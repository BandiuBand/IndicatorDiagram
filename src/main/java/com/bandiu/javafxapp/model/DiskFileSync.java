package com.bandiu.javafxapp.model;


import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.WString;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DiskFileSync {
    public static boolean FOLDER_MODE = false;
    public interface Kernel32 extends Library {
        Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class);

        int GetVolumeInformationW(WString lpRootPathName, char[] lpVolumeNameBuffer, int nVolumeNameSize, int[] lpVolumeSerialNumber, int[] lpMaximumComponentLength, int[] lpFileSystemFlags, char[] lpFileSystemNameBuffer, int nFileSystemNameSize);
    }

    private static final String TARGET_DEVICE_NAME = "PREMET_C";
    private static String RECEIVED_FOLDER = "src/main/resources/Database/";
    private static final String REPORT_FILE = "sync_report.txt";
    private static String TARGET_FOLDER = null;
     private ArrayList<File> newFiles = null;

    public static void main(String[] args) {
        DiskFileSync diskFileSync = new DiskFileSync();

    }

    public static void setReceivedFolder(String receivedFolder) {
        RECEIVED_FOLDER = receivedFolder;
    }

    private static String getReportPath (){
        return new File(RECEIVED_FOLDER,REPORT_FILE).toString();
    }

    public static void setFolderMode(boolean folderMode) {
        FOLDER_MODE = folderMode;
    }

    public static void setTargetFolder(String targetFolder) {
        TARGET_FOLDER = targetFolder;
    }

    public DiskFileSync (){
        newFiles = new ArrayList<>();

    }
    public boolean start(){
        return findAndSyncDevice();
    }
    public ArrayList<File> getNewFiles(){
        return newFiles;
    }
    private boolean findAndSyncDevice() {
        if (FOLDER_MODE){
            syncFiles(new File(TARGET_FOLDER),RECEIVED_FOLDER);
            return true;
        }
        else {
            File[] roots = File.listRoots();
            for (File root : roots) {
                String label = getDiskLabel(root.getAbsolutePath());
                if (label.equals(TARGET_DEVICE_NAME)) {
                    syncFiles(root, RECEIVED_FOLDER);
                    System.out.println(root.getAbsolutePath());
                    //syncFiles(new File("E:\\DiagramParser\\2023"),RECEIVED_FOLDER);
                    return true;
                }
            }
            System.out.println("Device not found.");
            return false;
        }
    }

    public static String getDiskLabel(String rootPath) {
        char[] volumeNameBuffer = new char[256];
        Kernel32 kernel32 = Kernel32.INSTANCE;

        if (kernel32.GetVolumeInformationW(new WString(rootPath), volumeNameBuffer, volumeNameBuffer.length, null, null, null, null, 0) != 0) {
            return new String(volumeNameBuffer).trim();
        } else {
            return "";
        }
    }

    public void syncFiles(File source, String dest) {
        StringBuilder report = new StringBuilder();

        File[] files = source.listFiles();
        if (files == null) return;

        for (File file : files) {
            File destFile = new File(dest, file.getName());

            if (file.isDirectory()&&!file.getName().equals("System Volume Information")) {
                if (!destFile.exists()) {
                    destFile.mkdir();
                }
                syncFiles(file, destFile.getPath());
            } else {
                if (!destFile.exists()&&file.getName().endsWith(".XL")) {
                    try {
                        Files.copy(file.toPath(), destFile.toPath());
                        newFiles.add(destFile);
                        report.append("Copied: ").append("\n").append(destFile.getPath()).append("\n");
                        LocalDate currentDate = LocalDate.now();
                        LocalTime currentTime = LocalTime.now();
                        report.append(currentDate).append("\n").append(currentTime).append("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(getReportPath()), StandardOpenOption.CREATE, StandardOpenOption.DELETE_ON_CLOSE)) {
            writer.write(report.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
