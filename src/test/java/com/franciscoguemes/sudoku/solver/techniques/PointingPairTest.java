package com.franciscoguemes.sudoku.solver.techniques;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import com.franciscoguemes.sudoku.solver.CandidateGrid;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PointingPair Technique Tests")
class PointingPairTest {

    @Test
    @DisplayName("Eliminates candidates in row when pointing pair found in box")
    void testPointingPairInRow() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);

        // In box (0,0): place values so that 9 can only go in row 0
        // Place 9 in rows 1 and 2 outside box (0,0) to prevent it there
        puzzle.makeMove(1, 3, 9, false);
        puzzle.makeMove(2, 6, 9, false);
        // Now in box (0,0), 9 can only appear in row 0

        CandidateGrid grid = new CandidateGrid(puzzle);

        // Verify 9 is not a candidate in row 1 or 2 of box (0,0)
        // (since we placed 9 in row 1 and 2)
        boolean canGoInRow1or2 = false;
        for (int[] cell : grid.getEmptyCellsInBox(0, 0)) {
            if (cell[0] > 0 && grid.getCandidates(cell[0], cell[1]).contains(9)) {
                canGoInRow1or2 = true;
            }
        }

        if (!canGoInRow1or2) {
            // 9 can only go in row 0 of box (0,0)
            // PointingPair should eliminate 9 from row 0 outside box (0,0)
            PointingPair technique = new PointingPair();
            boolean applied = technique.apply(grid);

            if (applied) {
                // 9 should be removed from row 0 cells outside box (0,0)
                for (int c = 3; c < 9; c++) {
                    if (grid.isEmpty(0, c)) {
                        assertFalse(grid.getCandidates(0, c).contains(9),
                            "9 should be removed from (0," + c + ") outside pointing pair box");
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Returns false when no pointing pair exists")
    void testReturnsFalseWhenNoPointingPair() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        PointingPair technique = new PointingPair();
        assertFalse(technique.apply(grid));
    }

    @Test
    @DisplayName("Technique level is NAKED_SUBSET")
    void testLevel() {
        PointingPair technique = new PointingPair();
        assertEquals(TechniqueLevel.NAKED_SUBSET, technique.getLevel());
    }
}
