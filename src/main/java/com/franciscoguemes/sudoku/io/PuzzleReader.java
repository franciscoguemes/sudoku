package com.franciscoguemes.sudoku.io;

import com.franciscoguemes.sudoku.model.Puzzle;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public interface PuzzleReader {

    Puzzle read(InputStream inputStream) throws IOException;

    default Puzzle read(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath)) {
            return read(is);
        }
    }

    static PuzzleReader getReaderForFile(Path filePath) throws IOException {
        String fileName = filePath.getFileName().toString().toLowerCase();
        if (fileName.endsWith(CsvFormatConstants.EXTENSION)) {
            return new CsvFormatPuzzleReader();
        } else if (fileName.endsWith(SudokuFormatConstants.EXTENSION)) {
            return new SudokuFormatPuzzleReader();
        }

        throw new IOException("Unsupported file format: " + filePath.getFileName()
                + ". Supported formats: " + CsvFormatConstants.EXTENSION + ", " + SudokuFormatConstants.EXTENSION);
    }
}
