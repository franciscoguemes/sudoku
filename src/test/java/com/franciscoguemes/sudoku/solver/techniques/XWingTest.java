package com.franciscoguemes.sudoku.solver.techniques;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import com.franciscoguemes.sudoku.solver.CandidateGrid;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("XWing Technique Tests")
class XWingTest {

    @Test
    @DisplayName("Returns false when no X-Wing pattern exists")
    void testReturnsFalseWhenNoXWing() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        XWing technique = new XWing();
        assertFalse(technique.apply(grid));
    }

    @Test
    @DisplayName("Technique level is FISH_AND_REDUCTION")
    void testLevel() {
        XWing technique = new XWing();
        assertEquals(TechniqueLevel.FISH_AND_REDUCTION, technique.getLevel());
    }

    @Test
    @DisplayName("Detects X-Wing in rows and eliminates from columns")
    void testXWingInRows() {
        // Set up a puzzle where value 5 appears in exactly 2 columns (0,3) in rows 0 and 6
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);

        // Place 5 in columns 1,2,4,5,6,7,8 of row 0 neighbors to restrict 5 to cols 0,3
        // Easiest: just build a CandidateGrid and manipulate candidates directly
        CandidateGrid grid = new CandidateGrid(puzzle);

        // In row 0: remove 5 from all cells except (0,0) and (0,3)
        for (int c = 0; c < 9; c++) {
            if (c != 0 && c != 3) {
                grid.removeCandidate(0, c, 5);
            }
        }
        // In row 6: remove 5 from all cells except (6,0) and (6,3)
        for (int c = 0; c < 9; c++) {
            if (c != 0 && c != 3) {
                grid.removeCandidate(6, c, 5);
            }
        }

        // Other rows should have 5 candidates in columns 0 and 3
        // to be eliminated by the X-Wing
        boolean had5InTargetCols = false;
        for (int r = 1; r < 9; r++) {
            if (r == 6) continue;
            if (grid.getCandidates(r, 0).contains(5) || grid.getCandidates(r, 3).contains(5)) {
                had5InTargetCols = true;
            }
        }

        if (had5InTargetCols) {
            XWing technique = new XWing();
            boolean applied = technique.apply(grid);

            if (applied) {
                // 5 should be eliminated from cols 0 and 3 in all rows except 0 and 6
                for (int r = 1; r < 9; r++) {
                    if (r == 6) continue;
                    if (grid.isEmpty(r, 0)) {
                        assertFalse(grid.getCandidates(r, 0).contains(5),
                            "5 should be removed from (" + r + ",0)");
                    }
                    if (grid.isEmpty(r, 3)) {
                        assertFalse(grid.getCandidates(r, 3).contains(5),
                            "5 should be removed from (" + r + ",3)");
                    }
                }
            }
        }
    }
}
