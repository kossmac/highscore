package com.puf.dynamobeuth;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HighscoreTest extends javafx.application.Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/highscore.fxml"));
        primaryStage.setTitle("Highscore");
        primaryStage.setScene(new Scene(root, 520, 550));
        primaryStage.setResizable(false);

        primaryStage.show();
    }
}