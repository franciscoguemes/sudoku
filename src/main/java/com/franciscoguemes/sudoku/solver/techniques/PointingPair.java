package com.franciscoguemes.sudoku.solver.techniques;

import com.franciscoguemes.sudoku.solver.CandidateGrid;
import com.franciscoguemes.sudoku.solver.SolvingTechnique;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import com.franciscoguemes.sudoku.model.PuzzleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PointingPair implements SolvingTechnique {

    private static final Logger LOG = LoggerFactory.getLogger(PointingPair.class);

    @Override
    public TechniqueLevel getLevel() {
        return TechniqueLevel.NAKED_SUBSET;
    }

    @Override
    public boolean apply(CandidateGrid grid) {
        PuzzleType type = grid.getPuzzleType();

        for (int br = 0; br < type.getRows(); br += type.getBoxHeight()) {
            for (int bc = 0; bc < type.getColumns(); bc += type.getBoxWidth()) {
                List<int[]> boxCells = grid.getEmptyCellsInBox(br, bc);
                if (boxCells.isEmpty()) continue;

                for (int v = type.getMinValue(); v <= type.getMaxValue(); v++) {
                    final int val = v;
                    // Find cells in this box where v is a candidate
                    List<int[]> cellsWithV = boxCells.stream()
                            .filter(cell -> grid.getCandidates(cell[0], cell[1]).contains(val))
                            .toList();

                    if (cellsWithV.size() < 2) continue;

                    // Check if all cells are in the same row
                    boolean sameRow = cellsWithV.stream().allMatch(c -> c[0] == cellsWithV.get(0)[0]);
                    if (sameRow) {
                        int row = cellsWithV.get(0)[0];
                        boolean removed = false;
                        for (int[] cell : grid.getEmptyCellsInRow(row)) {
                            // Skip cells that are in this box
                            if (cell[1] >= bc && cell[1] < bc + type.getBoxWidth()) continue;
                            if (grid.removeCandidate(cell[0], cell[1], v)) {
                                removed = true;
                            }
                        }
                        if (removed) {
                            LOG.debug("Pointing Pair: value {} in row {} from box [{},{}]", v, row, br, bc);
                            return true;
                        }
                    }

                    // Check if all cells are in the same column
                    boolean sameCol = cellsWithV.stream().allMatch(c -> c[1] == cellsWithV.get(0)[1]);
                    if (sameCol) {
                        int col = cellsWithV.get(0)[1];
                        boolean removed = false;
                        for (int[] cell : grid.getEmptyCellsInCol(col)) {
                            // Skip cells that are in this box
                            if (cell[0] >= br && cell[0] < br + type.getBoxHeight()) continue;
                            if (grid.removeCandidate(cell[0], cell[1], v)) {
                                removed = true;
                            }
                        }
                        if (removed) {
                            LOG.debug("Pointing Pair: value {} in col {} from box [{},{}]", v, col, br, bc);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "Pointing Pair";
    }
}
