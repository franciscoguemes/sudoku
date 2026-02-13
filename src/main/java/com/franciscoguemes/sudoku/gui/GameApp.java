package com.franciscoguemes.sudoku.gui;

import com.franciscoguemes.sudoku.gui.game.GameGridPane;
import com.franciscoguemes.sudoku.gui.game.GameToolBar;
import com.franciscoguemes.sudoku.gui.game.NumberPad;
import com.franciscoguemes.sudoku.gui.game.SelectorBar;
import com.franciscoguemes.sudoku.gui.game.StatsPane;
import com.franciscoguemes.sudoku.model.Difficulty;
import com.franciscoguemes.sudoku.model.Generator;
import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import java.util.LinkedHashMap;
import java.util.Map;

public class GameApp extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(GameApp.class);

    private PuzzleType currentPuzzleType = PuzzleType.SUDOKU;
    private Difficulty currentDifficulty = Difficulty.MEDIUM;
    private Stage primaryStage;
    private GameGridPane gridPane;
    private NumberPad numberPad;
    private StatsPane statsPane;

    @Override
    public void start(Stage stage) {
        LOG.info("Starting GameApp");
        this.primaryStage = stage;
        gridPane = new GameGridPane();
        numberPad = new NumberPad();
        gridPane.setOnWrongMove(this::handleWrongMove);
        gridPane.setOnBoardChanged(() -> numberPad.updateExhaustedNumbers(gridPane.getPuzzle()));
        gridPane.setOnPuzzleCompleted(() -> statsPane.stopTimer());

        // Puzzle type selector
        Map<PuzzleType, String> typeItems = new LinkedHashMap<>();
        for (PuzzleType type : PuzzleType.values()) {
            typeItems.put(type, type.getDescription());
        }
        SelectorBar<PuzzleType> typeSelector = new SelectorBar<>("Type:", typeItems, PuzzleType.SUDOKU);
        typeSelector.setOnSelectionChanged(this::onPuzzleTypeChanged);

        // Difficulty selector
        Map<Difficulty, String> diffItems = new LinkedHashMap<>();
        for (Difficulty diff : Difficulty.values()) {
            diffItems.put(diff, diff.getDescription());
        }
        SelectorBar<Difficulty> difficultySelector = new SelectorBar<>("Difficulty:", diffItems, Difficulty.EXTREME);
        difficultySelector.setOnSelectionChanged(this::onDifficultyChanged);

        // Stats and tools
        statsPane = new StatsPane();
        statsPane.setOnPauseToggled(() -> gridPane.setVisible(!statsPane.isPaused()));
        GameToolBar toolBar = new GameToolBar();
        toolBar.setOnUndo(() -> gridPane.undo());
        toolBar.setOnErase(() -> gridPane.erase());
        toolBar.setOnNotesToggled(() -> gridPane.setNotesMode(toolBar.isNotesMode()));

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
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        stage.setTitle("Sudoku");
        stage.setScene(scene);
        stage.show();
        LOG.info("GameApp ready");

        generateNewPuzzle();
    }

    private void onPuzzleTypeChanged(PuzzleType type) {
        LOG.info("Puzzle type changed to {}", type);
        currentPuzzleType = type;
        numberPad.buildForType(type);
        generateNewPuzzle();
    }

    private void onDifficultyChanged(Difficulty difficulty) {
        LOG.info("Difficulty changed to {}", difficulty);
        currentDifficulty = difficulty;
        generateNewPuzzle();
    }

    private void generateNewPuzzle() {
        LOG.info("Generating new puzzle: type={}, difficulty={}", currentPuzzleType, currentDifficulty);
        Generator generator = new Generator();
        Puzzle puzzle = generator.generateTechniqueGradedSudoku(currentPuzzleType, currentDifficulty);
        Puzzle solution = new Puzzle(puzzle);
        generator.solve(solution);
        gridPane.displayPuzzle(puzzle, solution);
        gridPane.setVisible(true);
        statsPane.resetMistakes();
        statsPane.startTimer();
    }

    private void handleWrongMove() {
        statsPane.incrementMistakes();
        if (statsPane.isGameOver()) {
            showGameOverDialog();
        }
    }

    private void showGameOverDialog() {
        Alert dialog = new Alert(Alert.AlertType.NONE);
        dialog.setTitle("Game Over");
        dialog.setHeaderText("You've made 3 mistakes!");
        dialog.setContentText("The game is over. What would you like to do?");
        dialog.initOwner(primaryStage);

        ButtonType secondChance = new ButtonType("Second chance");
        ButtonType newGame = new ButtonType("New game");
        dialog.getButtonTypes().addAll(secondChance, newGame);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == newGame) {
            generateNewPuzzle();
        } else {
            statsPane.decrementMistakes();
        }
    }

    private void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> { gridPane.moveSelection(-1, 0); event.consume(); }
            case DOWN -> { gridPane.moveSelection(1, 0); event.consume(); }
            case LEFT -> { gridPane.moveSelection(0, -1); event.consume(); }
            case RIGHT -> { gridPane.moveSelection(0, 1); event.consume(); }
            case DIGIT1, NUMPAD1 -> handleValueKey(1, event);
            case DIGIT2, NUMPAD2 -> handleValueKey(2, event);
            case DIGIT3, NUMPAD3 -> handleValueKey(3, event);
            case DIGIT4, NUMPAD4 -> handleValueKey(4, event);
            case DIGIT5, NUMPAD5 -> handleValueKey(5, event);
            case DIGIT6, NUMPAD6 -> handleValueKey(6, event);
            case DIGIT7, NUMPAD7 -> handleValueKey(7, event);
            case DIGIT8, NUMPAD8 -> handleValueKey(8, event);
            case DIGIT9, NUMPAD9 -> handleValueKey(9, event);
            case A -> handleValueKey(10, event);
            case B -> handleValueKey(11, event);
            case C -> handleValueKey(12, event);
            case D -> handleValueKey(13, event);
            case E -> handleValueKey(14, event);
            case F -> handleValueKey(15, event);
            case G -> handleValueKey(16, event);
            case DELETE, BACK_SPACE -> { gridPane.erase(); event.consume(); }
            default -> {}
        }
    }

    private void handleValueKey(int value, KeyEvent event) {
        if (value >= currentPuzzleType.getMinValue() && value <= currentPuzzleType.getMaxValue()) {
            gridPane.placeNumber(value);
            event.consume();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
