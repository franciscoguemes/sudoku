package com.franciscoguemes.sudoku.textui;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;

public class PuzzlePrinter {

    public void print(Puzzle puzzle) {
        System.out.print(render(puzzle));
    }

    public String render(Puzzle puzzle) {
        PuzzleType type = puzzle.getPuzzleType();
        int rows = type.getRows();
        int cols = type.getColumns();
        int boxWidth = type.getBoxWidth();
        int boxHeight = type.getBoxHeight();
        int cellWidth = String.valueOf(type.getMaxValue()).length();

        String separator = buildSeparator(cols, boxWidth, cellWidth);
        String newLine = System.lineSeparator();
        StringBuilder sb = new StringBuilder();

        for (int r = 0; r < rows; r++) {
            if (r % boxHeight == 0) {
                sb.append(separator).append(newLine);
            }
            for (int c = 0; c < cols; c++) {
                if (c % boxWidth == 0) {
                    sb.append("| ");
                }
                int value = puzzle.getValue(r, c);
                if (value == Puzzle.NO_VALUE) {
                    sb.append(String.format("%" + cellWidth + "s", "."));
                } else {
                    sb.append(String.format("%" + cellWidth + "d", value));
                }
                sb.append(" ");
            }
            sb.append("|").append(newLine);
        }
        sb.append(separator).append(newLine);

        return sb.toString();
    }

    private String buildSeparator(int cols, int boxWidth, int cellWidth) {
        int boxCols = cols / boxWidth;
        int segmentWidth = boxWidth * (cellWidth + 1) + 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < boxCols; i++) {
            sb.append("+");
            sb.append("-".repeat(segmentWidth));
        }
        sb.append("+");
        return sb.toString();
    }
}
