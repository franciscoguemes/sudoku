package com.franciscoguemes.sudoku.gui.game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StatsPane extends VBox {

    private final Label scoreLabel;
    private final Label mistakesLabel;
    private final Label timeLabel;

    public StatsPane() {
        setAlignment(Pos.CENTER);
        setSpacing(8);
        setPadding(new Insets(8));

        scoreLabel = new Label("0");
        scoreLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        scoreLabel.setStyle("-fx-text-fill: #1A237E;");

        HBox statsRow = new HBox(24);
        statsRow.setAlignment(Pos.CENTER);

        VBox mistakesBox = createStatBox("Mistakes", "0/3");
        VBox timeBox = createStatBox("Time", "00:00");

        mistakesLabel = (Label) mistakesBox.getChildren().get(1);
        timeLabel = (Label) timeBox.getChildren().get(1);

        statsRow.getChildren().addAll(mistakesBox, timeBox);

        getChildren().addAll(scoreLabel, statsRow);
    }

    public Label getScoreLabel() {
        return scoreLabel;
    }

    public Label getMistakesLabel() {
        return mistakesLabel;
    }

    public Label getTimeLabel() {
        return timeLabel;
    }

    private VBox createStatBox(String title, String value) {
        VBox box = new VBox(2);
        box.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.NORMAL, 11));
        titleLabel.setStyle("-fx-text-fill: #757575;");

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        valueLabel.setStyle("-fx-text-fill: #37474F;");

        box.getChildren().addAll(titleLabel, valueLabel);
        return box;
    }
}
