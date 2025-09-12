package com.example.cardsmemorygame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameController {
    private final Stage stage;
    private final BorderPane root;
    private final GridPane grid;
    private final Label timerLabel;

    private final List<CardButton> buttons = new ArrayList<>();
    private CardButton firstSelected = null;
    private CardButton secondSelected = null;

    private int pairsFound = 0;
    private int totalPairs;

    private Timeline timer;
    private double elapsedTime = 0;

    private final int rows;
    private final int cols;

    public GameController(int rows, int cols, Stage stage) {
        this.stage = stage;
        this.rows = rows;
        this.cols = cols;

        root = new BorderPane();
        grid = new GridPane();
        timerLabel = new Label("00:00.000");

        setupBoard(rows, cols);
        setupTimer();
        setupBottomControls();

        root.setTop(timerLabel);
        root.setCenter(grid);
    }

    public BorderPane getRoot() {
        return root;
    }

    // ----------------- Board Setup -----------------
    private void setupBoard(int rows, int cols) {
        grid.setHgap(10);
        grid.setVgap(10);

        totalPairs = (rows * cols) / 2;
        List<Card> cards = generateCards(totalPairs);
        Collections.shuffle(cards);

        int index = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Card card = cards.get(index);
                CardButton btn = new CardButton(card);

                btn.setOnAction(e -> handleCardClick(btn));

                buttons.add(btn);
                grid.add(btn, c, r);
                index++;
            }
        }
    }

    private List<Card> generateCards(int pairs) {
        // Load *all* card files from resources
        String path = "/com/example/cardsmemorygame/cards/";
        var resource = getClass().getResource(path);
        if (resource == null) {
            throw new RuntimeException("Card folder not found in resources!");
        }

        // pool of available card images (must exist in resources/cards/)
        String[] allCards = {
                "2_of_hearts.jpeg", "2_of_spades.jpeg",
                "3_of_hearts.jpeg", "3_of_spades.jpeg",
                "Q_of_clubs.jpeg", "Q_of_diamonds.jpeg",
                "K_of_hearts.jpeg", "K_of_spades.jpeg",
                "A_of_spades.jpeg", "A_of_hearts.jpeg",
                "J_of_diamonds.jpeg", "J_of_clubs.jpeg"
        };

        // Shuffle and pick "pairs" number of unique cards
        List<String> deck = new ArrayList<>(List.of(allCards));
        Collections.shuffle(deck);

        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < pairs; i++) {
            String file = deck.get(i);
            String name = file.substring(0, file.indexOf(".jpeg"));
            cards.add(new Card(name, file));
            cards.add(new Card(name, file)); // duplicate for pair
        }

        Collections.shuffle(cards);
        return cards;
    }

    // ----------------- Timer -----------------
    private void setupTimer() {
        timer = new Timeline(new KeyFrame(Duration.millis(10), e -> {
            elapsedTime += 0.01;
            timerLabel.setText(formatTime(elapsedTime));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private String formatTime(double timeSeconds) {
        int minutes = (int) timeSeconds / 60;
        int seconds = (int) timeSeconds % 60;
        int millis = (int) ((timeSeconds - (int) timeSeconds) * 1000);

        return String.format("%02d:%02d.%03d", minutes, seconds, millis);
    }

    // ----------------- Card Logic -----------------
    private void handleCardClick(CardButton clicked) {
        if (clicked.getCard().isMatched()) return;
        if (firstSelected != null && secondSelected != null) return;

        clicked.reveal();

        if (firstSelected == null) {
            firstSelected = clicked;
        } else if (secondSelected == null && clicked != firstSelected) {
            secondSelected = clicked;
            checkMatch();
        }
    }

    private void checkMatch() {
        if (firstSelected.getCard().getName().equals(secondSelected.getCard().getName())) {
            // ✅ match
            firstSelected.getCard().setMatched(true);
            secondSelected.getCard().setMatched(true);

            firstSelected = null;
            secondSelected = null;
            pairsFound++;

            if (pairsFound == totalPairs) {
                endGame();
            }
        } else {
            // ❌ no match → flip back after delay
            Timeline delay = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                firstSelected.hide();
                secondSelected.hide();
                firstSelected = null;
                secondSelected = null;
            }));
            delay.play();
        }
    }

    // ----------------- End Game -----------------
    private void endGame() {
        timer.stop();

        Label resultLabel = new Label("Your Time: " + formatTime(elapsedTime));

        Button playAgainBtn = new Button("Play Again");
        playAgainBtn.setOnAction(e -> {
            GameController newGame = new GameController(rows, cols, stage);
            stage.setScene(new Scene(newGame.getRoot(), 800, 600));
        });

        Button backBtn = new Button("Back to Main Menu");
        backBtn.setOnAction(e -> {
            StartScreen startScreen = new StartScreen(stage);
            stage.setScene(new Scene(startScreen.getRoot(), 600, 400));
        });

        VBox endBox = new VBox(15, resultLabel, playAgainBtn, backBtn);
        endBox.setStyle("-fx-alignment: center; -fx-padding: 20;");
        root.setBottom(endBox);
    }

    // ----------------- Back to Menu -----------------
    private void setupBottomControls() {
        Button backBtn = new Button("Back to Main Menu");
        backBtn.setOnAction(e -> {
            StartScreen startScreen = new StartScreen(stage);
            stage.setScene(new Scene(startScreen.getRoot(), 600, 400));
        });

        VBox bottomBox = new VBox(backBtn);
        bottomBox.setSpacing(10);
        bottomBox.setStyle("-fx-padding: 10; -fx-alignment: center;");
        root.setBottom(bottomBox);
    }
}
