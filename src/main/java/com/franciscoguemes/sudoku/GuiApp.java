package com.franciscoguemes.sudoku;

import com.franciscoguemes.sudoku.gui.MainMenuBar;
import com.franciscoguemes.sudoku.gui.SudokuGridPane;
import com.franciscoguemes.sudoku.io.PuzzleReader;
import com.franciscoguemes.sudoku.model.Puzzle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class GuiApp extends Application {

    private SudokuGridPane gridPane;
    private Stage primaryStage;

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
                this::notImplemented,       // New puzzle
                this::notImplemented,       // Export puzzle
                this::importPuzzle,         // Import puzzle
                this::notImplemented,       // Start playing
                this::notImplemented,       // Solve puzzle
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
