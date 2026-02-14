package com.franciscoguemes.sudoku.solver.techniques;

import com.franciscoguemes.sudoku.solver.CandidateGrid;
import com.franciscoguemes.sudoku.solver.SolvingTechnique;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import com.franciscoguemes.sudoku.model.PuzzleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NakedTriple implements SolvingTechnique {

    private static final Logger LOG = LoggerFactory.getLogger(NakedTriple.class);

    @Override
    public TechniqueLevel getLevel() {
        return TechniqueLevel.NAKED_SUBSET;
    }

    @Override
    public boolean apply(CandidateGrid grid) {
        PuzzleType type = grid.getPuzzleType();

        for (int r = 0; r < type.getRows(); r++) {
            if (findNakedTripleInRegion(grid, grid.getEmptyCellsInRow(r))) return true;
        }

        for (int c = 0; c < type.getColumns(); c++) {
            if (findNakedTripleInRegion(grid, grid.getEmptyCellsInCol(c))) return true;
        }

        for (int br = 0; br < type.getRows(); br += type.getBoxHeight()) {
            for (int bc = 0; bc < type.getColumns(); bc += type.getBoxWidth()) {
                if (findNakedTripleInRegion(grid, grid.getEmptyCellsInBox(br, bc))) return true;
            }
        }

        return false;
    }

    private boolean findNakedTripleInRegion(CandidateGrid grid, List<int[]> emptyCells) {
        for (int i = 0; i < emptyCells.size(); i++) {
            Set<Integer> candsA = grid.getCandidates(emptyCells.get(i)[0], emptyCells.get(i)[1]);
            if (candsA.size() < 2 || candsA.size() > 3) continue;

            for (int j = i + 1; j < emptyCells.size(); j++) {
                Set<Integer> candsB = grid.getCandidates(emptyCells.get(j)[0], emptyCells.get(j)[1]);
                if (candsB.size() < 2 || candsB.size() > 3) continue;

                Set<Integer> unionAB = new HashSet<>(candsA);
                unionAB.addAll(candsB);
                if (unionAB.size() > 3) continue;

                for (int k = j + 1; k < emptyCells.size(); k++) {
                    Set<Integer> candsC = grid.getCandidates(emptyCells.get(k)[0], emptyCells.get(k)[1]);
                    if (candsC.size() < 2 || candsC.size() > 3) continue;

                    Set<Integer> union = new HashSet<>(unionAB);
                    union.addAll(candsC);
                    if (union.size() != 3) continue;

                    // Found a naked triple — eliminate union values from other cells
                    boolean removed = false;
                    for (int m = 0; m < emptyCells.size(); m++) {
                        if (m == i || m == j || m == k) continue;
                        int[] cell = emptyCells.get(m);
                        for (int v : union) {
                            if (grid.removeCandidate(cell[0], cell[1], v)) {
                                removed = true;
                            }
                        }
                    }
                    if (removed) {
                        LOG.debug("Naked Triple: {} — eliminated candidates", union);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "Naked Triple";
    }
}
