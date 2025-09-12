package com.example.cardsmemorygame;

public class Card {
    private final String name;   // e.g. "2_of_hearts"
    private final String imagePath; // e.g. "/com/example/cardsmemorygame/cards/2_of_hearts.png"
    private boolean matched = false;

    public Card(String name, String imageFile) {
        this.name = name;
        this.imagePath = "/com/example/cardsmemorygame/cards/" + imageFile;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }
}
