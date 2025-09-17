package com.example.cardsmemorygame;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameBoard {
    private final BorderPane root;

    public GameBoard(String difficulty, Stage stage) {
        int rows = 0, cols = 0;
        switch (difficulty) {
            case "Easy" -> { rows = 3; cols = 4; }
            case "Medium" -> { rows = 4; cols = 6; }
            case "Hard" -> { rows = 6; cols = 8; }
        }

        GameController controller = new GameController(rows, cols, stage);
        root = controller.getRoot();
    }

    public BorderPane getRoot() {
        return root;
    }
}
