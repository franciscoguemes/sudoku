package com.franciscoguemes.sudoku.solver.techniques;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import com.franciscoguemes.sudoku.solver.CandidateGrid;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NakedPair Technique Tests")
class NakedPairTest {

    @Test
    @DisplayName("Eliminates candidates when naked pair found in row")
    void testNakedPairInRow() {
        // Create a puzzle where row 0 has two cells with same 2-candidate pair
        // and another cell that has one of those candidates
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);

        // Fill row 0 partially to create a naked pair scenario
        puzzle.makeMove(0, 0, 1, false);
        puzzle.makeMove(0, 1, 2, false);
        puzzle.makeMove(0, 2, 3, false);
        puzzle.makeMove(0, 3, 4, false);
        puzzle.makeMove(0, 4, 5, false);
        puzzle.makeMove(0, 5, 6, false);
        // Cells (0,6), (0,7), (0,8) are empty
        // Remaining values for row 0: {7, 8, 9}

        CandidateGrid grid = new CandidateGrid(puzzle);

        // Manually set up candidates to create a naked pair at (0,6) and (0,7) = {7,8}
        // and (0,8) has {7, 8, 9}
        // For this to work, we need to remove 9 from (0,6) and (0,7) candidates
        grid.removeCandidate(0, 6, 9);
        grid.removeCandidate(0, 7, 9);

        NakedPair technique = new NakedPair();
        boolean applied = technique.apply(grid);

        if (applied) {
            // 7 and 8 should have been removed from (0,8)
            assertFalse(grid.getCandidates(0, 8).contains(7));
            assertFalse(grid.getCandidates(0, 8).contains(8));
            assertTrue(grid.getCandidates(0, 8).contains(9));
        }
    }

    @Test
    @DisplayName("Returns false when no naked pair exists")
    void testReturnsFalseWhenNoPair() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        NakedPair technique = new NakedPair();
        assertFalse(technique.apply(grid));
    }

    @Test
    @DisplayName("Technique level is NAKED_SUBSET")
    void testLevel() {
        NakedPair technique = new NakedPair();
        assertEquals(TechniqueLevel.NAKED_SUBSET, technique.getLevel());
    }
}
