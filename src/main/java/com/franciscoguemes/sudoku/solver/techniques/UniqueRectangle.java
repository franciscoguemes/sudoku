package com.franciscoguemes.sudoku.solver.techniques;

import com.franciscoguemes.sudoku.solver.CandidateGrid;
import com.franciscoguemes.sudoku.solver.SolvingTechnique;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import com.franciscoguemes.sudoku.model.PuzzleType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UniqueRectangle implements SolvingTechnique {

    @Override
    public TechniqueLevel getLevel() {
        return TechniqueLevel.ADVANCED;
    }

    @Override
    public boolean apply(CandidateGrid grid) {
        PuzzleType type = grid.getPuzzleType();

        // Find all cells with exactly 2 candidates
        List<int[]> biValueCells = new ArrayList<>();
        for (int r = 0; r < type.getRows(); r++) {
            for (int c = 0; c < type.getColumns(); c++) {
                if (grid.isEmpty(r, c) && grid.getCandidates(r, c).size() == 2) {
                    biValueCells.add(new int[]{r, c});
                }
            }
        }

        // Type 1 Unique Rectangle:
        // Find 4 cells forming a rectangle across exactly 2 boxes
        // where 3 cells have exactly {a,b} and the 4th has {a,b,...}
        for (int i = 0; i < biValueCells.size(); i++) {
            for (int j = i + 1; j < biValueCells.size(); j++) {
                int[] c1 = biValueCells.get(i);
                int[] c2 = biValueCells.get(j);

                Set<Integer> pair = grid.getCandidates(c1[0], c1[1]);
                if (!pair.equals(grid.getCandidates(c2[0], c2[1]))) continue;

                // c1 and c2 must share a row or column
                if (c1[0] != c2[0] && c1[1] != c2[1]) continue;

                if (c1[0] == c2[0]) {
                    // Same row — look for third cell in another row, same columns
                    if (findType1InRows(grid, c1, c2, pair)) return true;
                } else {
                    // Same column — look for third cell in another column, same rows
                    if (findType1InCols(grid, c1, c2, pair)) return true;
                }
            }
        }

        return false;
    }

    private boolean findType1InRows(CandidateGrid grid, int[] c1, int[] c2, Set<Integer> pair) {
        PuzzleType type = grid.getPuzzleType();
        int row = c1[0];
        int col1 = c1[1], col2 = c2[1];

        for (int r = 0; r < type.getRows(); r++) {
            if (r == row) continue;
            if (!grid.isEmpty(r, col1) || !grid.isEmpty(r, col2)) continue;

            // Check the rectangle spans exactly 2 boxes
            int box1 = boxIndex(row, col1, type);
            int box2 = boxIndex(row, col2, type);
            int box3 = boxIndex(r, col1, type);
            int box4 = boxIndex(r, col2, type);

            if (!spansTwoBoxes(box1, box2, box3, box4)) continue;

            Set<Integer> candsA = grid.getCandidates(r, col1);
            Set<Integer> candsB = grid.getCandidates(r, col2);

            // Case: 3 cells have pair, (r,col1) has extra candidates
            if (candsB.equals(pair) && candsA.containsAll(pair) && candsA.size() > 2) {
                boolean removed = false;
                for (int v : pair) {
                    if (grid.removeCandidate(r, col1, v)) removed = true;
                }
                if (removed) return true;
            }

            // Case: 3 cells have pair, (r,col2) has extra candidates
            if (candsA.equals(pair) && candsB.containsAll(pair) && candsB.size() > 2) {
                boolean removed = false;
                for (int v : pair) {
                    if (grid.removeCandidate(r, col2, v)) removed = true;
                }
                if (removed) return true;
            }
        }
        return false;
    }

    private boolean findType1InCols(CandidateGrid grid, int[] c1, int[] c2, Set<Integer> pair) {
        PuzzleType type = grid.getPuzzleType();
        int col = c1[1];
        int row1 = c1[0], row2 = c2[0];

        for (int c = 0; c < type.getColumns(); c++) {
            if (c == col) continue;
            if (!grid.isEmpty(row1, c) || !grid.isEmpty(row2, c)) continue;

            int box1 = boxIndex(row1, col, type);
            int box2 = boxIndex(row2, col, type);
            int box3 = boxIndex(row1, c, type);
            int box4 = boxIndex(row2, c, type);

            if (!spansTwoBoxes(box1, box2, box3, box4)) continue;

            Set<Integer> candsA = grid.getCandidates(row1, c);
            Set<Integer> candsB = grid.getCandidates(row2, c);

            if (candsB.equals(pair) && candsA.containsAll(pair) && candsA.size() > 2) {
                boolean removed = false;
                for (int v : pair) {
                    if (grid.removeCandidate(row1, c, v)) removed = true;
                }
                if (removed) return true;
            }

            if (candsA.equals(pair) && candsB.containsAll(pair) && candsB.size() > 2) {
                boolean removed = false;
                for (int v : pair) {
                    if (grid.removeCandidate(row2, c, v)) removed = true;
                }
                if (removed) return true;
            }
        }
        return false;
    }

    private int boxIndex(int r, int c, PuzzleType type) {
        return (r / type.getBoxHeight()) * (type.getColumns() / type.getBoxWidth())
                + (c / type.getBoxWidth());
    }

    private boolean spansTwoBoxes(int b1, int b2, int b3, int b4) {
        // The 4 cells must span exactly 2 boxes
        Set<Integer> boxes = new java.util.HashSet<>();
        boxes.add(b1);
        boxes.add(b2);
        boxes.add(b3);
        boxes.add(b4);
        return boxes.size() == 2;
    }

    @Override
    public String getName() {
        return "Unique Rectangle";
    }
}
