package com.franciscoguemes.sudoku.io;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvPuzzleReader implements PuzzleReader {

    public static final String SEPARATOR = ",";

    @Override
    public Puzzle read(InputStream inputStream) throws IOException {
        List<int[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] tokens = line.split(SEPARATOR, -1);
                int[] row = new int[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    String token = tokens[i].trim();
                    if (token.isEmpty()) {
                        row[i] = Puzzle.NO_VALUE;
                    } else {
                        row[i] = Integer.parseInt(token);
                    }
                }
                rows.add(row);
            }
        }

        return buildPuzzle(rows);
    }

    private Puzzle buildPuzzle(List<int[]> rows) throws IOException {
        if (rows.isEmpty()) {
            throw new IOException("Empty puzzle file");
        }

        int numRows = rows.size();
        int numCols = rows.get(0).length;

        PuzzleType puzzleType = inferPuzzleType(numRows, numCols);
        Puzzle puzzle = new Puzzle(puzzleType);

        for (int r = 0; r < numRows; r++) {
            int[] row = rows.get(r);
            if (row.length != numCols) {
                throw new IOException("Inconsistent number of columns at row " + r
                        + ": expected " + numCols + " but got " + row.length);
            }
            for (int c = 0; c < numCols; c++) {
                if (row[c] != Puzzle.NO_VALUE) {
                    puzzle.makeMove(r, c, row[c], false);
                }
            }
        }

        return puzzle;
    }

    private PuzzleType inferPuzzleType(int rows, int cols) throws IOException {
        for (PuzzleType type : PuzzleType.values()) {
            if (type.getRows() == rows && type.getColumns() == cols) {
                return type;
            }
        }
        throw new IOException("Unsupported puzzle dimensions: " + rows + "x" + cols);
    }
}
