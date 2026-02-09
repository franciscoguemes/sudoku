package com.franciscoguemes.sudoku.io;

import com.franciscoguemes.sudoku.model.Puzzle;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public interface PuzzleReader {

    String CSV_EXTENSION = ".csv";
    String SUDOKU_EXTENSION = ".sudoku";

    Puzzle read(InputStream inputStream) throws IOException;

    default Puzzle read(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath)) {
            return read(is);
        }
    }

    static PuzzleReader getReaderForFile(Path filePath) throws IOException {
        String fileName = filePath.getFileName().toString().toLowerCase();
        if (fileName.endsWith(CSV_EXTENSION)) {
            return new CsvPuzzleReader();
        } else if (fileName.endsWith(SUDOKU_EXTENSION)) {
            return new SudokuFormatPuzzleReader();
        }

        throw new IOException("Unsupported file format: " + filePath.getFileName()
                + ". Supported formats: " + CSV_EXTENSION + ", " + SUDOKU_EXTENSION );
    }
}
