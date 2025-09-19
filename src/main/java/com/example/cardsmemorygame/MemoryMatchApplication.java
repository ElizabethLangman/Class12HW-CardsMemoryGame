package com.example.cardsmemorygame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MemoryMatchApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        StartScreen startScreen = new StartScreen(primaryStage);
        Scene scene = new Scene(startScreen.getRoot(), 1000, 400);

        primaryStage.setTitle("Memory Card Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
