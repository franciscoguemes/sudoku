package com.franciscoguemes.sudoku.util;

import com.franciscoguemes.sudoku.io.SudokuConstants;
import com.franciscoguemes.sudoku.model.Puzzle;

public final class ValueFormatter {

    public static final String EMPTY_STRING = "";

    private ValueFormatter() {
    }

    public static String formatForGui(int value) {
        if (value == Puzzle.NO_VALUE) {
            return EMPTY_STRING;
        }
        if (value > 9) {
            return String.valueOf((char) ('A' + value - 10));
        }
        return String.valueOf(value);
    }

    public static void formatForTextUi(int internalValue, StringBuilder sb) {
        if (internalValue == Puzzle.NO_VALUE) {
            sb.append(SudokuConstants.EMPTY);
            return;
        }
        if (internalValue > 9) {
            sb.append((char) ('A' + internalValue - 10));
        } else {
            sb.append(internalValue);
        }
    }
}
