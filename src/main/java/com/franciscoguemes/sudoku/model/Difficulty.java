package com.franciscoguemes.sudoku.model;

import com.franciscoguemes.sudoku.solver.TechniqueLevel;

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

    public TechniqueLevel getTechniqueLevel() {
        return switch (this) {
            case EASY -> TechniqueLevel.NAKED_SINGLE;
            case MEDIUM -> TechniqueLevel.HIDDEN_SINGLE;
            case HARD -> TechniqueLevel.NAKED_SUBSET;
            case EXPERT -> TechniqueLevel.FISH_AND_REDUCTION;
            case MASTER -> TechniqueLevel.ADVANCED;
            case EXTREME -> TechniqueLevel.BACKTRACKING;
        };
    }
}
