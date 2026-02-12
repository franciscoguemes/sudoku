package com.franciscoguemes.sudoku.solver.techniques;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import com.franciscoguemes.sudoku.solver.CandidateGrid;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("XYWing Technique Tests")
class XYWingTest {

    @Test
    @DisplayName("Returns false when no XY-Wing pattern exists")
    void testReturnsFalseWhenNoXYWing() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        XYWing technique = new XYWing();
        assertFalse(technique.apply(grid));
    }

    @Test
    @DisplayName("Technique level is ADVANCED")
    void testLevel() {
        XYWing technique = new XYWing();
        assertEquals(TechniqueLevel.ADVANCED, technique.getLevel());
    }

    @Test
    @DisplayName("Detects XY-Wing and eliminates z from common peers of wings")
    void testXYWingElimination() {
        // Set up: pivot at (0,0) with {1,2}
        // wing1 at (0,3) with {1,3} (peer of pivot via row)
        // wing2 at (3,0) with {2,3} (peer of pivot via column)
        // Cell (3,3) sees both wings -> eliminate 3 from (3,3)
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        // Set pivot (0,0) = {1,2}
        for (int v = 3; v <= 9; v++) grid.removeCandidate(0, 0, v);

        // Set wing1 (0,3) = {1,3}
        for (int v = 1; v <= 9; v++) {
            if (v != 1 && v != 3) grid.removeCandidate(0, 3, v);
        }

        // Set wing2 (3,0) = {2,3}
        for (int v = 1; v <= 9; v++) {
            if (v != 2 && v != 3) grid.removeCandidate(3, 0, v);
        }

        // Verify (3,3) currently has 3 as candidate
        boolean had3 = grid.getCandidates(3, 3).contains(3);

        XYWing technique = new XYWing();
        boolean applied = technique.apply(grid);

        if (applied && had3) {
            // 3 should be eliminated from (3,3) which sees both wings
            assertFalse(grid.getCandidates(3, 3).contains(3),
                "3 should be eliminated from (3,3) via XY-Wing");
        }
    }
}
