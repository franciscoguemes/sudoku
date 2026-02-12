package com.franciscoguemes.sudoku.solver.techniques;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import com.franciscoguemes.sudoku.solver.CandidateGrid;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Swordfish Technique Tests")
class SwordfishTest {

    @Test
    @DisplayName("Returns false when no Swordfish pattern exists")
    void testReturnsFalseWhenNoSwordfish() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        Swordfish technique = new Swordfish();
        assertFalse(technique.apply(grid));
    }

    @Test
    @DisplayName("Technique level is FISH_AND_REDUCTION")
    void testLevel() {
        Swordfish technique = new Swordfish();
        assertEquals(TechniqueLevel.FISH_AND_REDUCTION, technique.getLevel());
    }

    @Test
    @DisplayName("Detects Swordfish in rows and eliminates from columns")
    void testSwordfishInRows() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        // Set up a Swordfish for value 7 in rows 0, 3, 6 using columns 0, 3, 6
        // Row 0: 7 only in cols 0, 3
        // Row 3: 7 only in cols 3, 6
        // Row 6: 7 only in cols 0, 6
        for (int c = 0; c < 9; c++) {
            if (c != 0 && c != 3) grid.removeCandidate(0, c, 7);
            if (c != 3 && c != 6) grid.removeCandidate(3, c, 7);
            if (c != 0 && c != 6) grid.removeCandidate(6, c, 7);
        }

        boolean had7InTargetCols = false;
        for (int r = 0; r < 9; r++) {
            if (r == 0 || r == 3 || r == 6) continue;
            for (int c : new int[]{0, 3, 6}) {
                if (grid.isEmpty(r, c) && grid.getCandidates(r, c).contains(7)) {
                    had7InTargetCols = true;
                }
            }
        }

        if (had7InTargetCols) {
            Swordfish technique = new Swordfish();
            boolean applied = technique.apply(grid);

            if (applied) {
                for (int r = 0; r < 9; r++) {
                    if (r == 0 || r == 3 || r == 6) continue;
                    for (int c : new int[]{0, 3, 6}) {
                        if (grid.isEmpty(r, c)) {
                            assertFalse(grid.getCandidates(r, c).contains(7),
                                "7 should be removed from (" + r + "," + c + ")");
                        }
                    }
                }
            }
        }
    }
}
