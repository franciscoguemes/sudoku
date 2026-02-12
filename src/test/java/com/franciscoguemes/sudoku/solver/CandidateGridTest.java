package com.franciscoguemes.sudoku.solver;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CandidateGrid Tests")
class CandidateGridTest {

    @Test
    @DisplayName("Constructor computes correct initial candidates for empty puzzle")
    void testInitialCandidatesEmptyPuzzle() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        // Every empty cell in a completely empty 9x9 puzzle should have candidates 1-9
        Set<Integer> expected = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(expected, grid.getCandidates(0, 0));
        assertEquals(expected, grid.getCandidates(4, 4));
        assertEquals(expected, grid.getCandidates(8, 8));
    }

    @Test
    @DisplayName("Constructor excludes values present in same row, column, and box")
    void testInitialCandidatesWithGivenValues() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        puzzle.makeMove(0, 0, 5, false);
        puzzle.makeMove(0, 1, 3, false);
        puzzle.makeMove(1, 0, 6, false);

        CandidateGrid grid = new CandidateGrid(puzzle);

        // Cell (0,0) is filled — no candidates
        assertTrue(grid.getCandidates(0, 0).isEmpty());

        // Cell (0,2) is in row 0 (has 5,3) and box (0,0) (has 5,3,6)
        Set<Integer> c02 = grid.getCandidates(0, 2);
        assertFalse(c02.contains(5));
        assertFalse(c02.contains(3));
        assertFalse(c02.contains(6));
        assertTrue(c02.contains(1));
        assertTrue(c02.contains(2));

        // Cell (1,1) is in row 1 (has 6), col 1 (has 3), box (0,0) (has 5,3,6)
        Set<Integer> c11 = grid.getCandidates(1, 1);
        assertFalse(c11.contains(3));
        assertFalse(c11.contains(5));
        assertFalse(c11.contains(6));
    }

    @Test
    @DisplayName("placeValue sets value and eliminates from peers")
    void testPlaceValueEliminatesFromPeers() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        grid.placeValue(0, 0, 5);

        assertEquals(5, grid.getValue(0, 0));
        assertTrue(grid.getCandidates(0, 0).isEmpty());

        // Same row: 5 should be removed
        assertFalse(grid.getCandidates(0, 1).contains(5));
        assertFalse(grid.getCandidates(0, 8).contains(5));

        // Same col: 5 should be removed
        assertFalse(grid.getCandidates(1, 0).contains(5));
        assertFalse(grid.getCandidates(8, 0).contains(5));

        // Same box: 5 should be removed
        assertFalse(grid.getCandidates(1, 1).contains(5));
        assertFalse(grid.getCandidates(2, 2).contains(5));

        // Unrelated cell: 5 should remain
        assertTrue(grid.getCandidates(4, 4).contains(5));
    }

    @Test
    @DisplayName("removeCandidate removes and returns true; returns false if absent")
    void testRemoveCandidate() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        assertTrue(grid.removeCandidate(0, 0, 5));
        assertFalse(grid.getCandidates(0, 0).contains(5));
        assertFalse(grid.removeCandidate(0, 0, 5));
    }

    @Test
    @DisplayName("getRowCells returns correct cells")
    void testGetRowCells() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        List<int[]> cells = grid.getRowCells(3);
        assertEquals(9, cells.size());
        for (int i = 0; i < 9; i++) {
            assertEquals(3, cells.get(i)[0]);
            assertEquals(i, cells.get(i)[1]);
        }
    }

    @Test
    @DisplayName("getColCells returns correct cells")
    void testGetColCells() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        List<int[]> cells = grid.getColCells(4);
        assertEquals(9, cells.size());
        for (int i = 0; i < 9; i++) {
            assertEquals(i, cells.get(i)[0]);
            assertEquals(4, cells.get(i)[1]);
        }
    }

    @Test
    @DisplayName("getBoxCells returns correct cells for top-left box")
    void testGetBoxCells() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        List<int[]> cells = grid.getBoxCells(1, 1);
        assertEquals(9, cells.size());
        for (int[] cell : cells) {
            assertTrue(cell[0] >= 0 && cell[0] < 3);
            assertTrue(cell[1] >= 0 && cell[1] < 3);
        }
    }

    @Test
    @DisplayName("getBoxCells returns correct cells for center box in 9x9")
    void testGetBoxCellsCenter() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        List<int[]> cells = grid.getBoxCells(4, 4);
        assertEquals(9, cells.size());
        for (int[] cell : cells) {
            assertTrue(cell[0] >= 3 && cell[0] < 6);
            assertTrue(cell[1] >= 3 && cell[1] < 6);
        }
    }

    @Test
    @DisplayName("isSolved returns false for incomplete grid")
    void testIsSolvedFalse() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        assertFalse(grid.isSolved());
    }

    @Test
    @DisplayName("getEmptyCellsInRow filters out filled cells")
    void testGetEmptyCellsInRow() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        puzzle.makeMove(0, 0, 5, false);
        puzzle.makeMove(0, 3, 7, false);
        CandidateGrid grid = new CandidateGrid(puzzle);

        List<int[]> empty = grid.getEmptyCellsInRow(0);
        assertEquals(7, empty.size());
        for (int[] cell : empty) {
            assertTrue(cell[1] != 0 && cell[1] != 3);
        }
    }

    @Test
    @DisplayName("toPuzzle creates a valid Puzzle from grid state")
    void testToPuzzle() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        puzzle.makeMove(0, 0, 5, false);
        CandidateGrid grid = new CandidateGrid(puzzle);
        grid.placeValue(0, 1, 3);

        Puzzle result = grid.toPuzzle();
        assertEquals(5, result.getValue(0, 0));
        assertEquals(3, result.getValue(0, 1));
        assertEquals(Puzzle.NO_VALUE, result.getValue(0, 2));
    }

    @Test
    @DisplayName("Copy constructor creates independent copy")
    void testCopyConstructor() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        CandidateGrid original = new CandidateGrid(puzzle);
        CandidateGrid copy = new CandidateGrid(original);

        copy.placeValue(0, 0, 5);

        assertEquals(5, copy.getValue(0, 0));
        assertEquals(Puzzle.NO_VALUE, original.getValue(0, 0));
        assertTrue(original.getCandidates(0, 1).contains(5));
    }

    @Test
    @DisplayName("Works with 6x6 Mini Sudoku")
    void testMiniSudoku() {
        Puzzle puzzle = new Puzzle(PuzzleType.MINI_SUDOKU);
        CandidateGrid grid = new CandidateGrid(puzzle);

        Set<Integer> expected = Set.of(1, 2, 3, 4, 5, 6);
        assertEquals(expected, grid.getCandidates(0, 0));

        List<int[]> boxCells = grid.getBoxCells(0, 0);
        // 6x6 has 3x2 boxes = 6 cells
        assertEquals(6, boxCells.size());
    }
}
