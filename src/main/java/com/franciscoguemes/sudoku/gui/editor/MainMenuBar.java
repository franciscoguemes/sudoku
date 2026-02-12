package com.franciscoguemes.sudoku.gui.editor;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class MainMenuBar extends MenuBar {

    public MainMenuBar(Runnable onNewGame,
                       Runnable onOpenGame,
                       Runnable onOpenRecentGame,
                       Runnable onSaveGame,
                       Runnable onPrint,
                       Runnable onQuit,
                       Runnable onNewPuzzle,
                       Runnable onExportPuzzle,
                       Runnable onImportPuzzle,
                       Runnable onStartPlaying,
                       Runnable onSolvePuzzle,
                       Runnable onVisitWebsite,
                       Runnable onAbout) {

        getMenus().addAll(
                buildFileMenu(onNewGame, onOpenGame, onOpenRecentGame, onSaveGame, onPrint, onQuit),
                buildGeneratorMenu(onNewPuzzle, onExportPuzzle, onImportPuzzle, onStartPlaying),
                buildSolverMenu(onSolvePuzzle),
                buildHelpMenu(onVisitWebsite, onAbout)
        );
    }

    private Menu buildFileMenu(Runnable onNewGame,
                               Runnable onOpenGame,
                               Runnable onOpenRecentGame,
                               Runnable onSaveGame,
                               Runnable onPrint,
                               Runnable onQuit) {
        MenuItem newGame = new MenuItem("New game");
        newGame.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        newGame.setOnAction(e -> onNewGame.run());

        MenuItem openGame = new MenuItem("Open game");
        openGame.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        openGame.setOnAction(e -> onOpenGame.run());

        MenuItem openRecentGame = new MenuItem("Open recent game");
        openRecentGame.setOnAction(e -> onOpenRecentGame.run());

        MenuItem saveGame = new MenuItem("Save game");
        saveGame.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveGame.setOnAction(e -> onSaveGame.run());

        MenuItem print = new MenuItem("Print");
        print.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
        print.setOnAction(e -> onPrint.run());

        MenuItem quit = new MenuItem("Quit");
        quit.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        quit.setOnAction(e -> onQuit.run());

        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(
                newGame,
                new SeparatorMenuItem(),
                openGame, openRecentGame, saveGame,
                new SeparatorMenuItem(),
                print,
                new SeparatorMenuItem(),
                quit
        );
        return fileMenu;
    }

    private Menu buildGeneratorMenu(Runnable onNewPuzzle,
                                    Runnable onExportPuzzle,
                                    Runnable onImportPuzzle,
                                    Runnable onStartPlaying) {
        MenuItem newPuzzle = new MenuItem("New puzzle");
        newPuzzle.setOnAction(e -> onNewPuzzle.run());

        MenuItem exportPuzzle = new MenuItem("Export puzzle");
        exportPuzzle.setOnAction(e -> onExportPuzzle.run());

        MenuItem importPuzzle = new MenuItem("Import puzzle");
        importPuzzle.setOnAction(e -> onImportPuzzle.run());

        MenuItem startPlaying = new MenuItem("Start playing");
        startPlaying.setOnAction(e -> onStartPlaying.run());

        Menu generatorMenu = new Menu("Generator");
        generatorMenu.getItems().addAll(
                newPuzzle,
                new SeparatorMenuItem(),
                exportPuzzle, importPuzzle,
                new SeparatorMenuItem(),
                startPlaying
        );
        return generatorMenu;
    }

    private Menu buildSolverMenu(Runnable onSolvePuzzle) {
        MenuItem solvePuzzle = new MenuItem("Solve puzzle");
        solvePuzzle.setOnAction(e -> onSolvePuzzle.run());

        Menu solverMenu = new Menu("Solver");
        solverMenu.getItems().add(solvePuzzle);
        return solverMenu;
    }

    private Menu buildHelpMenu(Runnable onVisitWebsite, Runnable onAbout) {
        MenuItem visitWebsite = new MenuItem("Visit the website");
        visitWebsite.setOnAction(e -> onVisitWebsite.run());

        MenuItem about = new MenuItem("About");
        about.setOnAction(e -> onAbout.run());

        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().addAll(visitWebsite, about);
        return helpMenu;
    }
}
