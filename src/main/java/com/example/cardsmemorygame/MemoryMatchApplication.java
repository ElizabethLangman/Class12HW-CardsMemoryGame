package com.example.cardsmemorygame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MemoryMatchApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        StartScreen startScreen = new StartScreen(primaryStage);
        Scene scene = new Scene(startScreen.getRoot(), 800, 600);

        scene.getRoot().requestFocus(); //prevent initial button
        primaryStage.setTitle("Memory Card Game");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); //full screen
        primaryStage.show();

        CardLoader.preloadImages();
    }

    public static void main(String[] args) {
        launch();
    }
}
