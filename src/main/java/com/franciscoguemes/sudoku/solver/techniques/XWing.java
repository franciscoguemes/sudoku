package com.franciscoguemes.sudoku.solver.techniques;

import com.franciscoguemes.sudoku.solver.CandidateGrid;
import com.franciscoguemes.sudoku.solver.SolvingTechnique;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import com.franciscoguemes.sudoku.model.PuzzleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class XWing implements SolvingTechnique {

    private static final Logger LOG = LoggerFactory.getLogger(XWing.class);

    @Override
    public TechniqueLevel getLevel() {
        return TechniqueLevel.FISH_AND_REDUCTION;
    }

    @Override
    public boolean apply(CandidateGrid grid) {
        PuzzleType type = grid.getPuzzleType();

        for (int v = type.getMinValue(); v <= type.getMaxValue(); v++) {
            // Row-based X-Wing
            if (findXWingInRows(grid, v)) return true;
            // Column-based X-Wing
            if (findXWingInCols(grid, v)) return true;
        }
        return false;
    }

    private boolean findXWingInRows(CandidateGrid grid, int v) {
        PuzzleType type = grid.getPuzzleType();
        List<int[]> rowsWithTwo = new ArrayList<>(); // [row, col1, col2]

        for (int r = 0; r < type.getRows(); r++) {
            List<Integer> cols = new ArrayList<>();
            for (int[] cell : grid.getEmptyCellsInRow(r)) {
                if (grid.getCandidates(cell[0], cell[1]).contains(v)) {
                    cols.add(cell[1]);
                }
            }
            if (cols.size() == 2) {
                rowsWithTwo.add(new int[]{r, cols.get(0), cols.get(1)});
            }
        }

        for (int i = 0; i < rowsWithTwo.size(); i++) {
            for (int j = i + 1; j < rowsWithTwo.size(); j++) {
                int[] a = rowsWithTwo.get(i);
                int[] b = rowsWithTwo.get(j);
                if (a[1] == b[1] && a[2] == b[2]) {
                    // X-Wing found: eliminate v from these two columns in all other rows
                    boolean removed = false;
                    for (int r = 0; r < type.getRows(); r++) {
                        if (r == a[0] || r == b[0]) continue;
                        if (grid.isEmpty(r, a[1]) && grid.removeCandidate(r, a[1], v)) removed = true;
                        if (grid.isEmpty(r, a[2]) && grid.removeCandidate(r, a[2], v)) removed = true;
                    }
                    if (removed) {
                        LOG.debug("X-Wing (rows): value {} in rows [{},{}] cols [{},{}]", v, a[0], b[0], a[1], a[2]);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean findXWingInCols(CandidateGrid grid, int v) {
        PuzzleType type = grid.getPuzzleType();
        List<int[]> colsWithTwo = new ArrayList<>(); // [col, row1, row2]

        for (int c = 0; c < type.getColumns(); c++) {
            List<Integer> rows = new ArrayList<>();
            for (int[] cell : grid.getEmptyCellsInCol(c)) {
                if (grid.getCandidates(cell[0], cell[1]).contains(v)) {
                    rows.add(cell[0]);
                }
            }
            if (rows.size() == 2) {
                colsWithTwo.add(new int[]{c, rows.get(0), rows.get(1)});
            }
        }

        for (int i = 0; i < colsWithTwo.size(); i++) {
            for (int j = i + 1; j < colsWithTwo.size(); j++) {
                int[] a = colsWithTwo.get(i);
                int[] b = colsWithTwo.get(j);
                if (a[1] == b[1] && a[2] == b[2]) {
                    boolean removed = false;
                    for (int c = 0; c < type.getColumns(); c++) {
                        if (c == a[0] || c == b[0]) continue;
                        if (grid.isEmpty(a[1], c) && grid.removeCandidate(a[1], c, v)) removed = true;
                        if (grid.isEmpty(a[2], c) && grid.removeCandidate(a[2], c, v)) removed = true;
                    }
                    if (removed) {
                        LOG.debug("X-Wing (cols): value {} in cols [{},{}] rows [{},{}]", v, a[0], b[0], a[1], a[2]);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "X-Wing";
    }
}
