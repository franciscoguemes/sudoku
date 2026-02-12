package com.franciscoguemes.sudoku.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Puzzle Tests")
class PuzzleTest {

    private Puzzle standardPuzzle;
    private Puzzle miniPuzzle;

    @BeforeEach
    void setUp() {
        standardPuzzle = new Puzzle(PuzzleType.SUDOKU);
        miniPuzzle = new Puzzle(PuzzleType.MINI_SUDOKU);
    }

    @Test
    @DisplayName("Constructor creates empty board with correct dimensions")
    void testConstructor() {
        assertEquals(PuzzleType.SUDOKU, standardPuzzle.getPuzzleType());
        assertEquals(0, standardPuzzle.getValue(0, 0));
        assertEquals(0, standardPuzzle.getValue(8, 8));
        assertTrue(standardPuzzle.isSlotMutable(0, 0));
    }

    @Test
    @DisplayName("Copy constructor creates independent copy")
    void testCopyConstructor() {
        standardPuzzle.makeMove(0, 0, 5, false);
        Puzzle copy = new Puzzle(standardPuzzle);

        assertEquals(5, copy.getValue(0, 0));
        assertFalse(copy.isSlotMutable(0, 0));

        // Verify independence
        copy.makeMove(1, 1, 3, true);
        assertEquals(0, standardPuzzle.getValue(1, 1));
        assertEquals(3, copy.getValue(1, 1));
    }

    @Test
    @DisplayName("makeMove sets value when move is valid")
    void testMakeMoveValid() {
        standardPuzzle.makeMove(0, 0, 5, true);
        assertEquals(5, standardPuzzle.getValue(0, 0));
        assertTrue(standardPuzzle.isSlotMutable(0, 0));
    }

    @Test
    @DisplayName("makeMove does not set value on immutable slot")
    void testMakeMoveOnImmutableSlot() {
        standardPuzzle.makeMove(0, 0, 5, false);
        standardPuzzle.makeMove(0, 0, 7, true);
        assertEquals(5, standardPuzzle.getValue(0, 0));
    }

    @Test
    @DisplayName("makeMove rejects invalid value (duplicate in row)")
    void testMakeMoveInvalidRow() {
        standardPuzzle.makeMove(0, 0, 5, true);
        standardPuzzle.makeMove(0, 5, 5, true);
        assertEquals(0, standardPuzzle.getValue(0, 5));
    }

    @Test
    @DisplayName("makeMove rejects invalid value (duplicate in column)")
    void testMakeMoveInvalidColumn() {
        standardPuzzle.makeMove(0, 0, 5, true);
        standardPuzzle.makeMove(5, 0, 5, true);
        assertEquals(0, standardPuzzle.getValue(5, 0));
    }

    @Test
    @DisplayName("makeMove rejects invalid value (duplicate in box)")
    void testMakeMoveInvalidBox() {
        standardPuzzle.makeMove(0, 0, 5, true);
        standardPuzzle.makeMove(2, 2, 5, true);
        assertEquals(0, standardPuzzle.getValue(2, 2));
    }

    @Test
    @DisplayName("isValidMove returns true for valid moves")
    void testIsValidMoveValid() {
        standardPuzzle.makeMove(0, 0, 5, true);
        assertTrue(standardPuzzle.isValidMove(0, 1, 3));
        assertTrue(standardPuzzle.isValidMove(1, 0, 3));
    }

    @Test
    @DisplayName("isValidMove returns false for invalid moves")
    void testIsValidMoveInvalid() {
        standardPuzzle.makeMove(0, 0, 5, true);
        assertFalse(standardPuzzle.isValidMove(0, 5, 5)); // Same row
        assertFalse(standardPuzzle.isValidMove(5, 0, 5)); // Same column
        assertFalse(standardPuzzle.isValidMove(2, 2, 5)); // Same box
    }

    @Test
    @DisplayName("numInRow detects number in row")
    void testNumInRow() {
        standardPuzzle.makeMove(0, 0, 5, true);
        assertTrue(standardPuzzle.numInRow(0, 5));
        assertFalse(standardPuzzle.numInRow(0, 3));
        assertFalse(standardPuzzle.numInRow(1, 5));
    }

    @Test
    @DisplayName("numInCol detects number in column")
    void testNumInCol() {
        standardPuzzle.makeMove(0, 0, 5, true);
        assertTrue(standardPuzzle.numInCol(0, 5));
        assertFalse(standardPuzzle.numInCol(0, 3));
        assertFalse(standardPuzzle.numInCol(1, 5));
    }

    @Test
    @DisplayName("numInBox detects number in 3x3 box")
    void testNumInBox() {
        standardPuzzle.makeMove(0, 0, 5, true);
        assertTrue(standardPuzzle.numInBox(0, 0, 5));
        assertTrue(standardPuzzle.numInBox(2, 2, 5));
        assertFalse(standardPuzzle.numInBox(3, 3, 5)); // Different box
        assertFalse(standardPuzzle.numInBox(0, 0, 3));
    }

    @Test
    @DisplayName("numInBox works for different boxes")
    void testNumInBoxDifferentBoxes() {
        // Top-left box (0,0) to (2,2)
        standardPuzzle.makeMove(1, 1, 5, true);
        assertTrue(standardPuzzle.numInBox(0, 0, 5));

        // Top-middle box (0,3) to (2,5)
        assertFalse(standardPuzzle.numInBox(0, 3, 5));
        standardPuzzle.makeMove(0, 3, 7, true);
        assertTrue(standardPuzzle.numInBox(2, 5, 7));

        // Bottom-right box (6,6) to (8,8)
        assertFalse(standardPuzzle.numInBox(6, 6, 5));
        standardPuzzle.makeMove(8, 8, 9, true);
        assertTrue(standardPuzzle.numInBox(6, 6, 9));
    }

    @Test
    @DisplayName("getValue returns correct value")
    void testGetValue() {
        standardPuzzle.makeMove(3, 4, 7, true);
        assertEquals(7, standardPuzzle.getValue(3, 4));
        assertEquals(0, standardPuzzle.getValue(0, 0));
    }

    @ParameterizedTest
    @CsvSource({
            "-1, 0",
            "0, -1",
            "9, 0",
            "0, 9"
    })
    @DisplayName("getValue throws ArrayIndexOutOfBoundsException for out of range coordinates")
    void testGetValueOutOfRane(int row, int col) {
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> standardPuzzle.getValue(row, col));
    }

    @Test
    @DisplayName("getBoard returns defensive copy")
    void testGetBoardDefensiveCopy() {
        standardPuzzle.makeMove(0, 0, 5, true);
        int[][] board = standardPuzzle.getBoard();

        assertEquals(5, board[0][0]);

        // Modify the returned array
        board[0][0] = 9;
        board[1][1] = 7;

        // Original puzzle should be unchanged
        assertEquals(5, standardPuzzle.getValue(0, 0));
        assertEquals(0, standardPuzzle.getValue(1, 1));
    }

    @Test
    @DisplayName("isSlotAvailable returns true for empty mutable slots")
    void testIsSlotAvailable() {
        assertTrue(standardPuzzle.isSlotAvailable(0, 0));

        standardPuzzle.makeMove(0, 0, 5, false);
        assertFalse(standardPuzzle.isSlotAvailable(0, 0)); // Has value

        standardPuzzle.makeMove(1, 1, 3, true);
        assertFalse(standardPuzzle.isSlotAvailable(1, 1)); // Has value
    }

    @Test
    @DisplayName("isSlotMutable returns correct mutability status")
    void testIsSlotMutable() {
        assertTrue(standardPuzzle.isSlotMutable(0, 0));

        standardPuzzle.makeMove(0, 0, 5, false);
        assertFalse(standardPuzzle.isSlotMutable(0, 0));

        standardPuzzle.makeMove(1, 1, 3, true);
        assertTrue(standardPuzzle.isSlotMutable(1, 1));
    }

    @Test
    @DisplayName("inRange validates coordinates correctly")
    void testInRange() {
        assertTrue(standardPuzzle.inRange(0, 0));
        assertTrue(standardPuzzle.inRange(8, 8));
        assertTrue(standardPuzzle.inRange(4, 4));

        assertFalse(standardPuzzle.inRange(-1, 0));
        assertFalse(standardPuzzle.inRange(0, -1));
        assertFalse(standardPuzzle.inRange(9, 0));
        assertFalse(standardPuzzle.inRange(0, 9));
    }

    @Test
    @DisplayName("boardFull returns false for incomplete board")
    void testBoardFullIncomplete() {
        assertFalse(standardPuzzle.boardFull());

        standardPuzzle.makeMove(0, 0, 5, true);
        assertFalse(standardPuzzle.boardFull());
    }

    @Test
    @DisplayName("boardFull returns true when board is complete")
    void testBoardFullComplete() {
        // Fill the entire board with valid moves
        for(int r = 0; r < 9; r++) {
            for(int c = 0; c < 9; c++) {
                // Simple valid fill (not a real solution, but tests the method)
                int value = ((r * 3 + r / 3 + c) % 9) + 1;
                standardPuzzle.makeMove(r, c, value, true);
            }
        }
        assertTrue(standardPuzzle.boardFull());
    }

    @Test
    @DisplayName("makeSlotEmpty clears slot value")
    void testMakeSlotEmpty() {
        standardPuzzle.makeMove(0, 0, 5, true);
        assertEquals(5, standardPuzzle.getValue(0, 0));

        standardPuzzle.makeSlotEmpty(0, 0);
        assertEquals(0, standardPuzzle.getValue(0, 0));
    }

    @Test
    @DisplayName("toString generates valid board representation")
    void testToString() {
        standardPuzzle.makeMove(0, 0, 5, true);
        String boardString = standardPuzzle.toString();

        assertNotNull(boardString);
        assertTrue(boardString.contains("Game Board:"));
        assertTrue(boardString.contains("5"));
    }

    @Test
    @DisplayName("Mini Sudoku (6x6) works correctly")
    void testMiniSudoku() {
        assertEquals(PuzzleType.MINI_SUDOKU, miniPuzzle.getPuzzleType());

        miniPuzzle.makeMove(0, 0, 5, true);
        assertEquals(5, miniPuzzle.getValue(0, 0));
        assertTrue(miniPuzzle.inRange(5, 5));
        assertFalse(miniPuzzle.inRange(6, 6));
    }
}
