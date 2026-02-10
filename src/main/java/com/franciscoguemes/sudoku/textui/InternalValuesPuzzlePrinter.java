package com.franciscoguemes.sudoku.textui;

import com.franciscoguemes.sudoku.io.SudokuFormatConstants;
import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;

public class InternalValuesPuzzlePrinter extends AbstractPuzzlePrinter {

    @Override
    protected int getCellWidth(PuzzleType type) {
        return String.valueOf(type.getMaxValue()).length();
    }

    @Override
    protected void renderValue(int internalValue, StringBuilder sb, int cellWidth) {
        if (internalValue == Puzzle.NO_VALUE) {
            sb.append(String.format("%" + cellWidth + "s", SudokuFormatConstants.EMPTY));
            return;
        }
        sb.append(String.format("%" + cellWidth + "d", internalValue));
    }
}
