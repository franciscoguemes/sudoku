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
}
