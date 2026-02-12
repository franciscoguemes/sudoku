package com.franciscoguemes.sudoku.solver;

public enum TechniqueLevel {
    NAKED_SINGLE(1),
    HIDDEN_SINGLE(2),
    NAKED_SUBSET(3),
    FISH_AND_REDUCTION(4),
    ADVANCED(5),
    BACKTRACKING(6);

    private final int rank;

    TechniqueLevel(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }
}
