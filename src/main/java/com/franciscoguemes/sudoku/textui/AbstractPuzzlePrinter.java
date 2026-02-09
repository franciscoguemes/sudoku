package com.franciscoguemes.sudoku.textui;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;

abstract class AbstractPuzzlePrinter implements PuzzlePrinter {

    @Override
    public void print(Puzzle puzzle) {
        System.out.print(render(puzzle));
    }

    @Override
    public String render(Puzzle puzzle) {
        PuzzleType type = puzzle.getPuzzleType();
        int rows = type.getRows();
        int cols = type.getColumns();
        int boxWidth = type.getBoxWidth();
        int boxHeight = type.getBoxHeight();
        int cellWidth = getCellWidth(type);

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
                int internalValue = puzzle.getValue(r, c);
                renderValue(internalValue, sb, cellWidth);
                sb.append(" ");
            }
            sb.append("|").append(newLine);
        }
        sb.append(separator).append(newLine);

        return sb.toString();
    }

    protected abstract int getCellWidth(PuzzleType type);

    protected abstract void renderValue(int internalValue, StringBuilder sb, int cellWidth);

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
