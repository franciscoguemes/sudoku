package com.franciscoguemes.sudoku.model;

public class Puzzle {

    private static final int NO_VALUE = 0;

    protected int [][] board;
    // Table to determine if a slot is mutable
    protected boolean [][] mutable;
    private final PuzzleType puzzleType;


    public Puzzle(PuzzleType puzzleType){
        this.puzzleType = puzzleType;
        this.board = new int[puzzleType.getRows()][puzzleType.getColumns()];
        this.mutable = new boolean[puzzleType.getRows()][puzzleType.getColumns()];
        initializeBoard();
        initializeMutableSlots();
    }

    public Puzzle(Puzzle puzzle) {
        this.puzzleType = puzzle.getPuzzleType();
        copyBoard(puzzle);
    }

    private void copyBoard(Puzzle puzzle) {
        final int ROWS = puzzleType.getRows();
        final int COLUMNS = puzzleType.getColumns();
        this.board = new int[ROWS][COLUMNS];
        for(int r = 0;r < ROWS;r++) {
            for(int c = 0;c < COLUMNS;c++) {
                board[r][c] = puzzle.board[r][c];
            }
        }
        this.mutable = new boolean[ROWS][COLUMNS];
        for(int r = 0;r < ROWS;r++) {
            for(int c = 0;c < COLUMNS;c++) {
                this.mutable[r][c] = puzzle.mutable[r][c];
            }
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
        if(this.inRange(row,col)) {
            if(!this.numInCol(col,value) && !this.numInRow(row,value) && !this.numInBox(row,col,value)) {
                return true;
            }
        }
        return false;
    }

    public boolean numInCol(int col,int value) {
        final int ROWS = puzzleType.getRows();
        final int COLUMNS = puzzleType.getColumns();
        if(col < COLUMNS) {
            for(int row=0;row < ROWS;row++) {
                if(this.board[row][col]==value) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean numInRow(int row,int value) {
        final int ROWS = puzzleType.getRows();
        final int COLUMNS = puzzleType.getColumns();
        if(row < ROWS) {
            for(int col=0;col < COLUMNS;col++) {
                if(this.board[row][col]==value) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean numInBox(int row,int col,int value) {
        final int BOXHEIGHT = puzzleType.getBoxHeight();
        final int BOXWIDTH = puzzleType.getBoxWidth();
        if(this.inRange(row, col)) {
            int boxRow = row / BOXHEIGHT;
            int boxCol = col / BOXWIDTH;

            int startingRow = (boxRow*BOXHEIGHT);
            int startingCol = (boxCol*BOXWIDTH);

            for(int r = startingRow;r <= (startingRow+BOXHEIGHT)-1;r++) {
                for(int c = startingCol;c <= (startingCol+BOXWIDTH)-1;c++) {
                    if(this.board[r][c]==value) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isSlotAvailable(int row,int col) {
        return (this.inRange(row,col) && this.board[row][col]==NO_VALUE && this.isSlotMutable(row, col));
    }

    public boolean isSlotMutable(int row,int col) {
        return this.mutable[row][col];
    }

    public int getValue(int row,int col) {
        if(this.inRange(row,col)) {
            return this.board[row][col];
        }
        return NO_VALUE;
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

    public boolean inRange(int row,int col) {
        final int ROWS = puzzleType.getRows();
        final int COLUMNS = puzzleType.getColumns();
        return row < ROWS && col < COLUMNS && row >= 0 && col >= 0;
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

    private void initializeBoard() {
        final int ROWS = puzzleType.getRows();
        final int COLUMNS = puzzleType.getColumns();
        for(int row = 0;row < ROWS;row++) {
            for(int col = 0;col < COLUMNS;col++) {
                this.board[row][col] = NO_VALUE;
            }
        }
    }

    private void initializeMutableSlots() {
        final int ROWS = puzzleType.getRows();
        final int COLUMNS = puzzleType.getColumns();
        for(int row = 0;row < ROWS;row++) {
            for(int col = 0;col < COLUMNS;col++) {
                this.mutable[row][col] = true;
            }
        }
    }
}