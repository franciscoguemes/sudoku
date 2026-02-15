package com.franciscoguemes.sudoku.gui.game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class GameToolBar extends HBox {

    private static final String TOOL_BG = "#ECEFF1";
    private static final String TOOL_TEXT = "#37474F";
    private static final String TOOL_ACTIVE_BG = "#3F51B5";
    private static final String TOOL_ACTIVE_TEXT = "white";

    private Runnable onUndo;
    private Runnable onErase;
    private Runnable onNotesToggled;
    private boolean notesMode;
    private final Button notesBtn;

    public GameToolBar() {
        setAlignment(Pos.CENTER);
        setSpacing(12);
        setPadding(new Insets(8));

        Button undoBtn = createToolButton("Undo");
        Button eraseBtn = createToolButton("Erase");
        notesBtn = createToolButton("Notes");
        Button hintsBtn = createToolButton("Hints");
        //Delete the lines below to show again the Hints button
        hintsBtn.setVisible(false);
        hintsBtn.setManaged(false);

        undoBtn.setOnAction(e -> {
            if (onUndo != null) onUndo.run();
        });

        eraseBtn.setOnAction(e -> {
            if (onErase != null) onErase.run();
        });

        notesBtn.setOnAction(e -> {
            notesMode = !notesMode;
            updateNotesButtonStyle();
            if (onNotesToggled != null) onNotesToggled.run();
        });

        getChildren().addAll(undoBtn, eraseBtn, notesBtn, hintsBtn);
    }

    public boolean isNotesMode() {
        return notesMode;
    }

    public void setOnUndo(Runnable handler) {
        this.onUndo = handler;
    }

    public void setOnErase(Runnable handler) {
        this.onErase = handler;
    }

    public void setOnNotesToggled(Runnable handler) {
        this.onNotesToggled = handler;
    }

    private void updateNotesButtonStyle() {
        if (notesMode) {
            notesBtn.setStyle(
                    "-fx-background-color: " + TOOL_ACTIVE_BG + ";" +
                    "-fx-text-fill: " + TOOL_ACTIVE_TEXT + ";" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;"
            );
        } else {
            notesBtn.setStyle(
                    "-fx-background-color: " + TOOL_BG + ";" +
                    "-fx-text-fill: " + TOOL_TEXT + ";" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;"
            );
        }
    }

    private Button createToolButton(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(60, 40);
        btn.setFont(Font.font("System", 11));
        btn.setStyle(
                "-fx-background-color: " + TOOL_BG + ";" +
                "-fx-text-fill: " + TOOL_TEXT + ";" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );
        return btn;
    }
}
