package com.franciscoguemes.sudoku.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Generator Tests")
class GeneratorTest {

    private Generator generator;

    @BeforeEach
    void setUp() {
        generator = new Generator();
    }

    @Test
    @DisplayName("Generate standard 9x9 Sudoku puzzle")
    void testGenerateStandardSudoku() {
        Puzzle puzzle = generator.generateRandomSudoku(PuzzleType.SUDOKU);

        assertNotNull(puzzle);
        assertEquals(PuzzleType.SUDOKU, puzzle.getPuzzleType());
        assertFalse(puzzle.boardFull());
        assertHasGivenValues(puzzle);
    }

    @Test
    @DisplayName("Generate 6x6 Mini Sudoku puzzle")
    void testGenerateMiniSudoku() {
        Puzzle puzzle = generator.generateRandomSudoku(PuzzleType.MINI_SUDOKU);

        assertNotNull(puzzle);
        assertEquals(PuzzleType.MINI_SUDOKU, puzzle.getPuzzleType());
        assertFalse(puzzle.boardFull());
        assertHasGivenValues(puzzle);
    }

    @Test
    @DisplayName("Generate 12x12 Big Sudoku puzzle")
    void testGenerateBigSudoku() {
        Puzzle puzzle = generator.generateRandomSudoku(PuzzleType.BIG_SUDOKU);

        assertNotNull(puzzle);
        assertEquals(PuzzleType.BIG_SUDOKU, puzzle.getPuzzleType());
        assertFalse(puzzle.boardFull());
        assertHasGivenValues(puzzle);
    }

    @Test
    @DisplayName("Generated puzzle has some given (immutable) values")
    void testGeneratedPuzzleHasGivenValues() {
        Puzzle puzzle = generator.generateRandomSudoku(PuzzleType.SUDOKU);

        int givenCount = 0;
        for(int r = 0; r < puzzle.getPuzzleType().getRows(); r++) {
            for(int c = 0; c < puzzle.getPuzzleType().getColumns(); c++) {
                if(puzzle.getValue(r, c) != 0 && !puzzle.isSlotMutable(r, c)) {
                    givenCount++;
                }
            }
        }

        assertTrue(givenCount > 0, "Generated puzzle should have at least some given values");
    }

    @Test
    @DisplayName("Generated puzzle has some empty slots")
    void testGeneratedPuzzleHasEmptySlots() {
        Puzzle puzzle = generator.generateRandomSudoku(PuzzleType.SUDOKU);

        int emptyCount = 0;
        for(int r = 0; r < puzzle.getPuzzleType().getRows(); r++) {
            for(int c = 0; c < puzzle.getPuzzleType().getColumns(); c++) {
                if(puzzle.getValue(r, c) == 0) {
                    emptyCount++;
                }
            }
        }

        assertTrue(emptyCount > 0, "Generated puzzle should have empty slots for player to fill");
    }

    @Test
    @DisplayName("All given values in generated puzzle are valid")
    void testAllGivenValuesAreValid() {
        Puzzle puzzle = generator.generateRandomSudoku(PuzzleType.SUDOKU);

        for(int r = 0; r < puzzle.getPuzzleType().getRows(); r++) {
            for(int c = 0; c < puzzle.getPuzzleType().getColumns(); c++) {
                int value = puzzle.getValue(r, c);
                if(value != 0) {
                    // Check no duplicates in row
                    assertNoDuplicateInRow(puzzle, r, value, c);
                    // Check no duplicates in column
                    assertNoDuplicateInColumn(puzzle, c, value, r);
                    // Check no duplicates in box
                    assertNoDuplicateInBox(puzzle, r, c, value);
                }
            }
        }
    }

    @RepeatedTest(3)
    @DisplayName("Multiple generations produce different puzzles")
    void testMultipleGenerationsProduceDifferentPuzzles() {
        Puzzle puzzle1 = generator.generateRandomSudoku(PuzzleType.SUDOKU);
        Puzzle puzzle2 = generator.generateRandomSudoku(PuzzleType.SUDOKU);

        boolean isDifferent = false;
        for(int r = 0; r < 9 && !isDifferent; r++) {
            for(int c = 0; c < 9 && !isDifferent; c++) {
                if(puzzle1.getValue(r, c) != puzzle2.getValue(r, c)) {
                    isDifferent = true;
                }
            }
        }

        assertTrue(isDifferent, "Multiple puzzle generations should produce different results");
    }

    @Test
    @DisplayName("Default generation (no difficulty) uses MEDIUM and produces reasonable clue count")
    void testGeneratedPuzzleGivenValueCount() {
        Puzzle puzzle = generator.generateRandomSudoku(PuzzleType.SUDOKU);

        int givenCount = countClues(puzzle);

        // MEDIUM fill ratio 0.41 -> target ~33 clues for 9x9
        // Allow a wide range because uniqueness checking may prevent reaching exact target
        assertTrue(givenCount >= 28 && givenCount <= 45,
            "Generated puzzle should have between 28 and 45 given values for MEDIUM difficulty, but had " + givenCount);
    }

    @Test
    @DisplayName("Given values in generated puzzle are immutable")
    void testGivenValuesAreImmutable() {
        Puzzle puzzle = generator.generateRandomSudoku(PuzzleType.SUDOKU);

        for(int r = 0; r < puzzle.getPuzzleType().getRows(); r++) {
            for(int c = 0; c < puzzle.getPuzzleType().getColumns(); c++) {
                if(puzzle.getValue(r, c) != 0) {
                    assertFalse(puzzle.isSlotMutable(r, c),
                        "Given values at (" + r + "," + c + ") should be immutable");
                }
            }
        }
    }

    @Test
    @DisplayName("Empty slots in generated puzzle are mutable")
    void testEmptySlotsAreMutable() {
        Puzzle puzzle = generator.generateRandomSudoku(PuzzleType.SUDOKU);

        for(int r = 0; r < puzzle.getPuzzleType().getRows(); r++) {
            for(int c = 0; c < puzzle.getPuzzleType().getColumns(); c++) {
                if(puzzle.getValue(r, c) == 0) {
                    assertTrue(puzzle.isSlotMutable(r, c),
                        "Empty slots at (" + r + "," + c + ") should be mutable");
                }
            }
        }
    }

    // --- Difficulty-level tests ---

    @Test
    @DisplayName("EASY difficulty produces 36-50 clues for 9x9")
    void testEasyDifficulty() {
        Puzzle puzzle = generator.generateRandomSudoku(PuzzleType.SUDOKU, Difficulty.EASY);
        int clues = countClues(puzzle);
        assertTrue(clues >= 36 && clues <= 50,
            "EASY should have 36-50 clues, but had " + clues);
    }

    @Test
    @DisplayName("MEDIUM difficulty produces 28-40 clues for 9x9")
    void testMediumDifficulty() {
        Puzzle puzzle = generator.generateRandomSudoku(PuzzleType.SUDOKU, Difficulty.MEDIUM);
        int clues = countClues(puzzle);
        assertTrue(clues >= 28 && clues <= 40,
            "MEDIUM should have 28-40 clues, but had " + clues);
    }

    @Test
    @DisplayName("HARD difficulty produces 23-33 clues for 9x9")
    void testHardDifficulty() {
        Puzzle puzzle = generator.generateRandomSudoku(PuzzleType.SUDOKU, Difficulty.HARD);
        int clues = countClues(puzzle);
        assertTrue(clues >= 23 && clues <= 33,
            "HARD should have 23-33 clues, but had " + clues);
    }

    @Test
    @DisplayName("EXPERT difficulty produces 19-27 clues for 9x9")
    void testExpertDifficulty() {
        Puzzle puzzle = generator.generateRandomSudoku(PuzzleType.SUDOKU, Difficulty.EXPERT);
        int clues = countClues(puzzle);
        assertTrue(clues >= 19 && clues <= 27,
            "EXPERT should have 19-27 clues, but had " + clues);
    }

    @Test
    @DisplayName("MASTER difficulty produces 17-27 clues for 9x9")
    void testMasterDifficulty() {
        Puzzle puzzle = generator.generateRandomSudoku(PuzzleType.SUDOKU, Difficulty.MASTER);
        int clues = countClues(puzzle);
        assertTrue(clues >= 17 && clues <= 27,
            "MASTER should have 17-27 clues, but had " + clues);
    }

    @Test
    @DisplayName("EXTREME difficulty produces 15-25 clues for 9x9")
    void testExtremeDifficulty() {
        Puzzle puzzle = generator.generateRandomSudoku(PuzzleType.SUDOKU, Difficulty.EXTREME);
        int clues = countClues(puzzle);
        assertTrue(clues >= 15 && clues <= 25,
            "EXTREME should have 15-25 clues, but had " + clues);
    }

    @Test
    @DisplayName("Generated puzzle is solvable and has a valid complete solution")
    void testGeneratedPuzzleIsSolvable() {
        Puzzle puzzle = generator.generateRandomSudoku(PuzzleType.SUDOKU, Difficulty.HARD);
        Puzzle solution = new Puzzle(puzzle);
        boolean solved = generator.solve(solution);

        assertTrue(solved, "Generated puzzle should be solvable");
        assertTrue(solution.boardFull(), "Solved puzzle should have all cells filled");

        // Verify all values are valid
        for (int r = 0; r < solution.getPuzzleType().getRows(); r++) {
            for (int c = 0; c < solution.getPuzzleType().getColumns(); c++) {
                int value = solution.getValue(r, c);
                assertTrue(value >= 1 && value <= 9, "All cells should have valid values");
                assertNoDuplicateInRow(solution, r, value, c);
                assertNoDuplicateInColumn(solution, c, value, r);
                assertNoDuplicateInBox(solution, r, c, value);
            }
        }
    }

    @Test
    @DisplayName("Backward-compatible generateRandomSudoku(PuzzleType) still works")
    void testBackwardCompatibility() {
        Puzzle puzzle = generator.generateRandomSudoku(PuzzleType.SUDOKU);

        assertNotNull(puzzle);
        assertEquals(PuzzleType.SUDOKU, puzzle.getPuzzleType());
        assertFalse(puzzle.boardFull());

        int clues = countClues(puzzle);
        assertTrue(clues > 0, "Should have given values");
        assertTrue(clues < 81, "Should have empty cells");
    }

    @Test
    @DisplayName("Difficulty with Mini Sudoku produces appropriate clue count")
    void testDifficultyWithMiniSudoku() {
        Puzzle puzzle = generator.generateRandomSudoku(PuzzleType.MINI_SUDOKU, Difficulty.EASY);
        int clues = countClues(puzzle);
        // EASY fill ratio 0.50 -> target 18 clues for 6x6 (36 cells)
        assertTrue(clues >= 14 && clues <= 22,
            "EASY Mini Sudoku should have 14-22 clues, but had " + clues);
    }

    // Helper methods

    private int countClues(Puzzle puzzle) {
        int count = 0;
        for (int r = 0; r < puzzle.getPuzzleType().getRows(); r++) {
            for (int c = 0; c < puzzle.getPuzzleType().getColumns(); c++) {
                if (puzzle.getValue(r, c) != Puzzle.NO_VALUE) {
                    count++;
                }
            }
        }
        return count;
    }

    private void assertHasGivenValues(Puzzle puzzle) {
        int valueCount = 0;
        for(int r = 0; r < puzzle.getPuzzleType().getRows(); r++) {
            for(int c = 0; c < puzzle.getPuzzleType().getColumns(); c++) {
                if(puzzle.getValue(r, c) != 0) {
                    valueCount++;
                }
            }
        }
        assertTrue(valueCount > 0, "Puzzle should have at least some given values");
    }

    private void assertNoDuplicateInRow(Puzzle puzzle, int row, int value, int excludeCol) {
        int count = 0;
        for(int c = 0; c < puzzle.getPuzzleType().getColumns(); c++) {
            if(puzzle.getValue(row, c) == value) {
                count++;
            }
        }
        assertEquals(1, count, "Value " + value + " should appear exactly once in row " + row);
    }

    private void assertNoDuplicateInColumn(Puzzle puzzle, int col, int value, int excludeRow) {
        int count = 0;
        for(int r = 0; r < puzzle.getPuzzleType().getRows(); r++) {
            if(puzzle.getValue(r, col) == value) {
                count++;
            }
        }
        assertEquals(1, count, "Value " + value + " should appear exactly once in column " + col);
    }

    private void assertNoDuplicateInBox(Puzzle puzzle, int row, int col, int value) {
        int boxHeight = puzzle.getPuzzleType().getBoxHeight();
        int boxWidth = puzzle.getPuzzleType().getBoxWidth();

        int boxRow = row / boxHeight;
        int boxCol = col / boxWidth;

        int startRow = boxRow * boxHeight;
        int startCol = boxCol * boxWidth;

        int count = 0;
        for(int r = startRow; r < startRow + boxHeight; r++) {
            for(int c = startCol; c < startCol + boxWidth; c++) {
                if(puzzle.getValue(r, c) == value) {
                    count++;
                }
            }
        }
        assertEquals(1, count, "Value " + value + " should appear exactly once in box at (" + row + "," + col + ")");
    }
}
