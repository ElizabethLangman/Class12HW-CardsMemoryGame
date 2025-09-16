package com.example.cardsmemorygame;

public class Card {
    private final String name;
    private final String imagePath;
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
