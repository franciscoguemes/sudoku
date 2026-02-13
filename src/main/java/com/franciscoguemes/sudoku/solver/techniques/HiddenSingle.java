package com.franciscoguemes.sudoku.solver.techniques;

import com.franciscoguemes.sudoku.solver.CandidateGrid;
import com.franciscoguemes.sudoku.solver.SolvingTechnique;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import com.franciscoguemes.sudoku.model.PuzzleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HiddenSingle implements SolvingTechnique {

    private static final Logger LOG = LoggerFactory.getLogger(HiddenSingle.class);

    @Override
    public TechniqueLevel getLevel() {
        return TechniqueLevel.HIDDEN_SINGLE;
    }

    @Override
    public boolean apply(CandidateGrid grid) {
        PuzzleType type = grid.getPuzzleType();

        // Check rows
        for (int r = 0; r < type.getRows(); r++) {
            if (findHiddenSingleInRegion(grid, grid.getEmptyCellsInRow(r))) return true;
        }

        // Check columns
        for (int c = 0; c < type.getColumns(); c++) {
            if (findHiddenSingleInRegion(grid, grid.getEmptyCellsInCol(c))) return true;
        }

        // Check boxes
        for (int br = 0; br < type.getRows(); br += type.getBoxHeight()) {
            for (int bc = 0; bc < type.getColumns(); bc += type.getBoxWidth()) {
                if (findHiddenSingleInRegion(grid, grid.getEmptyCellsInBox(br, bc))) return true;
            }
        }

        return false;
    }

    private boolean findHiddenSingleInRegion(CandidateGrid grid, List<int[]> emptyCells) {
        int min = grid.getPuzzleType().getMinValue();
        int max = grid.getPuzzleType().getMaxValue();

        for (int v = min; v <= max; v++) {
            int[] onlyCell = null;
            int count = 0;

            for (int[] cell : emptyCells) {
                if (grid.getCandidates(cell[0], cell[1]).contains(v)) {
                    onlyCell = cell;
                    count++;
                    if (count > 1) break;
                }
            }

            if (count == 1) {
                LOG.debug("Hidden Single: placed {} at [{},{}]", v, onlyCell[0], onlyCell[1]);
                grid.placeValue(onlyCell[0], onlyCell[1], v);
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "Hidden Single";
    }
}
