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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class GameGridPane extends StackPane {

    private static final String SELECTED_BG = "#BBDEFB";
    private static final String HIGHLIGHT_BG = "#E3F2FD";
    private static final String SAME_NUMBER_BG = "#C5CAE9";
    private static final String CELL_BORDER = "#B0BEC5";
    private static final String BOX_BORDER = "#37474F";
    private static final String GIVEN_COLOR = "#1A237E";
    private static final String PLAYER_COLOR = "#1565C0";
    private static final String WRONG_COLOR = "#D32F2F";
    private static final String NOTES_COLOR = "#5C6BC0";

    private final GridPane outerGrid = new GridPane();
    private Puzzle puzzle;
    private Puzzle solution;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private StackPane[][] cellContainers;
    private Label[][] valueLabels;
    private GridPane[][] notesGridPanes;
    private Label[][][] noteLabels;
    private int[][] wrongValues;
    private Set<Integer>[][] notes;
    private boolean notesMode;
    private Runnable onWrongMove;
    private final Deque<UndoEntry> undoStack = new ArrayDeque<>();

    public GameGridPane() {
        setAlignment(Pos.CENTER);
        getChildren().add(outerGrid);
        outerGrid.setAlignment(Pos.CENTER);
    }

    public void displayPuzzle(Puzzle puzzle, Puzzle solution) {
        this.puzzle = puzzle;
        this.solution = solution;
        this.selectedRow = 0;
        this.selectedCol = 0;
        int rows = puzzle.getPuzzleType().getRows();
        int cols = puzzle.getPuzzleType().getColumns();
        this.wrongValues = new int[rows][cols];
        this.notes = createNotesArray(rows, cols);
        this.undoStack.clear();
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

        if (notesMode) {
            handleNotesInput(value);
            return;
        }

        // Capture previous state for undo
        undoStack.push(new UndoEntry.PlaceValue(
                selectedRow, selectedCol,
                puzzle.getValue(selectedRow, selectedCol),
                wrongValues[selectedRow][selectedCol],
                new HashSet<>(notes[selectedRow][selectedCol])));

        // Clear any existing wrong value in this cell
        wrongValues[selectedRow][selectedCol] = 0;

        // Clear notes for this cell (value replaces notes)
        notes[selectedRow][selectedCol].clear();

        // Clear any existing correct value in the puzzle
        if (puzzle.getValue(selectedRow, selectedCol) != Puzzle.NO_VALUE) {
            puzzle.makeSlotEmpty(selectedRow, selectedCol);
        }

        if (value == Puzzle.NO_VALUE) {
            refresh();
            return;
        }

        int correctValue = solution.getValue(selectedRow, selectedCol);
        if (value == correctValue) {
            puzzle.makeMove(selectedRow, selectedCol, value, true);
        } else {
            wrongValues[selectedRow][selectedCol] = value;
            if (onWrongMove != null) {
                onWrongMove.run();
            }
        }

        refresh();
    }

    private void handleNotesInput(int value) {
        if (value == Puzzle.NO_VALUE) return;
        // Notes only apply to empty cells (no puzzle value and no wrong value)
        if (puzzle.getValue(selectedRow, selectedCol) != Puzzle.NO_VALUE) return;
        if (wrongValues[selectedRow][selectedCol] != 0) return;

        Set<Integer> cellNotes = notes[selectedRow][selectedCol];
        boolean wasAdded;
        if (cellNotes.contains(value)) {
            cellNotes.remove(value);
            wasAdded = false;
        } else {
            cellNotes.add(value);
            wasAdded = true;
        }

        undoStack.push(new UndoEntry.NoteToggle(selectedRow, selectedCol, value, wasAdded));

        refresh();
    }

    public void setNotesMode(boolean notesMode) {
        this.notesMode = notesMode;
    }

    public boolean isNotesMode() {
        return notesMode;
    }

    public void setOnWrongMove(Runnable handler) {
        this.onWrongMove = handler;
    }

    public void undo() {
        if (undoStack.isEmpty()) return;

        UndoEntry entry = undoStack.pop();
        int row = entry.row();
        int col = entry.col();

        switch (entry) {
            case UndoEntry.PlaceValue pv -> {
                puzzle.makeSlotEmpty(row, col);
                wrongValues[row][col] = 0;
                notes[row][col].clear();

                if (pv.prevPuzzleValue() != Puzzle.NO_VALUE) {
                    puzzle.makeMove(row, col, pv.prevPuzzleValue(), true);
                }
                wrongValues[row][col] = pv.prevWrongValue();
                notes[row][col].addAll(pv.prevNotes());
            }
            case UndoEntry.NoteToggle nt -> {
                if (nt.wasAdded()) {
                    notes[row][col].remove(nt.noteValue());
                } else {
                    notes[row][col].add(nt.noteValue());
                }
            }
        }

        refresh();
    }

    public void moveSelection(int deltaRow, int deltaCol) {
        if (puzzle == null) return;
        PuzzleType type = puzzle.getPuzzleType();
        int newRow = Math.clamp(selectedRow + deltaRow, 0, type.getRows() - 1);
        int newCol = Math.clamp(selectedCol + deltaCol, 0, type.getColumns() - 1);
        selectedRow = newRow;
        selectedCol = newCol;
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
        int totalValues = type.getMaxValue() - type.getMinValue() + 1;
        double fontSize = fontSizeFor(type);

        cellContainers = new StackPane[type.getRows()][type.getColumns()];
        valueLabels = new Label[type.getRows()][type.getColumns()];
        notesGridPanes = new GridPane[type.getRows()][type.getColumns()];
        noteLabels = new Label[type.getRows()][type.getColumns()][totalValues];

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
                double cellSize = cellSizeFor(type);

                Label valueLabel = new Label();
                valueLabel.setAlignment(Pos.CENTER);
                valueLabel.setPrefSize(cellSize, cellSize);

                GridPane notesGrid = createNotesGrid(type, row, col, cellSize);

                StackPane cell = new StackPane();
                cell.setPrefSize(cellSize, cellSize);
                cell.setCursor(Cursor.HAND);
                cell.getChildren().addAll(notesGrid, valueLabel);

                final int fRow = row;
                final int fCol = col;
                cell.setOnMouseClicked(e -> handleCellClick(fRow, fCol));

                cellContainers[row][col] = cell;
                valueLabels[row][col] = valueLabel;
                notesGridPanes[row][col] = notesGrid;

                updateCellAppearance(row, col, fontSize);

                boxGrid.add(cell, c, r);
            }
        }
        return boxGrid;
    }

    private GridPane createNotesGrid(PuzzleType type, int row, int col, double cellSize) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        int noteCols = notesColumnsFor(type);
        int min = type.getMinValue();
        int max = type.getMaxValue();
        int totalValues = max - min + 1;
        int noteRows = (int) Math.ceil((double) totalValues / noteCols);

        double noteWidth = cellSize / noteCols;
        double noteHeight = cellSize / noteRows;
        double noteFontSize = notesFontSizeFor(type);

        for (int v = min; v <= max; v++) {
            int index = v - min;
            int gc = index % noteCols;
            int gr = index / noteCols;

            Label noteLabel = new Label();
            noteLabel.setAlignment(Pos.CENTER);
            noteLabel.setPrefSize(noteWidth, noteHeight);
            noteLabel.setFont(Font.font("System", FontWeight.NORMAL, noteFontSize));
            noteLabel.setStyle("-fx-text-fill: " + NOTES_COLOR + ";");

            noteLabels[row][col][index] = noteLabel;
            grid.add(noteLabel, gc, gr);
        }

        return grid;
    }

    private void updateCellAppearance(int row, int col, double fontSize) {
        Label valueLabel = valueLabels[row][col];
        GridPane notesGrid = notesGridPanes[row][col];
        int puzzleValue = puzzle.getValue(row, col);
        int wrongValue = wrongValues[row][col];
        boolean mutable = puzzle.isSlotMutable(row, col);
        boolean isWrong = wrongValue != 0;
        boolean hasValue = puzzleValue != Puzzle.NO_VALUE || isWrong;
        Set<Integer> cellNotes = notes[row][col];
        boolean hasNotes = !hasValue && !cellNotes.isEmpty();

        int displayValue = isWrong ? wrongValue : puzzleValue;

        if (hasNotes) {
            valueLabel.setVisible(false);
            notesGrid.setVisible(true);
            updateNotesDisplay(row, col);
        } else {
            valueLabel.setVisible(true);
            notesGrid.setVisible(false);
            valueLabel.setText(ValueFormatter.getGuiRepresentationOf(displayValue));
        }

        // Background applied to cell container
        String bgColor = determineBgColor(row, col, displayValue);
        cellContainers[row][col].setStyle(
                "-fx-border-color: " + CELL_BORDER + "; -fx-border-width: 0.5; -fx-background-color: " + bgColor + ";");

        // Value label text styling
        if (isWrong) {
            valueLabel.setFont(Font.font("System", FontWeight.NORMAL, fontSize));
            valueLabel.setStyle("-fx-text-fill: " + WRONG_COLOR + ";");
        } else if (displayValue != Puzzle.NO_VALUE) {
            if (!mutable) {
                valueLabel.setFont(Font.font("System", FontWeight.BOLD, fontSize));
                valueLabel.setStyle("-fx-text-fill: " + GIVEN_COLOR + ";");
            } else {
                valueLabel.setFont(Font.font("System", FontWeight.NORMAL, fontSize));
                valueLabel.setStyle("-fx-text-fill: " + PLAYER_COLOR + ";");
            }
        } else {
            valueLabel.setFont(Font.font("System", FontWeight.NORMAL, fontSize));
            valueLabel.setStyle("-fx-text-fill: " + PLAYER_COLOR + ";");
        }
    }

    private void updateNotesDisplay(int row, int col) {
        Set<Integer> cellNotes = notes[row][col];
        PuzzleType type = puzzle.getPuzzleType();
        int min = type.getMinValue();
        int max = type.getMaxValue();

        for (int v = min; v <= max; v++) {
            int index = v - min;
            Label noteLabel = noteLabels[row][col][index];
            if (cellNotes.contains(v)) {
                noteLabel.setText(ValueFormatter.getGuiRepresentationOf(v));
            } else {
                noteLabel.setText("");
            }
        }
    }

    private String determineBgColor(int row, int col, int value) {
        if (selectedRow < 0 || selectedCol < 0) return "white";

        if (row == selectedRow && col == selectedCol) {
            return SELECTED_BG;
        }

        int selectedDisplayValue = wrongValues[selectedRow][selectedCol] != 0
                ? wrongValues[selectedRow][selectedCol]
                : puzzle.getValue(selectedRow, selectedCol);
        if (value != Puzzle.NO_VALUE && selectedDisplayValue != Puzzle.NO_VALUE && value == selectedDisplayValue) {
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

    @SuppressWarnings("unchecked")
    private static Set<Integer>[][] createNotesArray(int rows, int cols) {
        Set<Integer>[][] result = new HashSet[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                result[r][c] = new HashSet<>();
            }
        }
        return result;
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

    private int notesColumnsFor(PuzzleType type) {
        return switch (type) {
            case MINI_SUDOKU, SUDOKU -> 3;
            case BIG_SUDOKU, MAXI_SUDOKU -> 4;
        };
    }

    private double notesFontSizeFor(PuzzleType type) {
        return switch (type) {
            case MINI_SUDOKU -> 8;
            case SUDOKU -> 8;
            case BIG_SUDOKU -> 6;
            case MAXI_SUDOKU -> 5;
        };
    }

    private sealed interface UndoEntry {
        int row();
        int col();

        record PlaceValue(int row, int col, int prevPuzzleValue, int prevWrongValue,
                          Set<Integer> prevNotes) implements UndoEntry {}

        record NoteToggle(int row, int col, int noteValue, boolean wasAdded) implements UndoEntry {}
    }
}
