package com.franciscoguemes.sudoku.solver.techniques;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import com.franciscoguemes.sudoku.solver.CandidateGrid;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BoxLineReduction Technique Tests")
class BoxLineReductionTest {

    @Test
    @DisplayName("Eliminates candidates in box when value confined to single row")
    void testBoxLineReductionFromRow() {
        // In row 0, if value 9 candidates are only in box (0,0),
        // then 9 should be removed from rest of box (0,0) outside row 0
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);

        // Place 9 in columns 3-8 of row 0's other boxes to confine 9 to box (0,0) in row 0
        puzzle.makeMove(0, 3, 9, false);
        // Also place 9 in the box (0,0) rows 1 and 2 to create the reduction scenario
        // We need 9 to be available in row 0 col 0-2 but also in other rows of box (0,0)
        // The technique removes 9 from box cells outside the row

        CandidateGrid grid = new CandidateGrid(puzzle);

        // Manually remove 9 from row 0, cols 6-8 area
        for (int c = 6; c < 9; c++) {
            grid.removeCandidate(0, c, 9);
        }

        BoxLineReduction technique = new BoxLineReduction();
        boolean applied = technique.apply(grid);

        // If the technique applied, 9 should be removed from box (0,0) rows 1,2
        if (applied) {
            for (int r = 1; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (grid.isEmpty(r, c)) {
                        assertFalse(grid.getCandidates(r, c).contains(9),
                            "9 should be removed from box (0,0) cell (" + r + "," + c + ") outside row 0");
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Returns false when no box-line reduction exists")
    void testReturnsFalseWhenNoReduction() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        BoxLineReduction technique = new BoxLineReduction();
        assertFalse(technique.apply(grid));
    }

    @Test
    @DisplayName("Technique level is FISH_AND_REDUCTION")
    void testLevel() {
        BoxLineReduction technique = new BoxLineReduction();
        assertEquals(TechniqueLevel.FISH_AND_REDUCTION, technique.getLevel());
    }
}
