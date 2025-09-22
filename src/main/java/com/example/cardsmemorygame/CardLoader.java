package com.example.cardsmemorygame;
import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CardLoader {
    // Reads all filenames from cards.txt in resources
    public static List<String> loadCardFilenames() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                CardLoader.class.getResourceAsStream("/com/example/cardsmemorygame/cards/cardsName.txt")))) {
            return reader.lines().filter(line -> !line.isBlank()).collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error loading card filenames");
            e.printStackTrace(System.err);
            return new ArrayList<>();
        }
    }

    // Returns a random selection of "count" card filenames
    public static List<String> getRandomCards(int count) {
        List<String> allCards = loadCardFilenames();
        Collections.shuffle(allCards);
        return allCards.subList(0, count);
    }

    public static void preloadImages() {
        for (String filename : loadCardFilenames()) {
            String path = "/com/example/cardsmemorygame/cards/" + filename;
            try {
                new Image(CardLoader.class.getResource(path).toExternalForm());
            } catch (Exception e) {
                System.err.println("Failed to preload: " + path);
            }
        }
    }
}
