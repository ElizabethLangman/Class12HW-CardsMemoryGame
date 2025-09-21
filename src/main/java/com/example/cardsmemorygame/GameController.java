package com.example.cardsmemorygame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.*;
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
    private final String difficulty;

    public GameController(int rows, int cols, String difficulty, Stage stage) {
        this.stage = stage;
        this.rows = rows;
        this.cols = cols;
        this.difficulty = difficulty;
        this.root = new BorderPane();
        this.grid = new GridPane();
        this.timerLabel = new Label("00:00.000");

        setupTopBar();
        setupBoard(rows, cols);
        setupTimer();
        setupBottomBar();
    }

    public BorderPane getRoot() {
        return root;
    }

    // ----------------- Top Bar -----------------
    private void setupTopBar() {
        Image logoImg = new Image(getClass().getResource("/com/example/cardsmemorygame/logo.jpeg").toExternalForm());
        ImageView logoView = new ImageView(logoImg);
        logoView.setFitWidth(1500);
        logoView.setPreserveRatio(true);

        timerLabel.setFont(javafx.scene.text.Font.font("Times New Roman", 32));
        timerLabel.setStyle("-fx-text-alignment: center;");

        VBox topBox = new VBox(10, logoView, timerLabel);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10));
        root.setTop(topBox);
    }

    // ----------------- Board Setup -----------------
    private void setupBoard(int rows, int cols) {
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        totalPairs = (rows * cols) / 2;

        List<Card> cards = generateCards(totalPairs);
        Collections.shuffle(cards);

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        double cardWidth = (screenWidth - 100) / cols;
        double cardHeight = (screenHeight - 300) / rows;

        int index = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                CardButton btn = new CardButton(cards.get(index));
                btn.setOnAction(e -> handleCardClick(btn));

                btn.setPrefSize(cardWidth, cardHeight);
                btn.setMinSize(cardWidth, cardHeight);
                btn.setMaxSize(cardWidth, cardHeight);
                btn.getFrontView().setFitWidth(cardWidth);
                btn.getFrontView().setFitHeight(cardHeight);
                btn.getBackView().setFitWidth(cardWidth);
                btn.getBackView().setFitHeight(cardHeight);

                buttons.add(btn);
                grid.add(btn, c, r);
                index++;
            }
        }

        root.setCenter(grid);

        // Show all cards for 5 seconds, then hide
        for (CardButton btn : buttons) {
            btn.reveal();
        }

        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            for (CardButton btn : buttons) btn.hide();
            elapsedTime = 0; // reset timer
            timerLabel.setText("00:00.00");
            timer.playFromStart();
        }));
        delay.setCycleCount(1);
        delay.play();
    }

    private List<Card> generateCards(int pairs) {
        List<Card> cards = new ArrayList<>();

        if (difficulty.equals("SKZ")) {
            Map<String, String> albumToMusic = Map.of(
                    "SKZ_ATE.png", "/com/example/cardsmemorygame/albums/music/CHK_CHK_BOOM.mp3",
                    "SKZ_CEREMONY.png", "/com/example/cardsmemorygame/albums/music/CEREMONY.mp3",
                    "SKZ_HOLLOW.png", "/com/example/cardsmemorygame/albums/music/HOLLOW.mp3",
                    "SKZ_HOP.png", "/com/example/cardsmemorygame/albums/music/WALKING_ON_WATER.mp3",
                    "SKZ_IN_LIFE.png", "/com/example/cardsmemorygame/albums/music/BACK_DOOR.mp3",
                    "SKZ_KARMA.png", "/com/example/cardsmemorygame/albums/music/BLEEP.mp3",
                    "SKZ_MAXIDENT.png", "/com/example/cardsmemorygame/albums/music/CIRCUS.mp3",
                    "SKZ_REPLAY.png", "/com/example/cardsmemorygame/albums/music/MAKNAE_ON_TOP.mp3"
            );

            for (String file : albumToMusic.keySet()) {
                String name = file.substring(0, file.indexOf(".png"));
                String musicPath = albumToMusic.get(file);
                cards.add(new Card(name, file, musicPath));
                cards.add(new Card(name, file, musicPath)); // duplicate for pair
            }
        } else {
            List<String> deck = CardLoader.getRandomCards(pairs);
            for (String file : deck) {
                String name = file.substring(0, file.indexOf(".jpeg"));
                cards.add(new Card(name, file));
                cards.add(new Card(name, file));
            }
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
        //maybe delete
        //timer.play();
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

        if (firstSelected == null) firstSelected = clicked;
        else if (secondSelected == null && clicked != firstSelected) {
            secondSelected = clicked;
            checkMatch();
        }
    }

    private void checkMatch() {
        if (firstSelected.getCard().getName().equals(secondSelected.getCard().getName())) {
            // matched
            firstSelected.getCard().setMatched(true);
            secondSelected.getCard().setMatched(true);

            // --- SKZ music feature ---
            if (difficulty.equals("SKZ")) {
                String musicPath = firstSelected.getCard().getMusicPath();
                if (musicPath != null) playMatchMusic(musicPath);
            }

            firstSelected = null;
            secondSelected = null;
            pairsFound++;

            if (pairsFound == totalPairs) endGame();
        } else {
            // no match -> flip back after delay
            Timeline delay = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                firstSelected.hide();
                secondSelected.hide();
                firstSelected = null;
                secondSelected = null;
            }));
            delay.play();
        }
    }

    // ----------------- Music on Match -----------------
    private void playMatchMusic(String musicPath) {
        // disable all input during music
        root.setDisable(true);

        Media sound = new Media(Objects.requireNonNull(getClass().getResource(musicPath)).toExternalForm());
        MediaPlayer player = new MediaPlayer(sound);

        // wait until media is fully loaded before playing
        player.setOnReady(() -> player.play());

        //re-enable screen after the music ends
        player.setOnEndOfMedia(() -> root.setDisable(false));

        // Safety: also re-enable if there's an error
        player.setOnError(() -> {
            System.err.println("Error playing music: " + player.getError());
            root.setDisable(false);
        });
    }

    // ----------------- End Game -----------------
    private void endGame() {
        timer.stop();

        Label resultLabel = new Label("Your Time: " + formatTime(elapsedTime));
        resultLabel.setFont(javafx.scene.text.Font.font("Times New Roman", 24));

        Button playAgainBtn = new Button("Play Again");
        playAgainBtn.setMinWidth(200);
        playAgainBtn.setOnAction(e -> {
            GameController newGame = new GameController(rows, cols, difficulty, stage);
            stage.setScene(new Scene(newGame.getRoot(), Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight()));
        });

        Button backBtn = new Button("Back to Main Menu");
        backBtn.setMinWidth(200);
        backBtn.setOnAction(e -> {
            StartScreen startScreen = new StartScreen(stage);
            stage.setScene(new Scene(startScreen.getRoot(), 800, 600));
        });

        VBox endBox = new VBox(15, resultLabel, playAgainBtn, backBtn);
        endBox.setStyle("-fx-alignment: center; -fx-padding: 20;");
        endBox.setAlignment(Pos.CENTER);
        root.setBottom(endBox);
    }

    // ----------------- Back to Menu -----------------
    private void setupBottomBar() {
        Button backBtn = new Button("Back to Main Menu");
        backBtn.setOnAction(e -> {
            StartScreen startScreen = new StartScreen(stage);
            stage.setScene(new Scene(startScreen.getRoot(), 800, 600));
        });

        Label footer = new Label(difficulty.equals("SKZ") ?
                "© Content credit to Stray Kids" :
                "© 2025 Elizabeth L. Langman. All rights reserved.");
        footer.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");
        footer.setAlignment(Pos.CENTER);

        VBox bottomBox = new VBox(10, backBtn, footer);
        bottomBox.setAlignment(Pos.BOTTOM_CENTER);
        bottomBox.setPadding(new Insets(10));
        root.setBottom(bottomBox);
    }
}
