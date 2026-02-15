package com.franciscoguemes.sudoku.solver;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Solves Sudoku puzzles using Knuth's Dancing Links (DLX) algorithm.
 * <p>
 * Models Sudoku as an exact cover problem and uses Algorithm X with
 * doubly-linked circular lists for efficient column covering/uncovering.
 * This is significantly faster than simple backtracking, especially for
 * larger puzzle sizes (12x12, 16x16).
 * <p>
 * The exact cover matrix has four constraint types (N*N columns each):
 * <ol>
 *   <li>Cell constraint: each cell must have exactly one value</li>
 *   <li>Row-value constraint: each value appears exactly once per row</li>
 *   <li>Column-value constraint: each value appears exactly once per column</li>
 *   <li>Box-value constraint: each value appears exactly once per box</li>
 * </ol>
 */
public class DancingLinksSolver implements SudokuSolver {

    private static final Logger LOG = LoggerFactory.getLogger(DancingLinksSolver.class);

    @Override
    public boolean solve(Puzzle puzzle) {
        PuzzleType type = puzzle.getPuzzleType();
        int N = type.getRows();
        int minVal = type.getMinValue();
        int maxVal = type.getMaxValue();
        int boxHeight = type.getBoxHeight();
        int boxWidth = type.getBoxWidth();
        int boxesPerRow = type.getColumns() / boxWidth;

        int totalConstraints = 4 * N * N;

        // Build DLX column headers in a circular doubly-linked list
        ColumnHeader root = new ColumnHeader(-1);
        ColumnHeader[] headers = new ColumnHeader[totalConstraints];
        for (int i = 0; i < totalConstraints; i++) {
            headers[i] = new ColumnHeader(i);
            headers[i].right = root;
            headers[i].left = root.left;
            root.left.right = headers[i];
            root.left = headers[i];
        }

        // Build candidate rows: one per (row, col, value) triple
        List<int[]> candidates = new ArrayList<>();

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                int given = puzzle.getValue(r, c);

                for (int v = minVal; v <= maxVal; v++) {
                    // For pre-filled cells, only add the candidate for the given value
                    if (given != Puzzle.NO_VALUE && given != v) continue;

                    int box = (r / boxHeight) * boxesPerRow + (c / boxWidth);
                    int vIdx = v - minVal;

                    int idx = candidates.size();
                    candidates.add(new int[]{r, c, v});

                    // Constraint column indices
                    int cellCol   = r * N + c;
                    int rowValCol = N * N + r * N + vIdx;
                    int colValCol = 2 * N * N + c * N + vIdx;
                    int boxValCol = 3 * N * N + box * N + vIdx;

                    // Create 4 nodes and link them horizontally (circular)
                    Node n1 = new Node(idx, headers[cellCol]);
                    Node n2 = new Node(idx, headers[rowValCol]);
                    Node n3 = new Node(idx, headers[colValCol]);
                    Node n4 = new Node(idx, headers[boxValCol]);

                    n1.right = n2; n2.right = n3; n3.right = n4; n4.right = n1;
                    n1.left = n4; n4.left = n3; n3.left = n2; n2.left = n1;

                    // Append each node into its column
                    appendToColumn(n1);
                    appendToColumn(n2);
                    appendToColumn(n3);
                    appendToColumn(n4);
                }
            }
        }

        LOG.debug("Built DLX matrix: {} candidates, {} constraints", candidates.size(), totalConstraints);

        // Solve using Algorithm X
        List<Node> solution = new ArrayList<>();
        boolean found = search(root, solution);

        if (found) {
            for (Node node : solution) {
                int[] info = candidates.get(node.candidateIndex);
                if (puzzle.getValue(info[0], info[1]) == Puzzle.NO_VALUE) {
                    puzzle.makeMove(info[0], info[1], info[2], true);
                }
            }
        }

        return found;
    }

    /**
     * Recursive Algorithm X search. Selects the constraint column with the fewest
     * candidates (S heuristic), then tries each candidate row in that column.
     */
    private boolean search(ColumnHeader root, List<Node> solution) {
        if (root.right == root) {
            return true; // All constraints satisfied
        }

        ColumnHeader col = chooseColumn(root);
        if (col.size == 0) {
            return false; // Dead end: constraint with no candidates
        }

        cover(col);

        for (Node row = col.down; row != col; row = row.down) {
            solution.add(row);

            for (Node j = row.right; j != row; j = j.right) {
                cover(j.column);
            }

            if (search(root, solution)) {
                return true;
            }

            solution.remove(solution.size() - 1);

            for (Node j = row.left; j != row; j = j.left) {
                uncover(j.column);
            }
        }

        uncover(col);
        return false;
    }

    /**
     * Choose the column with the fewest nodes (S heuristic) to minimize branching.
     */
    private ColumnHeader chooseColumn(ColumnHeader root) {
        ColumnHeader best = null;
        int min = Integer.MAX_VALUE;
        for (Node n = root.right; n != root; n = n.right) {
            ColumnHeader col = (ColumnHeader) n;
            if (col.size < min) {
                min = col.size;
                best = col;
            }
        }
        return best;
    }

    /**
     * Cover a column: remove it from the header list and eliminate all rows
     * that have a node in this column from their other columns.
     */
    private void cover(ColumnHeader col) {
        col.right.left = col.left;
        col.left.right = col.right;

        for (Node row = col.down; row != col; row = row.down) {
            for (Node j = row.right; j != row; j = j.right) {
                j.down.up = j.up;
                j.up.down = j.down;
                j.column.size--;
            }
        }
    }

    /**
     * Uncover a column: restore it and all its rows (exact reverse of cover).
     */
    private void uncover(ColumnHeader col) {
        for (Node row = col.up; row != col; row = row.up) {
            for (Node j = row.left; j != row; j = j.left) {
                j.column.size++;
                j.down.up = j;
                j.up.down = j;
            }
        }

        col.right.left = col;
        col.left.right = col;
    }

    /**
     * Append a node at the bottom of its column (before the header in the circular list).
     */
    private void appendToColumn(Node node) {
        ColumnHeader col = node.column;
        node.down = col;
        node.up = col.up;
        col.up.down = node;
        col.up = node;
        col.size++;
    }

    // ---- Inner data structure classes ----

    private static class Node {
        Node left, right, up, down;
        ColumnHeader column;
        int candidateIndex;

        Node() {
            this.left = this;
            this.right = this;
            this.up = this;
            this.down = this;
        }

        Node(int candidateIndex, ColumnHeader column) {
            this();
            this.candidateIndex = candidateIndex;
            this.column = column;
        }
    }

    private static class ColumnHeader extends Node {
        int size;
        int name;

        ColumnHeader(int name) {
            super();
            this.name = name;
            this.size = 0;
            this.column = this;
        }
    }
}
