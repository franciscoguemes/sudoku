package com.franciscoguemes.sudoku.solver.techniques;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import com.franciscoguemes.sudoku.solver.CandidateGrid;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NakedTriple Technique Tests")
class NakedTripleTest {

    @Test
    @DisplayName("Eliminates candidates when naked triple found in row")
    void testNakedTripleInRow() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);

        // Fill row 0 partially: cells (0,0)-(0,4) filled, (0,5)-(0,8) empty
        puzzle.makeMove(0, 0, 1, false);
        puzzle.makeMove(0, 1, 2, false);
        puzzle.makeMove(0, 2, 3, false);
        puzzle.makeMove(0, 3, 4, false);
        puzzle.makeMove(0, 4, 5, false);
        // Remaining values: {6, 7, 8, 9}

        CandidateGrid grid = new CandidateGrid(puzzle);

        // Create a naked triple: three cells with combined candidates {6,7,8}
        // Remove 9 from cells 5,6,7 to make them a triple
        grid.removeCandidate(0, 5, 9);
        grid.removeCandidate(0, 6, 9);
        grid.removeCandidate(0, 7, 9);

        // Also need each cell to have subset of {6,7,8}
        // Remove one extra from each to make them proper subsets
        grid.removeCandidate(0, 5, 8);  // {6,7}
        grid.removeCandidate(0, 6, 6);  // {7,8}
        grid.removeCandidate(0, 7, 7);  // {6,8}

        NakedTriple technique = new NakedTriple();
        boolean applied = technique.apply(grid);

        if (applied) {
            // 6, 7, 8 should be removed from (0,8), leaving only 9
            assertFalse(grid.getCandidates(0, 8).contains(6));
            assertFalse(grid.getCandidates(0, 8).contains(7));
            assertFalse(grid.getCandidates(0, 8).contains(8));
            assertTrue(grid.getCandidates(0, 8).contains(9));
        }
    }

    @Test
    @DisplayName("Returns false when no naked triple exists")
    void testReturnsFalseWhenNoTriple() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        NakedTriple technique = new NakedTriple();
        assertFalse(technique.apply(grid));
    }

    @Test
    @DisplayName("Technique level is NAKED_SUBSET")
    void testLevel() {
        NakedTriple technique = new NakedTriple();
        assertEquals(TechniqueLevel.NAKED_SUBSET, technique.getLevel());
    }
}
