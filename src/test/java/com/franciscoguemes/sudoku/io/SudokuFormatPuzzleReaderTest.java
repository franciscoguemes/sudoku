package com.franciscoguemes.sudoku.io;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SudokuFormatPuzzleReader Tests")
class SudokuFormatPuzzleReaderTest {

    private final SudokuFormatPuzzleReader reader = new SudokuFormatPuzzleReader();

    @Test
    @DisplayName("Reads the hardest sudoku from .sudoku file with correct values")
    void testReadHardestSudoku() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/puzzles/Hardest_in_the_world.sudoku")) {
            assertNotNull(is, "Resource file not found");
            Puzzle puzzle = reader.read(is);

            assertEquals(PuzzleType.SUDOKU, puzzle.getPuzzleType());

            // Same values as the CSV file
            assertEquals(8, puzzle.getValue(0, 0));
            assertEquals(3, puzzle.getValue(1, 2));
            assertEquals(6, puzzle.getValue(1, 3));
            assertEquals(7, puzzle.getValue(2, 1));
            assertEquals(9, puzzle.getValue(2, 4));
            assertEquals(2, puzzle.getValue(2, 6));
            assertEquals(5, puzzle.getValue(3, 1));
            assertEquals(7, puzzle.getValue(3, 5));
            assertEquals(4, puzzle.getValue(4, 4));
            assertEquals(5, puzzle.getValue(4, 5));
            assertEquals(7, puzzle.getValue(4, 6));
            assertEquals(1, puzzle.getValue(5, 3));
            assertEquals(3, puzzle.getValue(5, 7));
            assertEquals(1, puzzle.getValue(6, 2));
            assertEquals(6, puzzle.getValue(6, 7));
            assertEquals(8, puzzle.getValue(6, 8));
            assertEquals(8, puzzle.getValue(7, 2));
            assertEquals(5, puzzle.getValue(7, 3));
            assertEquals(1, puzzle.getValue(7, 7));
            assertEquals(9, puzzle.getValue(8, 1));
            assertEquals(4, puzzle.getValue(8, 6));

            // Verify empty cells
            assertEquals(0, puzzle.getValue(0, 1));
            assertEquals(0, puzzle.getValue(0, 8));
        }
    }

    @Test
    @DisplayName("Given values are immutable")
    void testGivenValuesAreImmutable() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/puzzles/Hardest_in_the_world.sudoku")) {
            Puzzle puzzle = reader.read(is);

            assertFalse(puzzle.isSlotMutable(0, 0));
            assertFalse(puzzle.isSlotMutable(1, 2));
            assertFalse(puzzle.isSlotMutable(8, 1));
        }
    }

    @Test
    @DisplayName("Empty cells are mutable")
    void testEmptyCellsAreMutable() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/puzzles/Hardest_in_the_world.sudoku")) {
            Puzzle puzzle = reader.read(is);

            assertTrue(puzzle.isSlotMutable(0, 1));
            assertTrue(puzzle.isSlotMutable(0, 2));
            assertTrue(puzzle.isSlotMutable(8, 8));
        }
    }

    @Test
    @DisplayName("Comments starting with # are skipped")
    void testCommentsSkipped() throws IOException {
        String sudoku = """
                # This is a comment
                # Another comment
                1 . . . . . . . .
                . . . 6 . . . . .
                . . . . . . . . .
                . . . . . . . . .
                . . . . . . . . .
                . . . . . . . . .
                . . . . . . . . .
                . . . . . . . . .
                . . . . . . . . .
                """;
        Puzzle puzzle = reader.read(new ByteArrayInputStream(sudoku.getBytes(StandardCharsets.UTF_8)));

        assertEquals(PuzzleType.SUDOKU, puzzle.getPuzzleType());
        assertEquals(1, puzzle.getValue(0, 0));
        assertEquals(6, puzzle.getValue(1, 3));
        assertEquals(0, puzzle.getValue(0, 1));
    }

    @Test
    @DisplayName("Dots represent no value")
    void testDotsAsNoValue() throws IOException {
        String sudoku = """
                . . . . . . . . .
                . . . . . . . . .
                . . . . . . . . .
                . . . . . . . . .
                . . . . . . . . .
                . . . . . . . . .
                . . . . . . . . .
                . . . . . . . . .
                . . . . . . . . .
                """;
        Puzzle puzzle = reader.read(new ByteArrayInputStream(sudoku.getBytes(StandardCharsets.UTF_8)));

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                assertEquals(0, puzzle.getValue(r, c));
            }
        }
    }

    @Test
    @DisplayName("Zeros represent no value")
    void testZerosAsNoValue() throws IOException {
        String sudoku = """
                0 0 0 0 0 0 0 0 0
                0 0 0 0 0 0 0 0 0
                0 0 0 0 0 0 0 0 0
                0 0 0 0 0 0 0 0 0
                0 0 0 0 0 0 0 0 0
                0 0 0 0 0 0 0 0 0
                0 0 0 0 0 0 0 0 0
                0 0 0 0 0 0 0 0 0
                0 0 0 0 0 0 0 0 0
                """;
        Puzzle puzzle = reader.read(new ByteArrayInputStream(sudoku.getBytes(StandardCharsets.UTF_8)));

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                assertEquals(0, puzzle.getValue(r, c));
            }
        }
    }

    @Test
    @DisplayName("Blank lines are skipped")
    void testBlankLinesSkipped() throws IOException {
        String sudoku = """

                1 . . . . . . . .

                . . . 6 . . . . .
                . . . . . . . . .
                . . . . . . . . .
                . . . . . . . . .
                . . . . . . . . .
                . . . . . . . . .

                . . . . . . . . .
                . . . . . . . . .

                """;
        Puzzle puzzle = reader.read(new ByteArrayInputStream(sudoku.getBytes(StandardCharsets.UTF_8)));

        assertEquals(PuzzleType.SUDOKU, puzzle.getPuzzleType());
        assertEquals(1, puzzle.getValue(0, 0));
        assertEquals(6, puzzle.getValue(1, 3));
    }

    @Test
    @DisplayName("Throws IOException for empty file")
    void testEmptyFile() {
        assertThrows(IOException.class, () ->
                reader.read(new ByteArrayInputStream(new byte[0])));
    }
}
