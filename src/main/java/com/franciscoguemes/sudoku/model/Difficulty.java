package com.franciscoguemes.sudoku.model;

public enum Difficulty {
    EASY(0.50, "Easy"),
    MEDIUM(0.41, "Medium"),
    HARD(0.34, "Hard"),
    EXPERT(0.29, "Expert"),
    MASTER(0.25, "Master"),
    EXTREME(0.21, "Extreme");

    private final double fillRatio;
    private final String description;

    Difficulty(double fillRatio, String description) {
        this.fillRatio = fillRatio;
        this.description = description;
    }

    public double getFillRatio() {
        return fillRatio;
    }

    public String getDescription() {
        return description;
    }

    public int getTargetClueCount(PuzzleType type) {
        return (int) (fillRatio * type.getRows() * type.getColumns());
    }
}
