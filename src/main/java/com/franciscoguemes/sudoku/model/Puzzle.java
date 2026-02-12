package com.franciscoguemes.sudoku.model;

import java.util.Arrays;

public class Puzzle {

    public static final int NO_VALUE = 0;

    protected int [][] board;
    protected boolean [][] mutable;
    private final PuzzleType puzzleType;


    public Puzzle(PuzzleType puzzleType){
        this.puzzleType = puzzleType;
        this.board = new int[puzzleType.getRows()][puzzleType.getColumns()];
        this.mutable = new boolean[puzzleType.getRows()][puzzleType.getColumns()];
        initializeBoard();
        initializeMutableSlots();
    }

    private void initializeBoard() {
        final int ROWS = puzzleType.getRows();
        for (int row = 0; row < ROWS; row++) {
            Arrays.fill(this.board[row], NO_VALUE);
        }
    }

    private void initializeMutableSlots() {
        final int ROWS = puzzleType.getRows();
        for (int row = 0; row < ROWS; row++) {
            Arrays.fill(this.mutable[row], true);
        }
    }

    public Puzzle(Puzzle puzzle) {
        this.puzzleType = puzzle.getPuzzleType();
        copyBoard(puzzle);
    }

    private void copyBoard(Puzzle puzzle) {
        final int ROWS = puzzleType.getRows();
        final int COLUMNS = puzzleType.getColumns();

        this.board = new int[ROWS][COLUMNS];
        this.mutable = new boolean[ROWS][COLUMNS];

        for(int r = 0; r < ROWS; r++) {
            // Bulk copy entire rows
            System.arraycopy(puzzle.board[r], 0, this.board[r], 0, COLUMNS);
            System.arraycopy(puzzle.mutable[r], 0, this.mutable[r], 0, COLUMNS);
        }
    }

    public PuzzleType getPuzzleType(){
        return puzzleType;
    }

    public void makeMove(int row, int col, int value, boolean isMutable) {
        if(this.isValidValue(value) && this.isValidMove(row,col,value) && this.isSlotMutable(row, col)) {
            this.board[row][col] = value;
            this.mutable[row][col] = isMutable;
        }
    }

    public boolean isValidMove(int row,int col,int value) {
        return !this.numInCol(col, value) && !this.numInRow(row, value) && !this.numInBox(row, col, value);
    }

    public boolean numInCol(int col, int value) {
        if (!isValidColumnIndex(col)) return false;
        final int ROWS = puzzleType.getRows();

        for (int row = 0; row < ROWS; row++) {
            if (this.board[row][col] == value) {
                return true;
            }
        }
        return false;
    }

    public boolean numInRow(int row, int value) {
        if (!isValidRowIndex(row)) return false;
        final int COLUMNS = puzzleType.getColumns();

        for (int col = 0; col < COLUMNS; col++) {
            if (this.board[row][col] == value) {
                return true;
            }
        }
        return false;
    }

    public boolean numInBox(int row,int col,int value) {

        final int BOX_HEIGHT = puzzleType.getBoxHeight();
        final int BOX_WIDTH = puzzleType.getBoxWidth();

        int boxRow = row / BOX_HEIGHT;
        int boxCol = col / BOX_WIDTH;

        int startingRow = (boxRow * BOX_HEIGHT);
        int startingCol = (boxCol * BOX_WIDTH);

        for (int r = startingRow; r <= (startingRow + BOX_HEIGHT) - 1; r++) {
            for (int c = startingCol; c <= (startingCol + BOX_WIDTH) - 1; c++) {
                if (this.board[r][c] == value) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSlotAvailable(int row,int col) {
        return (this.board[row][col]==NO_VALUE && this.isSlotMutable(row, col));
    }

    public boolean isSlotMutable(int row,int col) {
        return this.mutable[row][col];
    }

    public int getValue(int row,int col) {
        return this.board[row][col];
    }

    public int [][] getBoard() {
        final int ROWS = puzzleType.getRows();
        final int COLUMNS = puzzleType.getColumns();
        int[][] copy = new int[ROWS][COLUMNS];
        for(int r = 0; r < ROWS; r++) {
            System.arraycopy(this.board[r], 0, copy[r], 0, COLUMNS);
        }
        return copy;
    }

    private boolean isValidValue(int value) {
        return value >= puzzleType.getMinValue() && value <= puzzleType.getMaxValue();
    }

    private boolean isValidRowIndex(int row){
        final int ROWS = puzzleType.getRows();
        return row<ROWS && row>=0;
    }

    private boolean isValidColumnIndex(int col){
        final int COLUMNS = puzzleType.getColumns();
        return col < COLUMNS && col>=0;
    }

    public boolean inRange(int row,int col) {
        return isValidRowIndex(row) && isValidColumnIndex(col);
    }

    public boolean isLastRow(int row){
        return row == this.getPuzzleType().getRows() - 1;
    }

    public boolean boardFull() {
        final int ROWS = puzzleType.getRows();
        final int COLUMNS = puzzleType.getColumns();
        for(int r = 0;r < ROWS;r++) {
            for(int c = 0;c < COLUMNS;c++) {
                if(this.board[r][c] == NO_VALUE) return false;
            }
        }
        return true;
    }

    public void makeSlotEmpty(int row,int col) {
        this.board[row][col] = NO_VALUE;
    }

    @Override
    public String toString() {
        final String newLine = System.lineSeparator();
        final int ROWS = puzzleType.getRows();
        final int COLUMNS = puzzleType.getColumns();
        StringBuilder sb = new StringBuilder();
        sb.append("Game Board:").append(newLine);
        for(int row=0;row < ROWS;row++) {
            for(int col=0;col < COLUMNS;col++) {
                sb.append(String.format("%2d", this.board[row][col])).append(" ");
            }
            sb.append(newLine);
        }
        sb.append(newLine);
        return sb.toString();
    }

}