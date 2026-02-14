package com.franciscoguemes.sudoku.solver;

import com.franciscoguemes.sudoku.model.Puzzle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Solves Sudoku puzzles using a recursive backtracking (depth-first search) algorithm.
 * For each empty cell, tries values 1 through N, validates against row/column/box constraints,
 * and backtracks when no valid value exists.
 */
public class BacktrackingSolver implements SudokuSolver {

    private static final Logger LOG = LoggerFactory.getLogger(BacktrackingSolver.class);

    @Override
    public boolean solve(Puzzle puzzle) {
        return backtrackSudokuSolver(0, 0, puzzle);
    }

    /**
     * Solves the sudoku puzzle using backtracking.
     * Pre-cond: r = 0, c = 0
     * Post-cond: solved puzzle
     *
     * @param r      the current row
     * @param c      the current column
     * @param puzzle the puzzle to solve
     * @return true if the puzzle was solved, false otherwise
     */
    private boolean backtrackSudokuSolver(int r, int c, Puzzle puzzle) {
        if (!puzzle.inRange(r, c)) {
            return false;
        }

        int[] possibleValues = puzzle.getPuzzleType().getPossibleValuesInPuzzle();

        if (puzzle.isSlotAvailable(r, c)) {
            for (int i = 0; i < possibleValues.length; i++) {
                if (puzzle.isValidMove(r, c, possibleValues[i])) {
                    LOG.trace("Trying value {} at [{},{}]", possibleValues[i], r, c);

                    puzzle.makeMove(r, c, possibleValues[i], true);

                    if (puzzle.boardFull()) {
                        return true;
                    }

                    if (puzzle.isLastRow(r)) {
                        if (backtrackSudokuSolver(0, c + 1, puzzle)) return true;
                    } else {
                        if (backtrackSudokuSolver(r + 1, c, puzzle)) return true;
                    }
                }
            }
        } else {
            if (puzzle.isLastRow(r)) {
                return backtrackSudokuSolver(0, c + 1, puzzle);
            } else {
                return backtrackSudokuSolver(r + 1, c, puzzle);
            }
        }

        puzzle.makeSlotEmpty(r, c);
        return false;
    }

}
