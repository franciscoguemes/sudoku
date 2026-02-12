package com.franciscoguemes.sudoku.gui.game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class StatsPane extends VBox {

    private static final int MAX_MISTAKES = 3;
    private static final Color ICON_COLOR = Color.web("#37474F");

    private final Label scoreLabel;
    private final Label mistakesLabel;
    private final Label timeLabel;
    private final Button pauseButton;
    private final Timeline timer;
    private int mistakes = 0;
    private int elapsedSeconds = 0;
    private boolean paused = false;
    private Runnable onPauseToggled;

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
        mistakesLabel = (Label) mistakesBox.getChildren().get(1);

        // Time box with pause button
        VBox timeBox = createStatBox("Time", "00:00");
        timeLabel = (Label) timeBox.getChildren().get(1);

        pauseButton = new Button();
        pauseButton.setGraphic(createPauseIcon());
        pauseButton.setPrefSize(28, 28);
        pauseButton.setStyle(
                "-fx-background-color: #E8EAF6;" +
                "-fx-background-radius: 14;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 0;"
        );
        pauseButton.setOnAction(e -> togglePause());

        HBox timeWithPause = new HBox(6);
        timeWithPause.setAlignment(Pos.CENTER);
        timeWithPause.getChildren().addAll(timeBox, pauseButton);

        statsRow.getChildren().addAll(mistakesBox, timeWithPause);

        getChildren().addAll(scoreLabel, statsRow);

        // Timer ticks every second
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            elapsedSeconds++;
            updateTimeDisplay();
        }));
        timer.setCycleCount(Animation.INDEFINITE);
    }

    public void startTimer() {
        elapsedSeconds = 0;
        paused = false;
        pauseButton.setGraphic(createPauseIcon());
        updateTimeDisplay();
        timer.playFromStart();
    }

    public void stopTimer() {
        timer.stop();
    }

    public boolean isPaused() {
        return paused;
    }

    public void setOnPauseToggled(Runnable handler) {
        this.onPauseToggled = handler;
    }

    private void togglePause() {
        if (paused) {
            paused = false;
            pauseButton.setGraphic(createPauseIcon());
            timer.play();
        } else {
            paused = true;
            pauseButton.setGraphic(createPlayIcon());
            timer.pause();
        }
        if (onPauseToggled != null) onPauseToggled.run();
    }

    private HBox createPauseIcon() {
        Rectangle bar1 = new Rectangle(3, 10, ICON_COLOR);
        Rectangle bar2 = new Rectangle(3, 10, ICON_COLOR);
        HBox icon = new HBox(2, bar1, bar2);
        icon.setAlignment(Pos.CENTER);
        return icon;
    }

    private Polygon createPlayIcon() {
        Polygon triangle = new Polygon(0, 0, 0, 10, 8, 5);
        triangle.setFill(ICON_COLOR);
        return triangle;
    }

    private void updateTimeDisplay() {
        int minutes = elapsedSeconds / 60;
        int seconds = elapsedSeconds % 60;
        timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    public void incrementMistakes() {
        mistakes++;
        mistakesLabel.setText(mistakes + "/" + MAX_MISTAKES);
        if (mistakes >= MAX_MISTAKES) {
            mistakesLabel.setStyle("-fx-text-fill: #D32F2F; -fx-font-weight: bold;");
        }
    }

    public void decrementMistakes() {
        if (mistakes > 0) mistakes--;
        mistakesLabel.setText(mistakes + "/" + MAX_MISTAKES);
        mistakesLabel.setStyle("-fx-text-fill: #37474F;");
    }

    public int getMistakes() {
        return mistakes;
    }

    public void resetMistakes() {
        mistakes = 0;
        mistakesLabel.setText("0/" + MAX_MISTAKES);
        mistakesLabel.setStyle("-fx-text-fill: #37474F;");
    }

    public boolean isGameOver() {
        return mistakes >= MAX_MISTAKES;
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
