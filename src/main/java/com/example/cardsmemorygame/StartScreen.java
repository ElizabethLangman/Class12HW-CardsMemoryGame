package com.example.cardsmemorygame;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private String difficulty;

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
        logoView.setFitWidth(1500);
        logoView.setPreserveRatio(true);

        Text title = new Text("New Game");
        title.setFont(Font.font("Times New Roman", 64));
        title.setStyle("-fx-text-alignment: center;");

        VBox topBox = new VBox(30, logoView, title);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10));
        root.setTop(topBox);
        root.setStyle("-fx-background-color: white;");

        // Difficulty toggle buttons
        ToggleButton easyBtn = new ToggleButton("Easy (3x4)");
        ToggleButton mediumBtn = new ToggleButton("Medium (4x6)");
        ToggleButton hardBtn = new ToggleButton("Hard (6x8)");
        ToggleButton skzBtn = new ToggleButton("Surprize (SKZ)");

        ToggleGroup group = new ToggleGroup();
        easyBtn.setToggleGroup(group);
        mediumBtn.setToggleGroup(group);
        hardBtn.setToggleGroup(group);
        skzBtn.setToggleGroup(group);

        // Style buttons
        String defaultStyle = "-fx-background-color: #006400; -fx-text-fill: white;";
        String selectedStyle = "-fx-background-color: #228B22; -fx-text-fill: white;";

        easyBtn.setStyle(defaultStyle);
        mediumBtn.setStyle(defaultStyle);
        hardBtn.setStyle(defaultStyle);
        skzBtn.setStyle(defaultStyle);

        easyBtn.setMinSize(200, 50);
        mediumBtn.setMinSize(200, 50);
        hardBtn.setMinSize(200, 50);
        skzBtn.setMinSize(200, 50);

        easyBtn.setFont(Font.font("Times New Roman", 18));
        mediumBtn.setFont(Font.font("Times New Roman", 18));
        hardBtn.setFont(Font.font("Times New Roman", 18));
        skzBtn.setFont(Font.font("Times New Roman", 18));

        // Prevent deselection
        group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null && oldVal != null) oldVal.setSelected(true);
            else {
                if (newVal == easyBtn) difficulty = "Easy";
                else if (newVal == mediumBtn) difficulty = "Medium";
                else if (newVal == hardBtn) difficulty = "Hard";
                else if (newVal == skzBtn) difficulty = "SKZ";
            }
        });

        group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (oldVal != null) ((ToggleButton) oldVal).setStyle(defaultStyle);
            if (newVal != null) ((ToggleButton) newVal).setStyle(selectedStyle);

            if (newVal == easyBtn) difficulty = "Easy";
            else if (newVal == mediumBtn) difficulty = "Medium";
            else if (newVal == hardBtn) difficulty = "Hard";
            else if (newVal == skzBtn) difficulty = "SKZ";
        });

        VBox menuBox = new VBox(15, easyBtn, mediumBtn, hardBtn, skzBtn);
        menuBox.setAlignment(Pos.CENTER);

        // Start button
        Button startBtn = new Button("Start");
        startBtn.setMinSize(200, 50);
        startBtn.setFont(Font.font("Times New Roman", 18));
        startBtn.setOnMousePressed(e -> startBtn.setStyle("-fx-background-color: #228B22;"));
        startBtn.setOnMouseReleased(e -> startBtn.setStyle("-fx-background-color: #006400;"));
        startBtn.setOnAction(e -> {
            ToggleButton selected = (ToggleButton) group.getSelectedToggle();
            if (selected == null) {
                // Show warning or disable start
                System.out.println("Please select a difficulty.");
                return;
            }

            if (selected == easyBtn) difficulty = "Easy";
            else if (selected == mediumBtn) difficulty = "Medium";
            else if (selected == hardBtn) difficulty = "Hard";
            else if (selected == skzBtn) difficulty = "SKZ";

            startGame();
        });

        VBox centerBox = new VBox(25, menuBox, startBtn);
        centerBox.setAlignment(Pos.CENTER);
        root.setCenter(centerBox);

        // Footer
        Label footer = new Label("© 2025 Elizabeth L. Langman. All rights reserved.");
        footer.setStyle("-fx-font-size: 18px; -fx-text-fill: gray;");
        BorderPane.setAlignment(footer, Pos.CENTER);
        root.setBottom(footer);

        root.setPadding(new Insets(20));
    }

    private void startGame() {
        GameBoard board = new GameBoard(difficulty, stage);
        stage.setScene(new Scene(board.getRoot(), 800, 600));
        stage.setMaximized(true);
    }
}
