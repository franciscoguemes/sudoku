package com.franciscoguemes.sudoku.gui.game;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import com.franciscoguemes.sudoku.util.ValueFormatter;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameGridPane extends StackPane {

    private static final String SELECTED_BG = "#BBDEFB";
    private static final String HIGHLIGHT_BG = "#E3F2FD";
    private static final String SAME_NUMBER_BG = "#C5CAE9";
    private static final String CELL_BORDER = "#B0BEC5";
    private static final String BOX_BORDER = "#37474F";
    private static final String GIVEN_COLOR = "#1A237E";
    private static final String PLAYER_COLOR = "#1565C0";

    private final GridPane outerGrid = new GridPane();
    private Puzzle puzzle;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private Label[][] cellLabels;

    public GameGridPane() {
        setAlignment(Pos.CENTER);
        getChildren().add(outerGrid);
        outerGrid.setAlignment(Pos.CENTER);
    }

    public void displayPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
        this.selectedRow = -1;
        this.selectedCol = -1;
        rebuild();
    }

    public void refresh() {
        if (puzzle == null) return;
        PuzzleType type = puzzle.getPuzzleType();
        double fontSize = fontSizeFor(type);

        for (int r = 0; r < type.getRows(); r++) {
            for (int c = 0; c < type.getColumns(); c++) {
                updateCellAppearance(r, c, fontSize);
            }
        }
    }

    public void placeNumber(int value) {
        if (selectedRow < 0 || selectedCol < 0) return;
        if (!puzzle.isSlotMutable(selectedRow, selectedCol)) return;

        if (value == Puzzle.NO_VALUE) {
            puzzle.makeSlotEmpty(selectedRow, selectedCol);
            refresh();
            return;
        }

        int previousValue = puzzle.getValue(selectedRow, selectedCol);
        if (previousValue != Puzzle.NO_VALUE) {
            puzzle.makeSlotEmpty(selectedRow, selectedCol);
        }

        puzzle.makeMove(selectedRow, selectedCol, value, true);

        if (puzzle.getValue(selectedRow, selectedCol) == Puzzle.NO_VALUE && previousValue != Puzzle.NO_VALUE) {
            puzzle.makeMove(selectedRow, selectedCol, previousValue, true);
        }

        refresh();
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public int getSelectedCol() {
        return selectedCol;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    private void rebuild() {
        outerGrid.getChildren().clear();

        PuzzleType type = puzzle.getPuzzleType();
        int boxWidth = type.getBoxWidth();
        int boxHeight = type.getBoxHeight();
        int boxCols = type.getColumns() / boxWidth;
        int boxRows = type.getRows() / boxHeight;
        double fontSize = fontSizeFor(type);

        cellLabels = new Label[type.getRows()][type.getColumns()];

        for (int br = 0; br < boxRows; br++) {
            for (int bc = 0; bc < boxCols; bc++) {
                GridPane boxGrid = createBoxGrid(type, br, bc, fontSize);
                outerGrid.add(boxGrid, bc, br);
            }
        }
    }

    private GridPane createBoxGrid(PuzzleType type, int boxRow, int boxCol, double fontSize) {
        int boxWidth = type.getBoxWidth();
        int boxHeight = type.getBoxHeight();
        int startRow = boxRow * boxHeight;
        int startCol = boxCol * boxWidth;

        GridPane boxGrid = new GridPane();
        boxGrid.setStyle("-fx-border-color: " + BOX_BORDER + "; -fx-border-width: 1.5;");

        for (int r = 0; r < boxHeight; r++) {
            for (int c = 0; c < boxWidth; c++) {
                int row = startRow + r;
                int col = startCol + c;

                Label label = new Label();
                label.setAlignment(Pos.CENTER);
                label.setPrefSize(cellSizeFor(type), cellSizeFor(type));
                label.setCursor(Cursor.HAND);

                final int fRow = row;
                final int fCol = col;
                label.setOnMouseClicked(e -> handleCellClick(fRow, fCol));

                cellLabels[row][col] = label;
                updateCellAppearance(row, col, fontSize);

                boxGrid.add(label, c, r);
            }
        }
        return boxGrid;
    }

    private void updateCellAppearance(int row, int col, double fontSize) {
        Label label = cellLabels[row][col];
        int value = puzzle.getValue(row, col);
        boolean mutable = puzzle.isSlotMutable(row, col);

        label.setText(ValueFormatter.getGuiRepresentationOf(value));

        String bgColor = determineBgColor(row, col, value);
        String style = "-fx-border-color: " + CELL_BORDER + "; -fx-border-width: 0.5; -fx-background-color: " + bgColor + ";";

        if (value != Puzzle.NO_VALUE) {
            if (!mutable) {
                label.setFont(Font.font("System", FontWeight.BOLD, fontSize));
                style += " -fx-text-fill: " + GIVEN_COLOR + ";";
            } else {
                label.setFont(Font.font("System", FontWeight.NORMAL, fontSize));
                style += " -fx-text-fill: " + PLAYER_COLOR + ";";
            }
        } else {
            label.setFont(Font.font("System", FontWeight.NORMAL, fontSize));
            style += " -fx-text-fill: " + PLAYER_COLOR + ";";
        }

        label.setStyle(style);
    }

    private String determineBgColor(int row, int col, int value) {
        if (selectedRow < 0 || selectedCol < 0) return "white";

        if (row == selectedRow && col == selectedCol) {
            return SELECTED_BG;
        }

        int selectedValue = puzzle.getValue(selectedRow, selectedCol);
        if (value != Puzzle.NO_VALUE && selectedValue != Puzzle.NO_VALUE && value == selectedValue) {
            return SAME_NUMBER_BG;
        }

        if (row == selectedRow || col == selectedCol) {
            return HIGHLIGHT_BG;
        }

        PuzzleType type = puzzle.getPuzzleType();
        int selBoxRow = selectedRow / type.getBoxHeight();
        int selBoxCol = selectedCol / type.getBoxWidth();
        int cellBoxRow = row / type.getBoxHeight();
        int cellBoxCol = col / type.getBoxWidth();
        if (selBoxRow == cellBoxRow && selBoxCol == cellBoxCol) {
            return HIGHLIGHT_BG;
        }

        return "white";
    }

    private void handleCellClick(int row, int col) {
        selectedRow = row;
        selectedCol = col;
        refresh();
    }

    private double fontSizeFor(PuzzleType type) {
        return switch (type) {
            case MINI_SUDOKU -> 24;
            case SUDOKU -> 22;
            case BIG_SUDOKU -> 16;
            case MAXI_SUDOKU -> 14;
        };
    }

    private double cellSizeFor(PuzzleType type) {
        return switch (type) {
            case MINI_SUDOKU -> 52;
            case SUDOKU -> 48;
            case BIG_SUDOKU -> 38;
            case MAXI_SUDOKU -> 32;
        };
    }
}
