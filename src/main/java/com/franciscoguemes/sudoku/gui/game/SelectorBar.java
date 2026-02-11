package com.franciscoguemes.sudoku.gui.game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SelectorBar<T> extends HBox {

    private static final String SELECTED_COLOR = "#1A237E";
    private static final String NORMAL_COLOR = "#757575";
    private static final double FONT_SIZE = 14;

    private final Map<T, Label> labels = new LinkedHashMap<>();
    private T selectedValue;
    private Consumer<T> onSelectionChanged;

    public SelectorBar(String prefix, Map<T, String> items, T defaultSelected) {
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(16);
        setPadding(new Insets(8, 16, 4, 16));

        if (prefix != null && !prefix.isEmpty()) {
            Label prefixLabel = new Label(prefix);
            prefixLabel.setFont(Font.font("System", FontWeight.NORMAL, FONT_SIZE));
            prefixLabel.setStyle("-fx-text-fill: #9E9E9E;");
            getChildren().add(prefixLabel);
        }

        for (Map.Entry<T, String> entry : items.entrySet()) {
            Label label = new Label(entry.getValue());
            label.setFont(Font.font("System", FontWeight.NORMAL, FONT_SIZE));
            label.setCursor(Cursor.HAND);
            label.setOnMouseClicked(e -> select(entry.getKey()));
            labels.put(entry.getKey(), label);
            getChildren().add(label);
        }

        this.selectedValue = defaultSelected;
        updateStyles();
    }

    public void select(T value) {
        this.selectedValue = value;
        updateStyles();
        if (onSelectionChanged != null) {
            onSelectionChanged.accept(value);
        }
    }

    public T getSelectedValue() {
        return selectedValue;
    }

    public void setOnSelectionChanged(Consumer<T> handler) {
        this.onSelectionChanged = handler;
    }

    private void updateStyles() {
        for (Map.Entry<T, Label> entry : labels.entrySet()) {
            Label label = entry.getValue();
            if (entry.getKey().equals(selectedValue)) {
                label.setFont(Font.font("System", FontWeight.BOLD, FONT_SIZE));
                label.setStyle("-fx-text-fill: " + SELECTED_COLOR + ";");
            } else {
                label.setFont(Font.font("System", FontWeight.NORMAL, FONT_SIZE));
                label.setStyle("-fx-text-fill: " + NORMAL_COLOR + ";");
            }
        }
    }
}
