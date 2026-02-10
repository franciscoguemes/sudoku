package com.franciscoguemes.sudoku.io;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class SudokuFormatPuzzleWriter implements PuzzleWriter {

    @Override
    public void write(Puzzle puzzle, OutputStream outputStream) throws IOException {
        PuzzleType type = puzzle.getPuzzleType();
        int rows = type.getRows();
        int cols = type.getColumns();

        PrintWriter writer = new PrintWriter(outputStream);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int value = puzzle.getValue(r, c);
                if (c > 0) {
                    writer.print(SudokuConstants.SEPARATOR);
                }
                if (value == Puzzle.NO_VALUE) {
                    writer.print(SudokuConstants.EMPTY);
                } else {
                    writer.print(value);
                }
            }
            writer.println();
        }
        writer.flush();
    }
}
