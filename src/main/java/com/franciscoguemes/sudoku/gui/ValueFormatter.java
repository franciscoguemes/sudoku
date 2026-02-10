package com.franciscoguemes.sudoku.gui;

import com.franciscoguemes.sudoku.model.Puzzle;

public final class ValueFormatter {

    private ValueFormatter() {
    }

    public static String format(int value) {
        if (value == Puzzle.NO_VALUE) {
            return "";
        }
        if (value > 9) {
            return String.valueOf((char) ('A' + value - 10));
        }
        return String.valueOf(value);
    }
}
