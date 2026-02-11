package com.franciscoguemes.sudoku.gui.game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class GameToolBar extends HBox {

    private static final String TOOL_BG = "#ECEFF1";
    private static final String TOOL_TEXT = "#37474F";

    private Runnable onErase;

    public GameToolBar() {
        setAlignment(Pos.CENTER);
        setSpacing(12);
        setPadding(new Insets(8));

        Button undoBtn = createToolButton("Undo");
        Button eraseBtn = createToolButton("Erase");
        Button notesBtn = createToolButton("Notes");
        Button hintsBtn = createToolButton("Hints");

        eraseBtn.setOnAction(e -> {
            if (onErase != null) onErase.run();
        });

        getChildren().addAll(undoBtn, eraseBtn, notesBtn, hintsBtn);
    }

    public void setOnErase(Runnable handler) {
        this.onErase = handler;
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
