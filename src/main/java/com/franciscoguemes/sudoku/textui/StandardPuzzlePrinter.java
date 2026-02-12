package com.franciscoguemes.sudoku.textui;

import com.franciscoguemes.sudoku.model.PuzzleType;
import com.franciscoguemes.sudoku.util.ValueFormatter;

public class StandardPuzzlePrinter extends AbstractPuzzlePrinter {

    @Override
    protected int getCellWidth(PuzzleType type) {
        return 1;
    }

    @Override
    protected void renderValue(int internalValue, StringBuilder sb, int cellWidth) {
        sb.append(ValueFormatter.getSudokuFormatRepresentationOf(internalValue));
    }
}
