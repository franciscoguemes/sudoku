package com.franciscoguemes.sudoku.solver.techniques;

import com.franciscoguemes.sudoku.solver.CandidateGrid;
import com.franciscoguemes.sudoku.solver.SolvingTechnique;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import com.franciscoguemes.sudoku.model.PuzzleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Swordfish implements SolvingTechnique {

    private static final Logger LOG = LoggerFactory.getLogger(Swordfish.class);

    @Override
    public TechniqueLevel getLevel() {
        return TechniqueLevel.FISH_AND_REDUCTION;
    }

    @Override
    public boolean apply(CandidateGrid grid) {
        PuzzleType type = grid.getPuzzleType();

        for (int v = type.getMinValue(); v <= type.getMaxValue(); v++) {
            if (findSwordfishInRows(grid, v)) return true;
            if (findSwordfishInCols(grid, v)) return true;
        }
        return false;
    }

    private boolean findSwordfishInRows(CandidateGrid grid, int v) {
        PuzzleType type = grid.getPuzzleType();
        List<int[]> rowData = new ArrayList<>(); // each entry: [row, cols...]

        for (int r = 0; r < type.getRows(); r++) {
            List<Integer> cols = new ArrayList<>();
            for (int[] cell : grid.getEmptyCellsInRow(r)) {
                if (grid.getCandidates(cell[0], cell[1]).contains(v)) {
                    cols.add(cell[1]);
                }
            }
            if (cols.size() >= 2 && cols.size() <= 3) {
                int[] entry = new int[cols.size() + 1];
                entry[0] = r;
                for (int i = 0; i < cols.size(); i++) entry[i + 1] = cols.get(i);
                rowData.add(entry);
            }
        }

        for (int i = 0; i < rowData.size(); i++) {
            for (int j = i + 1; j < rowData.size(); j++) {
                for (int k = j + 1; k < rowData.size(); k++) {
                    Set<Integer> colUnion = new HashSet<>();
                    addCols(colUnion, rowData.get(i));
                    addCols(colUnion, rowData.get(j));
                    addCols(colUnion, rowData.get(k));

                    if (colUnion.size() != 3) continue;

                    Set<Integer> rows = Set.of(rowData.get(i)[0], rowData.get(j)[0], rowData.get(k)[0]);
                    boolean removed = false;
                    for (int col : colUnion) {
                        for (int r = 0; r < type.getRows(); r++) {
                            if (rows.contains(r)) continue;
                            if (grid.isEmpty(r, col) && grid.removeCandidate(r, col, v)) {
                                removed = true;
                            }
                        }
                    }
                    if (removed) {
                        LOG.debug("Swordfish (rows): value {} in rows {} cols {}", v, rows, colUnion);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean findSwordfishInCols(CandidateGrid grid, int v) {
        PuzzleType type = grid.getPuzzleType();
        List<int[]> colData = new ArrayList<>();

        for (int c = 0; c < type.getColumns(); c++) {
            List<Integer> rows = new ArrayList<>();
            for (int[] cell : grid.getEmptyCellsInCol(c)) {
                if (grid.getCandidates(cell[0], cell[1]).contains(v)) {
                    rows.add(cell[0]);
                }
            }
            if (rows.size() >= 2 && rows.size() <= 3) {
                int[] entry = new int[rows.size() + 1];
                entry[0] = c;
                for (int i = 0; i < rows.size(); i++) entry[i + 1] = rows.get(i);
                colData.add(entry);
            }
        }

        for (int i = 0; i < colData.size(); i++) {
            for (int j = i + 1; j < colData.size(); j++) {
                for (int k = j + 1; k < colData.size(); k++) {
                    Set<Integer> rowUnion = new HashSet<>();
                    addCols(rowUnion, colData.get(i));
                    addCols(rowUnion, colData.get(j));
                    addCols(rowUnion, colData.get(k));

                    if (rowUnion.size() != 3) continue;

                    Set<Integer> cols = Set.of(colData.get(i)[0], colData.get(j)[0], colData.get(k)[0]);
                    boolean removed = false;
                    for (int row : rowUnion) {
                        for (int c = 0; c < type.getColumns(); c++) {
                            if (cols.contains(c)) continue;
                            if (grid.isEmpty(row, c) && grid.removeCandidate(row, c, v)) {
                                removed = true;
                            }
                        }
                    }
                    if (removed) {
                        LOG.debug("Swordfish (cols): value {} in cols {} rows {}", v, cols, rowUnion);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void addCols(Set<Integer> set, int[] entry) {
        for (int i = 1; i < entry.length; i++) {
            set.add(entry[i]);
        }
    }

    @Override
    public String getName() {
        return "Swordfish";
    }
}
