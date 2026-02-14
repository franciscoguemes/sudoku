package com.franciscoguemes.sudoku.solver;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CandidateGrid {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateGrid.class);

    private final int[][] board;
    private final Set<Integer>[][] candidates;
    private final PuzzleType puzzleType;

    @SuppressWarnings("unchecked")
    public CandidateGrid(Puzzle puzzle) {
        this.puzzleType = puzzle.getPuzzleType();
        int rows = puzzleType.getRows();
        int cols = puzzleType.getColumns();

        this.board = new int[rows][cols];
        this.candidates = new HashSet[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                board[r][c] = puzzle.getValue(r, c);
                candidates[r][c] = new HashSet<>();
            }
        }

        int emptyCells = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == Puzzle.NO_VALUE) {
                    emptyCells++;
                    for (int v = puzzleType.getMinValue(); v <= puzzleType.getMaxValue(); v++) {
                        if (!numInRow(r, v) && !numInCol(c, v) && !numInBox(r, c, v)) {
                            candidates[r][c].add(v);
                        }
                    }
                }
            }
        }
        LOG.debug("CandidateGrid initialized: {} empty cells out of {}x{}", emptyCells, rows, cols);
    }

    @SuppressWarnings("unchecked")
    public CandidateGrid(CandidateGrid other) {
        this.puzzleType = other.puzzleType;
        int rows = puzzleType.getRows();
        int cols = puzzleType.getColumns();

        this.board = new int[rows][cols];
        this.candidates = new HashSet[rows][cols];

        for (int r = 0; r < rows; r++) {
            System.arraycopy(other.board[r], 0, this.board[r], 0, cols);
            for (int c = 0; c < cols; c++) {
                this.candidates[r][c] = new HashSet<>(other.candidates[r][c]);
            }
        }
    }

    public Set<Integer> getCandidates(int r, int c) {
        return Collections.unmodifiableSet(candidates[r][c]);
    }

    public boolean removeCandidate(int r, int c, int value) {
        return candidates[r][c].remove(value);
    }

    public void placeValue(int r, int c, int value) {
        LOG.trace("Placing value {} at [{},{}]", value, r, c);
        board[r][c] = value;
        candidates[r][c].clear();

        // Eliminate value from all peers
        for (int[] peer : getRowCells(r)) {
            if (peer[1] != c) {
                candidates[peer[0]][peer[1]].remove(value);
            }
        }
        for (int[] peer : getColCells(c)) {
            if (peer[0] != r) {
                candidates[peer[0]][peer[1]].remove(value);
            }
        }
        for (int[] peer : getBoxCells(r, c)) {
            if (peer[0] != r || peer[1] != c) {
                candidates[peer[0]][peer[1]].remove(value);
            }
        }
    }

    public int getValue(int r, int c) {
        return board[r][c];
    }

    public boolean isEmpty(int r, int c) {
        return board[r][c] == Puzzle.NO_VALUE;
    }

    public boolean isSolved() {
        for (int r = 0; r < puzzleType.getRows(); r++) {
            for (int c = 0; c < puzzleType.getColumns(); c++) {
                if (board[r][c] == Puzzle.NO_VALUE) return false;
            }
        }
        return true;
    }

    public PuzzleType getPuzzleType() {
        return puzzleType;
    }

    public List<int[]> getRowCells(int r) {
        List<int[]> cells = new ArrayList<>();
        for (int c = 0; c < puzzleType.getColumns(); c++) {
            cells.add(new int[]{r, c});
        }
        return cells;
    }

    public List<int[]> getColCells(int c) {
        List<int[]> cells = new ArrayList<>();
        for (int r = 0; r < puzzleType.getRows(); r++) {
            cells.add(new int[]{r, c});
        }
        return cells;
    }

    public List<int[]> getBoxCells(int r, int c) {
        List<int[]> cells = new ArrayList<>();
        int boxHeight = puzzleType.getBoxHeight();
        int boxWidth = puzzleType.getBoxWidth();
        int startRow = (r / boxHeight) * boxHeight;
        int startCol = (c / boxWidth) * boxWidth;

        for (int br = startRow; br < startRow + boxHeight; br++) {
            for (int bc = startCol; bc < startCol + boxWidth; bc++) {
                cells.add(new int[]{br, bc});
            }
        }
        return cells;
    }

    public List<int[]> getEmptyCellsInRow(int r) {
        List<int[]> cells = new ArrayList<>();
        for (int c = 0; c < puzzleType.getColumns(); c++) {
            if (isEmpty(r, c)) cells.add(new int[]{r, c});
        }
        return cells;
    }

    public List<int[]> getEmptyCellsInCol(int c) {
        List<int[]> cells = new ArrayList<>();
        for (int r = 0; r < puzzleType.getRows(); r++) {
            if (isEmpty(r, c)) cells.add(new int[]{r, c});
        }
        return cells;
    }

    public List<int[]> getEmptyCellsInBox(int r, int c) {
        List<int[]> cells = new ArrayList<>();
        for (int[] cell : getBoxCells(r, c)) {
            if (isEmpty(cell[0], cell[1])) cells.add(cell);
        }
        return cells;
    }

    public Puzzle toPuzzle() {
        Puzzle puzzle = new Puzzle(puzzleType);
        for (int r = 0; r < puzzleType.getRows(); r++) {
            for (int c = 0; c < puzzleType.getColumns(); c++) {
                if (board[r][c] != Puzzle.NO_VALUE) {
                    puzzle.makeMove(r, c, board[r][c], false);
                }
            }
        }
        return puzzle;
    }

    private boolean numInRow(int row, int value) {
        for (int c = 0; c < puzzleType.getColumns(); c++) {
            if (board[row][c] == value) return true;
        }
        return false;
    }

    private boolean numInCol(int col, int value) {
        for (int r = 0; r < puzzleType.getRows(); r++) {
            if (board[r][col] == value) return true;
        }
        return false;
    }

    private boolean numInBox(int row, int col, int value) {
        int boxHeight = puzzleType.getBoxHeight();
        int boxWidth = puzzleType.getBoxWidth();
        int startRow = (row / boxHeight) * boxHeight;
        int startCol = (col / boxWidth) * boxWidth;

        for (int r = startRow; r < startRow + boxHeight; r++) {
            for (int c = startCol; c < startCol + boxWidth; c++) {
                if (board[r][c] == value) return true;
            }
        }
        return false;
    }
}
