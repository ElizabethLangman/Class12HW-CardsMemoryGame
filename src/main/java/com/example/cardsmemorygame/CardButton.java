package com.example.cardsmemorygame;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardButton extends Button {
    private final Card card;
    private final Image backImage;
    private final Image frontImage;
    private final ImageView backView;
    private final ImageView frontView;
    private boolean revealed = false;

    public CardButton(Card card) {
        this.card = card;

        // Load images
        backImage = new Image(getClass().getResource("/com/example/cardsmemorygame/cards/back.jpeg").toExternalForm());
        frontImage = new Image(getClass().getResource(card.getImagePath()).toExternalForm());

        // Create ImageViews with scaling
        backView = new ImageView(backImage);
        frontView = new ImageView(frontImage);

        // Set fixed size for the images
        backView.setFitWidth(80);
        backView.setFitHeight(120);
        backView.setPreserveRatio(true);

        frontView.setFitWidth(80);
        frontView.setFitHeight(120);
        frontView.setPreserveRatio(true);

        // Default look = back of card
        setGraphic(backView);
        setPrefSize(80, 120);
    }

    public void reveal() {
        if (!revealed) {
            setGraphic(frontView);
            revealed = true;
        }
    }

    public void hide() {
        setGraphic(backView);
        revealed = false;
    }

    public Card getCard() {
        return card;
    }
}
