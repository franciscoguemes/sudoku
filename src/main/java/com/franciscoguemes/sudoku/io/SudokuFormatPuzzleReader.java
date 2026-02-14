package com.franciscoguemes.sudoku.io;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import com.franciscoguemes.sudoku.util.ValueFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SudokuFormatPuzzleReader implements PuzzleReader {

    private static final Logger LOG = LoggerFactory.getLogger(SudokuFormatPuzzleReader.class);

    @Override
    public Puzzle read(InputStream inputStream) throws IOException {
        LOG.info("Reading puzzle in Sudoku format");
        List<int[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] tokens = line.split("\\s+");
                int[] row = new int[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    String token = tokens[i];
                    row[i] = ValueFormatter.getInternalValueFromRepresentation(token);
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
        LOG.info("Loaded Sudoku-format puzzle: {}x{} ({})", numRows, numCols, puzzleType);
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
