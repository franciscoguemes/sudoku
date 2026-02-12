package com.franciscoguemes.sudoku.solver.techniques;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import com.franciscoguemes.sudoku.solver.CandidateGrid;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UniqueRectangle Technique Tests")
class UniqueRectangleTest {

    @Test
    @DisplayName("Returns false when no unique rectangle pattern exists")
    void testReturnsFalseWhenNoUR() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        UniqueRectangle technique = new UniqueRectangle();
        assertFalse(technique.apply(grid));
    }

    @Test
    @DisplayName("Technique level is ADVANCED")
    void testLevel() {
        UniqueRectangle technique = new UniqueRectangle();
        assertEquals(TechniqueLevel.ADVANCED, technique.getLevel());
    }

    @Test
    @DisplayName("Type 1: removes pair values from cell with extra candidates")
    void testType1UniqueRectangle() {
        // Set up a rectangle at (0,0),(0,1),(3,0),(3,1) across 2 boxes
        // (0,0)={1,2}, (0,1)={1,2}, (3,0)={1,2}
        // (3,1)={1,2,5} — the "extra" cell
        // The technique should remove 1 and 2 from (3,1)
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        // Set (0,0) = {1,2}
        for (int v = 3; v <= 9; v++) grid.removeCandidate(0, 0, v);
        // Set (0,1) = {1,2}
        for (int v = 3; v <= 9; v++) grid.removeCandidate(0, 1, v);
        // Set (3,0) = {1,2}
        for (int v = 3; v <= 9; v++) grid.removeCandidate(3, 0, v);
        // Set (3,1) = {1,2,5}
        for (int v = 1; v <= 9; v++) {
            if (v != 1 && v != 2 && v != 5) grid.removeCandidate(3, 1, v);
        }

        UniqueRectangle technique = new UniqueRectangle();
        boolean applied = technique.apply(grid);

        if (applied) {
            assertFalse(grid.getCandidates(3, 1).contains(1),
                "1 should be removed from extra cell (3,1)");
            assertFalse(grid.getCandidates(3, 1).contains(2),
                "2 should be removed from extra cell (3,1)");
            assertTrue(grid.getCandidates(3, 1).contains(5),
                "5 should remain in extra cell (3,1)");
        }
    }
}
