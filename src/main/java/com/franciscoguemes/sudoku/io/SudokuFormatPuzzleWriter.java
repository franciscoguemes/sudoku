package com.franciscoguemes.sudoku.io;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import com.franciscoguemes.sudoku.util.ValueFormatter;

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
                    writer.print(SudokuFormatConstants.SEPARATOR);
                }
                writer.print(ValueFormatter.getSudokuFormatRepresentationOf(value));
            }
            writer.println();
        }
        writer.flush();
    }
}
