package com.example.cardsmemorygame;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StartScreen {
    private final Stage stage;
    private final BorderPane root;
    private String difficulty = "Medium"; // default

    public StartScreen(Stage stage) {
        this.stage = stage;
        root = new BorderPane();
        setupUI();
    }

    public BorderPane getRoot() {
        return root;
    }

    private void setupUI() {
        Image logoImg = new Image(getClass().getResource("/com/example/cardsmemorygame/logo.jpeg").toExternalForm());
        ImageView logoView = new ImageView(logoImg);
        logoView.setFitWidth(200);
        logoView.setPreserveRatio(true);
        Text title = new Text("New Game");
        title.setFont(Font.font("Times New Roman", 32));
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        // Difficulty toggle buttons
        ToggleButton easyBtn = new ToggleButton("Easy (3x4)");
        ToggleButton mediumBtn = new ToggleButton("Medium (4x6)");
        ToggleButton hardBtn = new ToggleButton("Hard (6x8)");
        ToggleButton skzBtn = new ToggleButton("Surprize (SKZ)");

        // check this one
        easyBtn.setMinWidth(160);
        mediumBtn.setMinWidth(160);
        hardBtn.setMinWidth(160);
        hardBtn.setMinWidth(160);

        ToggleGroup group = new ToggleGroup();
        easyBtn.setToggleGroup(group);
        mediumBtn.setToggleGroup(group);
        hardBtn.setToggleGroup(group);
        skzBtn.setToggleGroup(group);
        easyBtn.setSelected(true); // default

        group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == easyBtn) difficulty = "Easy";
            else if (newVal == mediumBtn) difficulty = "Medium";
            else if (newVal == hardBtn) difficulty = "Hard";
            else if (newVal == skzBtn) difficulty = "SKZ";
        });

        VBox menuBox = new VBox(15, easyBtn, mediumBtn, hardBtn, skzBtn);
        menuBox.setAlignment(Pos.CENTER);

        // Start button
        Button startBtn = new Button("Start");
        startBtn.setOnAction(e -> startGame());

        VBox centerBox = new VBox(25, menuBox, startBtn);
        centerBox.setAlignment(Pos.CENTER);

        root.setCenter(centerBox);
        root.setPadding(new Insets(20));
    }

    private void startGame() {
        GameBoard board = new GameBoard(difficulty, stage);
        Scene gameScene = new Scene(board.getRoot(), 800, 600);
        stage.setScene(gameScene);
    }
}
