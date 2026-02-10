package com.franciscoguemes.sudoku;

import com.franciscoguemes.sudoku.gui.MainMenuBar;
import com.franciscoguemes.sudoku.gui.SudokuGridPane;
import com.franciscoguemes.sudoku.io.PuzzleReader;
import com.franciscoguemes.sudoku.model.Generator;
import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class GuiApp extends Application {

    private SudokuGridPane gridPane;
    private Stage primaryStage;
    private Puzzle currentPuzzle;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.gridPane = new SudokuGridPane();

        MainMenuBar menuBar = new MainMenuBar(
                this::notImplemented,       // New game
                this::notImplemented,       // Open game
                this::notImplemented,       // Open recent game
                this::notImplemented,       // Save game
                this::notImplemented,       // Print
                Platform::exit,             // Quit
                this::newPuzzle,            // New puzzle
                this::notImplemented,       // Export puzzle
                this::importPuzzle,         // Import puzzle
                this::notImplemented,       // Start playing
                this::solvePuzzle,          // Solve puzzle
                this::visitWebsite,         // Visit the website
                this::showAbout             // About
        );

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(gridPane);

        Scene scene = new Scene(root, 600, 650);
        stage.setTitle("Sudoku");
        stage.setScene(scene);
        stage.show();

        List<String> args = getParameters().getRaw();
        if (!args.isEmpty()) {
            importPuzzleFromFile(Path.of(args.get(0)));
        }
    }

    private void newPuzzle() {
        Dialog<PuzzleType> dialog = new Dialog<>();
        dialog.setTitle("New Puzzle");
        dialog.setHeaderText("Select puzzle type:");
        dialog.initOwner(primaryStage);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ToggleGroup group = new ToggleGroup();
        VBox content = new VBox(8);
        content.setPadding(new Insets(10));

        for (PuzzleType type : PuzzleType.values()) {
            RadioButton rb = new RadioButton(type.getDescription());
            rb.setToggleGroup(group);
            rb.setUserData(type);
            content.getChildren().add(rb);
        }

        // Select SUDOKU (9x9) by default
        group.getToggles().stream()
                .filter(t -> t.getUserData() == PuzzleType.SUDOKU)
                .findFirst()
                .ifPresent(t -> t.setSelected(true));

        dialog.getDialogPane().setContent(content);
        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK && group.getSelectedToggle() != null) {
                return (PuzzleType) group.getSelectedToggle().getUserData();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(type -> {
            Generator generator = new Generator();
            Puzzle puzzle = generator.generateRandomSudoku(type);
            currentPuzzle = puzzle;
            gridPane.displayPuzzle(puzzle);
            primaryStage.setTitle("Sudoku - New " + type.getDescription());
        });
    }

    private void solvePuzzle() {
        if (currentPuzzle == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No puzzle");
            alert.setHeaderText(null);
            alert.setContentText("There is no puzzle to solve. Generate or import a puzzle first.");
            alert.showAndWait();
            return;
        }

        Puzzle copy = new Puzzle(currentPuzzle);
        Generator generator = new Generator();
        boolean solved = generator.solve(copy);

        if (solved) {
            gridPane.displayPuzzle(copy);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Solver");
            alert.setHeaderText("No solution found");
            alert.setContentText("The solver could not find a valid solution for this puzzle.");
            alert.showAndWait();
        }
    }

    private void importPuzzle() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Puzzle");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Sudoku files", "*.sudoku", "*.csv"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            importPuzzleFromFile(file.toPath());
        }
    }

    private void importPuzzleFromFile(Path filePath) {
        try {
            PuzzleReader reader = PuzzleReader.getReaderForFile(filePath);
            Puzzle puzzle = reader.read(filePath);
            currentPuzzle = puzzle;
            gridPane.displayPuzzle(puzzle);
            primaryStage.setTitle("Sudoku - " + filePath.getFileName());
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to import puzzle");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    private void visitWebsite() {
        getHostServices().showDocument("https://franciscoguemes.com");
    }

    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Sudoku");
        alert.setHeaderText("Sudoku");
        alert.setContentText("A Sudoku solver engine and desktop game.\nBy Francisco Guemes.");
        alert.showAndWait();
    }

    private void notImplemented() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Not implemented");
        alert.setHeaderText(null);
        alert.setContentText("Not implemented yet.");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
