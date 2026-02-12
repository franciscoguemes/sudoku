package com.franciscoguemes.sudoku.solver.techniques;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import com.franciscoguemes.sudoku.solver.CandidateGrid;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NakedSingle Technique Tests")
class NakedSingleTest {

    @Test
    @DisplayName("Places value when cell has exactly one candidate")
    void testPlacesValueWhenOnlyOneCandidate() {
        // Build a 9x9 puzzle where cell (0,8) has only one candidate left
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        // Fill row 0 with 8 values, leaving only one candidate for (0,8)
        puzzle.makeMove(0, 0, 1, false);
        puzzle.makeMove(0, 1, 2, false);
        puzzle.makeMove(0, 2, 3, false);
        puzzle.makeMove(0, 3, 4, false);
        puzzle.makeMove(0, 4, 5, false);
        puzzle.makeMove(0, 5, 6, false);
        puzzle.makeMove(0, 6, 7, false);
        puzzle.makeMove(0, 7, 8, false);
        // Cell (0,8) should have only candidate 9

        CandidateGrid grid = new CandidateGrid(puzzle);
        assertEquals(1, grid.getCandidates(0, 8).size());
        assertTrue(grid.getCandidates(0, 8).contains(9));

        NakedSingle technique = new NakedSingle();
        boolean applied = technique.apply(grid);

        assertTrue(applied);
        assertEquals(9, grid.getValue(0, 8));
    }

    @Test
    @DisplayName("Returns false when no cell has exactly one candidate")
    void testReturnsFalseWhenNoSingleCandidate() {
        // Empty puzzle — every cell has 9 candidates
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        NakedSingle technique = new NakedSingle();
        assertFalse(technique.apply(grid));
    }

    @Test
    @DisplayName("Technique level is NAKED_SINGLE")
    void testLevel() {
        NakedSingle technique = new NakedSingle();
        assertEquals(TechniqueLevel.NAKED_SINGLE, technique.getLevel());
    }
}
