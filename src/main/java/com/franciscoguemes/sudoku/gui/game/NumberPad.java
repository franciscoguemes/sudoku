package com.franciscoguemes.sudoku.gui.game;

import com.franciscoguemes.sudoku.model.PuzzleType;
import com.franciscoguemes.sudoku.util.ValueFormatter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.function.IntConsumer;

public class NumberPad extends GridPane {

    private static final String BUTTON_BG = "#E8EAF6";
    private static final String BUTTON_TEXT = "#1A237E";

    private IntConsumer onNumberSelected;

    public NumberPad() {
        setAlignment(Pos.CENTER);
        setHgap(8);
        setVgap(8);
        setPadding(new Insets(8));
        buildForType(PuzzleType.SUDOKU);
    }

    public void buildForType(PuzzleType type) {
        getChildren().clear();

        int min = type.getMinValue();
        int max = type.getMaxValue();

        int cols = switch (type) {
            case MINI_SUDOKU, SUDOKU -> 3;
            case BIG_SUDOKU, MAXI_SUDOKU -> 4;
        };

        int btnSize = switch (type) {
            case MINI_SUDOKU, SUDOKU -> 70;
            case BIG_SUDOKU -> 55;
            case MAXI_SUDOKU -> 48;
        };

        double fontSize = switch (type) {
            case MINI_SUDOKU, SUDOKU -> 24;
            case BIG_SUDOKU -> 18;
            case MAXI_SUDOKU -> 16;
        };

        int r = 0;
        int c = 0;
        for (int v = min; v <= max; v++) {
            String text = ValueFormatter.getGuiRepresentationOf(v);
            Button btn = new Button(text);
            btn.setPrefSize(btnSize, btnSize);
            btn.setFont(Font.font("System", FontWeight.BOLD, fontSize));
            btn.setStyle(
                    "-fx-background-color: " + BUTTON_BG + ";" +
                    "-fx-text-fill: " + BUTTON_TEXT + ";" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;"
            );

            final int value = v;
            btn.setOnAction(e -> {
                if (onNumberSelected != null) {
                    onNumberSelected.accept(value);
                }
            });

            add(btn, c, r);
            c++;
            if (c >= cols) {
                c = 0;
                r++;
            }
        }
    }

    public void setOnNumberSelected(IntConsumer handler) {
        this.onNumberSelected = handler;
    }
}
