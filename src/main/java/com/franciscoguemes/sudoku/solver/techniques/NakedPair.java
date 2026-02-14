package com.franciscoguemes.sudoku.solver.techniques;

import com.franciscoguemes.sudoku.solver.CandidateGrid;
import com.franciscoguemes.sudoku.solver.SolvingTechnique;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import com.franciscoguemes.sudoku.model.PuzzleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public class NakedPair implements SolvingTechnique {

    private static final Logger LOG = LoggerFactory.getLogger(NakedPair.class);

    @Override
    public TechniqueLevel getLevel() {
        return TechniqueLevel.NAKED_SUBSET;
    }

    @Override
    public boolean apply(CandidateGrid grid) {
        PuzzleType type = grid.getPuzzleType();

        // Check rows
        for (int r = 0; r < type.getRows(); r++) {
            if (findNakedPairInRegion(grid, grid.getEmptyCellsInRow(r))) return true;
        }

        // Check columns
        for (int c = 0; c < type.getColumns(); c++) {
            if (findNakedPairInRegion(grid, grid.getEmptyCellsInCol(c))) return true;
        }

        // Check boxes
        for (int br = 0; br < type.getRows(); br += type.getBoxHeight()) {
            for (int bc = 0; bc < type.getColumns(); bc += type.getBoxWidth()) {
                if (findNakedPairInRegion(grid, grid.getEmptyCellsInBox(br, bc))) return true;
            }
        }

        return false;
    }

    private boolean findNakedPairInRegion(CandidateGrid grid, List<int[]> emptyCells) {
        for (int i = 0; i < emptyCells.size(); i++) {
            Set<Integer> candsA = grid.getCandidates(emptyCells.get(i)[0], emptyCells.get(i)[1]);
            if (candsA.size() != 2) continue;

            for (int j = i + 1; j < emptyCells.size(); j++) {
                Set<Integer> candsB = grid.getCandidates(emptyCells.get(j)[0], emptyCells.get(j)[1]);
                if (!candsA.equals(candsB)) continue;

                // Found a naked pair — eliminate these values from other cells in the region
                boolean removed = false;
                for (int k = 0; k < emptyCells.size(); k++) {
                    if (k == i || k == j) continue;
                    int[] cell = emptyCells.get(k);
                    for (int v : candsA) {
                        if (grid.removeCandidate(cell[0], cell[1], v)) {
                            removed = true;
                        }
                    }
                }
                if (removed) {
                    LOG.debug("Naked Pair: {} at [{},{}] and [{},{}] — eliminated candidates",
                            candsA, emptyCells.get(i)[0], emptyCells.get(i)[1],
                            emptyCells.get(j)[0], emptyCells.get(j)[1]);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "Naked Pair";
    }
}
