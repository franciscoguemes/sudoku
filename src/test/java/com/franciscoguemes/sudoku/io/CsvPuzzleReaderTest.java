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

@DisplayName("CsvPuzzleReader Tests")
class CsvPuzzleReaderTest {

    private final CsvPuzzleReader reader = new CsvPuzzleReader();

    @Test
    @DisplayName("Reads the hardest sudoku from CSV file with correct values")
    void testReadHardestSudoku() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/puzzles/Hardest_in_the_world.csv")) {
            assertNotNull(is, "Resource file not found");
            Puzzle puzzle = reader.read(is);

            assertEquals(PuzzleType.SUDOKU, puzzle.getPuzzleType());

            // Row 0: 8,0,0,0,0,0,0,0,0
            assertEquals(8, puzzle.getValue(0, 0));
            // Row 1: 0,0,3,6,0,0,0,0,0
            assertEquals(3, puzzle.getValue(1, 2));
            assertEquals(6, puzzle.getValue(1, 3));
            // Row 2: 0,7,0,0,9,0,2,0,0
            assertEquals(7, puzzle.getValue(2, 1));
            assertEquals(9, puzzle.getValue(2, 4));
            assertEquals(2, puzzle.getValue(2, 6));
            // Row 3: 0,5,0,0,0,7,0,0,0
            assertEquals(5, puzzle.getValue(3, 1));
            assertEquals(7, puzzle.getValue(3, 5));
            // Row 4: 0,0,0,0,4,5,7,0,0
            assertEquals(4, puzzle.getValue(4, 4));
            assertEquals(5, puzzle.getValue(4, 5));
            assertEquals(7, puzzle.getValue(4, 6));
            // Row 5: 0,0,0,1,0,0,0,3,0
            assertEquals(1, puzzle.getValue(5, 3));
            assertEquals(3, puzzle.getValue(5, 7));
            // Row 6: 0,0,1,0,0,0,0,6,8
            assertEquals(1, puzzle.getValue(6, 2));
            assertEquals(6, puzzle.getValue(6, 7));
            assertEquals(8, puzzle.getValue(6, 8));
            // Row 7: 0,0,8,5,0,0,0,1,0
            assertEquals(8, puzzle.getValue(7, 2));
            assertEquals(5, puzzle.getValue(7, 3));
            assertEquals(1, puzzle.getValue(7, 7));
            // Row 8: 0,9,0,0,0,0,4,0,0
            assertEquals(9, puzzle.getValue(8, 1));
            assertEquals(4, puzzle.getValue(8, 6));

            // Verify empty cells
            assertEquals(0, puzzle.getValue(0, 1));
            assertEquals(0, puzzle.getValue(0, 8));
            assertEquals(0, puzzle.getValue(4, 0));
        }
    }

    @Test
    @DisplayName("Given values are immutable")
    void testGivenValuesAreImmutable() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/puzzles/Hardest_in_the_world.csv")) {
            Puzzle puzzle = reader.read(is);

            assertFalse(puzzle.isSlotMutable(0, 0));
            assertFalse(puzzle.isSlotMutable(1, 2));
            assertFalse(puzzle.isSlotMutable(8, 1));
        }
    }

    @Test
    @DisplayName("Empty cells are mutable")
    void testEmptyCellsAreMutable() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/puzzles/Hardest_in_the_world.csv")) {
            Puzzle puzzle = reader.read(is);

            assertTrue(puzzle.isSlotMutable(0, 1));
            assertTrue(puzzle.isSlotMutable(0, 2));
            assertTrue(puzzle.isSlotMutable(8, 8));
        }
    }

    @Test
    @DisplayName("Empty strings are treated as no value")
    void testEmptyStringsAsNoValue() throws IOException {
        String csv = """
                1,,3,,,,,,
                ,,,6,,,,,
                ,,,,,,,,
                ,,,,,,,,
                ,,,,,,,,
                ,,,,,,,,
                ,,,,,,,,
                ,,,,,,,,
                ,,,,,,,,
                """;
        Puzzle puzzle = reader.read(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)));

        assertEquals(PuzzleType.SUDOKU, puzzle.getPuzzleType());
        assertEquals(1, puzzle.getValue(0, 0));
        assertEquals(0, puzzle.getValue(0, 1));
        assertEquals(3, puzzle.getValue(0, 2));
        assertEquals(6, puzzle.getValue(1, 3));
        assertEquals(0, puzzle.getValue(1, 0));
    }

    @Test
    @DisplayName("Blank spaces are treated as no value")
    void testBlankSpacesAsNoValue() throws IOException {
        String csv = """
                1, ,3, , , , , ,\s
                 , , ,6, , , , ,\s
                 , , , , , , , ,\s
                 , , , , , , , ,\s
                 , , , , , , , ,\s
                 , , , , , , , ,\s
                 , , , , , , , ,\s
                 , , , , , , , ,\s
                 , , , , , , , ,\s
                """;
        Puzzle puzzle = reader.read(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)));

        assertEquals(1, puzzle.getValue(0, 0));
        assertEquals(0, puzzle.getValue(0, 1));
        assertEquals(3, puzzle.getValue(0, 2));
        assertEquals(6, puzzle.getValue(1, 3));
    }

    @Test
    @DisplayName("CSV and Sudoku format produce the same puzzle")
    void testCsvAndSudokuFormatProduceSamePuzzle() throws IOException {
        Puzzle csvPuzzle;
        try (InputStream is = getClass().getResourceAsStream("/puzzles/Hardest_in_the_world.csv")) {
            csvPuzzle = reader.read(is);
        }

        SudokuFormatPuzzleReader sudokuReader = new SudokuFormatPuzzleReader();
        Puzzle sudokuPuzzle;
        try (InputStream is = getClass().getResourceAsStream("/puzzles/Hardest_in_the_world.sudoku")) {
            sudokuPuzzle = sudokuReader.read(is);
        }

        int[][] csvBoard = csvPuzzle.getBoard();
        int[][] sudokuBoard = sudokuPuzzle.getBoard();

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                assertEquals(csvBoard[r][c], sudokuBoard[r][c],
                        "Mismatch at row " + r + ", col " + c);
            }
        }
    }

    @Test
    @DisplayName("Throws IOException for empty file")
    void testEmptyFile() {
        assertThrows(IOException.class, () ->
                reader.read(new ByteArrayInputStream(new byte[0])));
    }

    @Test
    @DisplayName("Throws IOException for unsupported dimensions")
    void testUnsupportedDimensions() {
        String csv = """
                1,2,3
                4,5,6
                7,8,9
                """;
        assertThrows(IOException.class, () ->
                reader.read(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8))));
    }
}
