package com.example.cardsmemorygame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SingleCardImageFlip extends Application {

    private boolean isFlipped = false;

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        // Front of the card
        Image aceImage = new Image(new FileInputStream("/Users/elizabethlangman/Pictures/Diamond2.jpeg"));
        ImageView frontView = new ImageView(aceImage);
        frontView.setFitWidth(150);
        frontView.setFitHeight(200);

        // Back of the card
        Image backImage = new Image(new FileInputStream("/Users/elizabethlangman/Pictures/Blue_Back.jpeg"));
        ImageView backView = new ImageView(backImage);
        backView.setFitWidth(150);
        backView.setFitHeight(200);

        StackPane card = new StackPane(backView);

        backView.setOnMouseClicked(e -> flipCard(card, frontView, backView));
        frontView.setOnMouseClicked(e -> flipCard(card, frontView, backView));

        Scene scene = new Scene(card, 300, 300);
        stage.setTitle("Card Flip Demo");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void flipCard(StackPane card, ImageView frontView, ImageView backView) {
        card.getChildren().clear();
        if (isFlipped) {
            card.getChildren().add(backView);
        } else {
            card.getChildren().add(frontView);
        }
        isFlipped = !isFlipped;
    }
}