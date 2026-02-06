package com.franciscoguemes.sudoku.model;

public enum PuzzleType {

    MINI_SUDOKU(6,6,3,2,1,6,"6 by 6 game"),
    SUDOKU(9,9,3,3,1,9,"9 by 9 game"),
    BIG_SUDOKU(12,12,4,3,1,12,"12 by 12 game"),
    MAXI_SUDOKU(16,16,4,4,1,16,"16 by 16 game");

    private final int rows;
    private final int columns;
    private final int boxWidth;
    private final int boxHeight;
    private final int minValue;
    private final int maxValue;
    private final String description;

    private PuzzleType(int rows,int columns,int boxWidth,int boxHeight,int minValue, int maxValue,String desc) {
        this.rows = rows;
        this.columns = columns;
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.description = desc;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getBoxWidth() {
        return boxWidth;
    }

    public int getBoxHeight() {
        return boxHeight;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public String getDescription() {
        return description;
    }

}
