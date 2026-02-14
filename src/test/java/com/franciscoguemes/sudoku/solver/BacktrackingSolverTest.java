package com.franciscoguemes.sudoku.solver;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BacktrackingSolver Tests")
class BacktrackingSolverTest {

    private BacktrackingSolver solver;

    @BeforeEach
    void setUp() {
        solver = new BacktrackingSolver();
    }

    @Test
    @DisplayName("BacktrackingSolver implements SudokuSolver")
    void testImplementsSudokuSolver() {
        assertInstanceOf(SudokuSolver.class, solver);
    }

    @Test
    @DisplayName("Solves an empty 9x9 puzzle")
    void testSolvesEmptyPuzzle() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        boolean solved = solver.solve(puzzle);

        assertTrue(solved, "Should solve an empty 9x9 puzzle");
        assertTrue(puzzle.boardFull(), "Solved puzzle should have all cells filled");
        assertAllConstraintsSatisfied(puzzle);
    }

    @Test
    @DisplayName("Solves an empty 6x6 Mini Sudoku puzzle")
    void testSolvesEmptyMiniPuzzle() {
        Puzzle puzzle = new Puzzle(PuzzleType.MINI_SUDOKU);
        boolean solved = solver.solve(puzzle);

        assertTrue(solved, "Should solve an empty 6x6 puzzle");
        assertTrue(puzzle.boardFull(), "Solved puzzle should have all cells filled");
        assertAllConstraintsSatisfied(puzzle);
    }

    @Test
    @DisplayName("Solves a partial 9x9 puzzle")
    void testSolvesPartialPuzzle() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        // Place a few valid values
        puzzle.makeMove(0, 0, 5, false);
        puzzle.makeMove(0, 1, 3, false);
        puzzle.makeMove(1, 0, 6, false);

        boolean solved = solver.solve(puzzle);

        assertTrue(solved, "Should solve a partial puzzle");
        assertTrue(puzzle.boardFull(), "Solved puzzle should have all cells filled");
        assertEquals(5, puzzle.getValue(0, 0), "Given value should be preserved");
        assertEquals(3, puzzle.getValue(0, 1), "Given value should be preserved");
        assertEquals(6, puzzle.getValue(1, 0), "Given value should be preserved");
        assertAllConstraintsSatisfied(puzzle);
    }

    @Test
    @DisplayName("countSolutions returns 1 for a puzzle with a unique solution")
    void testCountSolutionsUnique() {
        // Generate a full solution, then remove one cell — still unique
        Puzzle puzzle = new Puzzle(PuzzleType.MINI_SUDOKU);
        solver.solve(puzzle);

        int savedValue = puzzle.getValue(0, 0);
        puzzle.makeSlotEmpty(0, 0);

        int solutions = solver.countSolutions(puzzle, 10);
        assertEquals(1, solutions, "Removing one cell from a complete board should still yield a unique solution");
    }

    @Test
    @DisplayName("countSolutions respects maxCount limit")
    void testCountSolutionsRespectsMaxCount() {
        // An empty mini sudoku has many solutions; countSolutions should stop at maxCount
        Puzzle puzzle = new Puzzle(PuzzleType.MINI_SUDOKU);
        int solutions = solver.countSolutions(puzzle, 2);

        assertEquals(2, solutions, "Should stop counting at maxCount");
    }

    @Test
    @DisplayName("countSolutions does not modify the original puzzle")
    void testCountSolutionsDoesNotModifyPuzzle() {
        Puzzle puzzle = new Puzzle(PuzzleType.MINI_SUDOKU);
        // Place one value
        puzzle.makeMove(0, 0, 1, false);

        solver.countSolutions(puzzle, 5);

        // Verify original puzzle is unchanged
        assertEquals(1, puzzle.getValue(0, 0), "Original puzzle should not be modified");
        assertFalse(puzzle.boardFull(), "Original puzzle should remain incomplete");
    }

    // Helper: verify all Sudoku constraints are satisfied
    private void assertAllConstraintsSatisfied(Puzzle puzzle) {
        PuzzleType type = puzzle.getPuzzleType();
        for (int r = 0; r < type.getRows(); r++) {
            for (int c = 0; c < type.getColumns(); c++) {
                int value = puzzle.getValue(r, c);
                assertTrue(value >= type.getMinValue() && value <= type.getMaxValue(),
                        "Cell [" + r + "," + c + "] has invalid value: " + value);
                assertNoDuplicateInRow(puzzle, r, value, c);
                assertNoDuplicateInColumn(puzzle, c, value, r);
                assertNoDuplicateInBox(puzzle, r, c, value);
            }
        }
    }

    private void assertNoDuplicateInRow(Puzzle puzzle, int row, int value, int excludeCol) {
        int count = 0;
        for (int c = 0; c < puzzle.getPuzzleType().getColumns(); c++) {
            if (puzzle.getValue(row, c) == value) count++;
        }
        assertEquals(1, count, "Value " + value + " should appear exactly once in row " + row);
    }

    private void assertNoDuplicateInColumn(Puzzle puzzle, int col, int value, int excludeRow) {
        int count = 0;
        for (int r = 0; r < puzzle.getPuzzleType().getRows(); r++) {
            if (puzzle.getValue(r, col) == value) count++;
        }
        assertEquals(1, count, "Value " + value + " should appear exactly once in column " + col);
    }

    private void assertNoDuplicateInBox(Puzzle puzzle, int row, int col, int value) {
        int boxHeight = puzzle.getPuzzleType().getBoxHeight();
        int boxWidth = puzzle.getPuzzleType().getBoxWidth();
        int startRow = (row / boxHeight) * boxHeight;
        int startCol = (col / boxWidth) * boxWidth;

        int count = 0;
        for (int r = startRow; r < startRow + boxHeight; r++) {
            for (int c = startCol; c < startCol + boxWidth; c++) {
                if (puzzle.getValue(r, c) == value) count++;
            }
        }
        assertEquals(1, count, "Value " + value + " should appear exactly once in box at (" + row + "," + col + ")");
    }
}
