package com.franciscoguemes.sudoku.solver;

import com.franciscoguemes.sudoku.model.Puzzle;

/**
 * Strategy interface for solving Sudoku puzzles.
 * Implementations solve the given puzzle in-place and return whether a solution was found.
 */
public interface SudokuSolver {

    /**
     * Solves the given puzzle in-place.
     *
     * @param puzzle the puzzle to solve
     * @return {@code true} if a solution was found, {@code false} otherwise
     */
    boolean solve(Puzzle puzzle);
}
