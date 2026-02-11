package com.franciscoguemes.sudoku.gui;

import com.franciscoguemes.sudoku.gui.game.GameGridPane;
import com.franciscoguemes.sudoku.gui.game.GameToolBar;
import com.franciscoguemes.sudoku.gui.game.NumberPad;
import com.franciscoguemes.sudoku.gui.game.SelectorBar;
import com.franciscoguemes.sudoku.gui.game.StatsPane;
import com.franciscoguemes.sudoku.model.Generator;
import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Map;

public class GameApp extends Application {

    private PuzzleType currentPuzzleType = PuzzleType.SUDOKU;
    private GameGridPane gridPane;
    private NumberPad numberPad;

    @Override
    public void start(Stage stage) {
        gridPane = new GameGridPane();
        numberPad = new NumberPad();

        // Puzzle type selector
        Map<PuzzleType, String> typeItems = new LinkedHashMap<>();
        for (PuzzleType type : PuzzleType.values()) {
            typeItems.put(type, type.getDescription());
        }
        SelectorBar<PuzzleType> typeSelector = new SelectorBar<>("Type:", typeItems, PuzzleType.SUDOKU);
        typeSelector.setOnSelectionChanged(this::onPuzzleTypeChanged);

        // Difficulty selector
        Map<String, String> diffItems = new LinkedHashMap<>();
        for (String diff : new String[]{"Easy", "Medium", "Hard", "Expert", "Master", "Extreme"}) {
            diffItems.put(diff, diff);
        }
        SelectorBar<String> difficultySelector = new SelectorBar<>("Difficulty:", diffItems, "Extreme");
        difficultySelector.setOnSelectionChanged(diff -> generateNewPuzzle());

        // Stats and tools
        StatsPane statsPane = new StatsPane();
        GameToolBar toolBar = new GameToolBar();
        toolBar.setOnErase(() -> gridPane.placeNumber(Puzzle.NO_VALUE));

        // Number pad
        numberPad.setOnNumberSelected(value -> gridPane.placeNumber(value));

        // New Game button
        Button newGameBtn = new Button("New Game");
        newGameBtn.setMaxWidth(Double.MAX_VALUE);
        newGameBtn.setPrefHeight(45);
        newGameBtn.setFont(Font.font("System", FontWeight.BOLD, 16));
        newGameBtn.setStyle(
                "-fx-background-color: #3F51B5;" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );
        newGameBtn.setOnAction(e -> generateNewPuzzle());

        // Right panel
        VBox rightPanel = new VBox(12);
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setPadding(new Insets(8, 16, 16, 8));
        rightPanel.getChildren().addAll(statsPane, toolBar, numberPad, newGameBtn);

        // Top selectors
        VBox topSelectors = new VBox(0);
        topSelectors.setPadding(new Insets(8, 0, 0, 0));
        topSelectors.getChildren().addAll(typeSelector, difficultySelector);

        // Main content
        HBox mainContent = new HBox(16);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(8, 16, 16, 16));
        HBox.setHgrow(gridPane, Priority.NEVER);
        mainContent.getChildren().addAll(gridPane, rightPanel);

        // Root layout
        BorderPane root = new BorderPane();
        root.setTop(topSelectors);
        root.setCenter(mainContent);
        root.setStyle("-fx-background-color: white;");

        Scene scene = new Scene(root, 850, 700);
        stage.setTitle("Sudoku");
        stage.setScene(scene);
        stage.show();

        generateNewPuzzle();
    }

    private void onPuzzleTypeChanged(PuzzleType type) {
        currentPuzzleType = type;
        numberPad.buildForType(type);
        generateNewPuzzle();
    }

    private void generateNewPuzzle() {
        Generator generator = new Generator();
        Puzzle puzzle = generator.generateRandomSudoku(currentPuzzleType);
        gridPane.displayPuzzle(puzzle);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
