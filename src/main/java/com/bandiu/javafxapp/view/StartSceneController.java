package com.bandiu.javafxapp.view;

import com.bandiu.javafxapp.model.DiskFileSync;
import com.bandiu.javafxapp.model.FileItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import com.bandiu.javafxapp.model.DiagramParser.DiagramParserExecutor;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StartSceneController {

    private static String DATABASE_PATH = "/src/main/java/resources/Database";
    private static String TAMPLATE_PATH = "/src/main/java/resources/tamplates";
    @FXML
    public ListView<String> listOfChoisenFiles;
    @FXML
    public Label selectedMeasurementsLabel;

    @FXML
    public Button resiveData, saveToExcelChoisen, addFileToList,addToListFromDB, deleteFromList,chooseDBfolder,chooseTamplatefolder;

    private List<FileItem> fileItems = null;
    @FXML
    public void initialize() {
        try {
            initializeDatabase();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        fileItems = new ArrayList<>();
        listenerOfChoiceListElement();

    }
    private void initializeDatabase() throws URISyntaxException {

        TAMPLATE_PATH = getParentFolderPath("tamplate");
        DiagramParserExecutor.setTemplateDirectory(new File(TAMPLATE_PATH));

        DATABASE_PATH = getParentFolderPath("Database");
        DiskFileSync.setReceivedFolder(DATABASE_PATH);
        DiskFileSync.setTargetFolder("E:\\DiagramParser\\2023");
        DiskFileSync.setFolderMode(true);
    }
    private String getParentFolderPath(String name) throws URISyntaxException{
        File jarLocation = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        File parentDirectory = jarLocation.getParentFile();
        File folder = new File(parentDirectory, name);
        if (!folder.exists()) {

            if (!folder.mkdir()) {
                System.err.println("Не вдалося створити папку.");
            }
        }
        return  folder.getPath();
    }

    public List<FileItem> getFileItems(){
        return fileItems;
    }

    @FXML
    public void handleLoadData() {

        Alert waitingAlert = new Alert(Alert.AlertType.INFORMATION);
        waitingAlert.setTitle("Синхронізація");
        waitingAlert.setHeaderText(null);
        waitingAlert.setContentText("Зачекайте...");
        waitingAlert.show();

        DiskFileSync diskFileSync = new DiskFileSync();
        boolean success = diskFileSync.start();
        waitingAlert.close();
        if(success) {

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Синхронізація");
            successAlert.setHeaderText("Синхронізація успішна");


            List<File> newFiles = diskFileSync.getNewFiles();

            for (File file : newFiles) {
                fileItems.add(new FileItem(true, file.getPath()));
            }
            String filesList = String.join("\n", newFiles.stream().map(File::getName).collect(Collectors.toList()));
            successAlert.setContentText("Список доданих файлів:\n" + filesList);

            successAlert.showAndWait();
            listRefresh();
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Синхронізація");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Не вдалось зв'язатись з пристроєм");
            errorAlert.showAndWait();
        }

    }

    @FXML
    public void handleChooseDBfolder() {
        String folder = folderChooser();
        if (folder!=null){
            DATABASE_PATH = folder;
        }
    }
    @FXML
    public void handleСhooseTamplatefolder() {
        String folder = folderChooser();
        if (folder!=null){
            DiagramParserExecutor.setTemplateDirectory(new File(folder));
        }
    }

    @FXML
    public void handleSaveAsExcel() {
        // Реалізація зберігання вибраних вимірювань у файл Excel
        String selectedLabel = listOfChoisenFiles.getSelectionModel().getSelectedItem();
        FileItem itemToSave = null;
        if (selectedLabel != null) {

            for (FileItem fileItem : fileItems) {
                if (fileItem.getLabel().equals(selectedLabel)) {
                    itemToSave = fileItem;
                    break;
                }
            }

        }
        String targetFolder = folderChooser();
        String returnMassage = null;
        if (itemToSave!=null&&targetFolder!=null){
            try {
                returnMassage = saveAsExcelToFolder(itemToSave.getDbPath(),targetFolder);
            } catch (Exception e){
                System.out.println("Помилка при збереженні:"+e.getMessage());
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Помилка");
                alert.setHeaderText(null);
                alert.setContentText("Помилка при збереженні:"+e.getMessage());
                alert.showAndWait();
            }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Збереження завершено");
        alert.setHeaderText(null);
        alert.setContentText("Були створені такі файли:"+returnMassage);
        alert.showAndWait();

    }
    private String folderChooser(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Виберіть папку");


        File selectedFile = directoryChooser.showDialog(null);
        return selectedFile.getPath();
    }
    private String saveAsExcelToFolder(String filePath,String folder){
        DiagramParserExecutor diagramParser = new DiagramParserExecutor(filePath,folder);
        //DiagramParserExecutor.setTemplateDirectory(new File(TAMPLATE_PATH));
        return diagramParser.execute();
    }

    @FXML
    public void handleAddFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Вибрати файл");

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XL Files", "*.XL"));


        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {

            fileItems.add(new FileItem(false, selectedFile.getPath()));


            listRefresh();
        } else {
            System.out.println("Вибір файлу скасовано");
        }
    }

    @FXML
    public void handleAddFromDatabase() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Вибрати файл з бази даних");
        fileChooser.setInitialDirectory(new File(DATABASE_PATH));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XL Files", "*.XL"));


        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {

            fileItems.add(new FileItem(false, selectedFile.getPath()));


            listRefresh();
        } else {
            System.out.println("Вибір файлу скасовано");
        }
    }

    @FXML
    public void handleRemoveFromList() {

        String selectedLabel = listOfChoisenFiles.getSelectionModel().getSelectedItem();

        if (selectedLabel == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Помилка видалення");
            alert.setHeaderText(null);
            alert.setContentText("Будь ласка, оберіть файл для видалення.");
            alert.showAndWait();
            return;
        }


        FileItem itemToRemove = null;
        for (FileItem fileItem : fileItems) {
            if (fileItem.getLabel().equals(selectedLabel)) {
                itemToRemove = fileItem;
                break;
            }
        }

        if (itemToRemove != null) {
            fileItems.remove(itemToRemove);

            listRefresh();
        }
    }
    private void listRefresh(){
        ObservableList<String> items = FXCollections.observableArrayList();
        for (FileItem fileItem : getFileItems()) {
            items.add(fileItem.getLabel());
        }
        listOfChoisenFiles.setItems(items);
    }
    private void listenerOfChoiceListElement(){
        listOfChoisenFiles.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {

                System.out.println("Вибрано файл: " + newSelection);
            }
        });
    }
    public void refresh(){
        listRefresh();
    }
}