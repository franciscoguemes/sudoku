package com.franciscoguemes.sudoku.io;

import com.franciscoguemes.sudoku.model.Puzzle;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public interface PuzzleWriter {

    void write(Puzzle puzzle, OutputStream outputStream) throws IOException;

    default void write(Puzzle puzzle, Path filePath) throws IOException {
        try (OutputStream os = Files.newOutputStream(filePath)) {
            write(puzzle, os);
        }
    }

    static PuzzleWriter getWriterForFile(Path filePath) throws IOException {
        String fileName = filePath.getFileName().toString().toLowerCase();
        if (fileName.endsWith(CsvConstants.EXTENSION)) {
            return new CsvPuzzleWriter();
        } else if (fileName.endsWith(SudokuConstants.EXTENSION)) {
            return new SudokuFormatPuzzleWriter();
        }
        throw new IOException("Unsupported file format: " + filePath.getFileName()
                + ". Supported formats: " + CsvConstants.EXTENSION + ", " + SudokuConstants.EXTENSION);
    }
}
