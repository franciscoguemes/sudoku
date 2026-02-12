package com.franciscoguemes.sudoku.solver.techniques;

import com.franciscoguemes.sudoku.solver.CandidateGrid;
import com.franciscoguemes.sudoku.solver.SolvingTechnique;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;

import java.util.Set;

public class NakedSingle implements SolvingTechnique {

    @Override
    public TechniqueLevel getLevel() {
        return TechniqueLevel.NAKED_SINGLE;
    }

    @Override
    public boolean apply(CandidateGrid grid) {
        int rows = grid.getPuzzleType().getRows();
        int cols = grid.getPuzzleType().getColumns();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid.isEmpty(r, c)) {
                    Set<Integer> cands = grid.getCandidates(r, c);
                    if (cands.size() == 1) {
                        int value = cands.iterator().next();
                        grid.placeValue(r, c, value);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "Naked Single";
    }
}
