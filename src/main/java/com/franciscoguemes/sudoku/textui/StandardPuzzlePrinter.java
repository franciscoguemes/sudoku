package com.franciscoguemes.sudoku.textui;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;

public class StandardPuzzlePrinter extends AbstractPuzzlePrinter {

    @Override
    protected int getCellWidth(PuzzleType type) {
        return 1;
    }

    @Override
    protected void renderValue(int internalValue, StringBuilder sb, int cellWidth) {
        if (internalValue == Puzzle.NO_VALUE) {
            sb.append(".");
            return;
        }
        if (internalValue > 9) {
            sb.append((char) ('A' + internalValue - 10));
        } else {
            sb.append(internalValue);
        }
    }
}
