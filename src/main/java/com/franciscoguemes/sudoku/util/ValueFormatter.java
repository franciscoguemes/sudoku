package com.franciscoguemes.sudoku.util;

import com.franciscoguemes.sudoku.io.CsvFormatConstants;
import com.franciscoguemes.sudoku.io.SudokuFormatConstants;
import com.franciscoguemes.sudoku.model.Puzzle;

public final class ValueFormatter {

    public static final String EMPTY_STRING = "";

    private ValueFormatter() {
    }

    public static int getInternalValueFromRepresentation(String representation){
        return switch (representation) {
            case EMPTY_STRING -> Puzzle.NO_VALUE;
            case CsvFormatConstants.EMPTY -> Puzzle.NO_VALUE;
            case SudokuFormatConstants.EMPTY -> Puzzle.NO_VALUE;
            case "A" -> 10;
            case "B" -> 11;
            case "C" -> 12;
            case "D" -> 13;
            case "E" -> 14;
            case "F" -> 15;
            case "G" -> 16;
            default -> Integer.parseInt(representation);
        };
    }

    public static String getSudokuFormatRepresentationOf(int internalValue){
        if (internalValue == Puzzle.NO_VALUE) {
            return SudokuFormatConstants.EMPTY;
        }
        return getRepresentationOf(internalValue);
    }

    public static String getCsvFormatRepresentationOf(int internalValue){
        if (internalValue == Puzzle.NO_VALUE) {
            return CsvFormatConstants.EMPTY;
        }
        return getRepresentationOf(internalValue);
    }

    private static String getRepresentationOf(int internalValue){
        if (internalValue > 9) {
            return String.valueOf((char) ('A' + internalValue - 10));
        } else {
            return Integer.toString(internalValue);
        }
    }

    public static String getGuiRepresentationOf(int internalValue) {
        if (internalValue == Puzzle.NO_VALUE) {
            return EMPTY_STRING;
        }
        return getRepresentationOf(internalValue);
    }

}
