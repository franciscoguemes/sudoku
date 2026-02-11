package com.franciscoguemes.sudoku.gui.editor;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import com.franciscoguemes.sudoku.util.ValueFormatter;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SudokuGridPane extends StackPane {

    private final GridPane outerGrid = new GridPane();

    public SudokuGridPane() {
        setAlignment(Pos.CENTER);
        getChildren().add(outerGrid);
        outerGrid.setAlignment(Pos.CENTER);
    }

    public void displayPuzzle(Puzzle puzzle) {
        outerGrid.getChildren().clear();

        PuzzleType type = puzzle.getPuzzleType();
        int boxWidth = type.getBoxWidth();
        int boxHeight = type.getBoxHeight();
        int boxCols = type.getColumns() / boxWidth;
        int boxRows = type.getRows() / boxHeight;
        double fontSize = fontSizeFor(type);

        for (int br = 0; br < boxRows; br++) {
            for (int bc = 0; bc < boxCols; bc++) {
                GridPane boxGrid = createBoxGrid(puzzle, type, br, bc, fontSize);
                outerGrid.add(boxGrid, bc, br);
            }
        }
    }

    private GridPane createBoxGrid(Puzzle puzzle, PuzzleType type,
                                   int boxRow, int boxCol, double fontSize) {
        int boxWidth = type.getBoxWidth();
        int boxHeight = type.getBoxHeight();
        int startRow = boxRow * boxHeight;
        int startCol = boxCol * boxWidth;

        GridPane boxGrid = new GridPane();
        boxGrid.setStyle("-fx-border-color: black; -fx-border-width: 2;");

        for (int r = 0; r < boxHeight; r++) {
            for (int c = 0; c < boxWidth; c++) {
                int row = startRow + r;
                int col = startCol + c;
                int value = puzzle.getValue(row, col);
                boolean mutable = puzzle.isSlotMutable(row, col);

                Label label = new Label(ValueFormatter.getGuiRepresentationOf(value));
                label.setAlignment(Pos.CENTER);
                label.setPrefSize(cellSizeFor(type), cellSizeFor(type));
                label.setStyle("-fx-border-color: #cccccc; -fx-border-width: 0.5;");

                if (value != Puzzle.NO_VALUE) {
                    if (mutable) {
                        label.setFont(Font.font("System", FontWeight.NORMAL, fontSize));
                        label.setStyle(label.getStyle() + " -fx-text-fill: #0055cc;");
                    } else {
                        label.setFont(Font.font("System", FontWeight.BOLD, fontSize));
                        label.setStyle(label.getStyle() + " -fx-text-fill: black;");
                    }
                } else {
                    label.setFont(Font.font("System", FontWeight.NORMAL, fontSize));
                }

                boxGrid.add(label, c, r);
            }
        }
        return boxGrid;
    }

    private double fontSizeFor(PuzzleType type) {
        return switch (type) {
            case MINI_SUDOKU -> 22;
            case SUDOKU -> 20;
            case BIG_SUDOKU -> 16;
            case MAXI_SUDOKU -> 14;
        };
    }

    private double cellSizeFor(PuzzleType type) {
        return switch (type) {
            case MINI_SUDOKU -> 48;
            case SUDOKU -> 44;
            case BIG_SUDOKU -> 38;
            case MAXI_SUDOKU -> 32;
        };
    }
}
