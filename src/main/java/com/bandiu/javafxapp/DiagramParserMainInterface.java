package com.bandiu.javafxapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.bandiu.javafxapp.view.StartSceneController;

import java.io.IOException;
import java.net.URL;

public class DiagramParserMainInterface extends Application {
    private Stage primaryStage;
    private VBox rootLayout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Diagram Parser Interface");

        initRootLayout();
    }


    public void initRootLayout() {
        try {
            // Завантаження кореневого макету з fxml файлу.
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource("startScene.fxml"));
            rootLayout =  loader.load();

            // Показати сцену, що містить кореневий макет.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Даємо контролеру доступ до головного додатка.
            StartSceneController controller = loader.getController();

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}