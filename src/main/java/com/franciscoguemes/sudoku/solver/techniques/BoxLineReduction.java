package com.franciscoguemes.sudoku.solver.techniques;

import com.franciscoguemes.sudoku.solver.CandidateGrid;
import com.franciscoguemes.sudoku.solver.SolvingTechnique;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import com.franciscoguemes.sudoku.model.PuzzleType;

import java.util.List;

public class BoxLineReduction implements SolvingTechnique {

    @Override
    public TechniqueLevel getLevel() {
        return TechniqueLevel.FISH_AND_REDUCTION;
    }

    @Override
    public boolean apply(CandidateGrid grid) {
        PuzzleType type = grid.getPuzzleType();

        // Check rows
        for (int r = 0; r < type.getRows(); r++) {
            List<int[]> emptyCells = grid.getEmptyCellsInRow(r);
            for (int v = type.getMinValue(); v <= type.getMaxValue(); v++) {
                final int val = v;
                List<int[]> cellsWithV = emptyCells.stream()
                        .filter(cell -> grid.getCandidates(cell[0], cell[1]).contains(val))
                        .toList();

                if (cellsWithV.size() < 2) continue;

                // Check if all cells with v are in the same box
                int firstBoxCol = cellsWithV.get(0)[1] / type.getBoxWidth();
                boolean sameBox = cellsWithV.stream()
                        .allMatch(c -> c[1] / type.getBoxWidth() == firstBoxCol);

                if (sameBox) {
                    int boxStartCol = firstBoxCol * type.getBoxWidth();
                    int boxStartRow = (r / type.getBoxHeight()) * type.getBoxHeight();

                    // Eliminate v from rest of the box outside this row
                    boolean removed = false;
                    for (int[] cell : grid.getEmptyCellsInBox(boxStartRow, boxStartCol)) {
                        if (cell[0] == r) continue;
                        if (grid.removeCandidate(cell[0], cell[1], val)) {
                            removed = true;
                        }
                    }
                    if (removed) return true;
                }
            }
        }

        // Check columns
        for (int c = 0; c < type.getColumns(); c++) {
            List<int[]> emptyCells = grid.getEmptyCellsInCol(c);
            for (int v = type.getMinValue(); v <= type.getMaxValue(); v++) {
                final int val = v;
                List<int[]> cellsWithV = emptyCells.stream()
                        .filter(cell -> grid.getCandidates(cell[0], cell[1]).contains(val))
                        .toList();

                if (cellsWithV.size() < 2) continue;

                int firstBoxRow = cellsWithV.get(0)[0] / type.getBoxHeight();
                boolean sameBox = cellsWithV.stream()
                        .allMatch(cell -> cell[0] / type.getBoxHeight() == firstBoxRow);

                if (sameBox) {
                    int boxStartRow = firstBoxRow * type.getBoxHeight();
                    int boxStartCol = (c / type.getBoxWidth()) * type.getBoxWidth();

                    boolean removed = false;
                    for (int[] cell : grid.getEmptyCellsInBox(boxStartRow, boxStartCol)) {
                        if (cell[1] == c) continue;
                        if (grid.removeCandidate(cell[0], cell[1], val)) {
                            removed = true;
                        }
                    }
                    if (removed) return true;
                }
            }
        }

        return false;
    }

    @Override
    public String getName() {
        return "Box/Line Reduction";
    }
}
