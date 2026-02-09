package com.franciscoguemes.sudoku;

import com.franciscoguemes.sudoku.textui.ConsoleUI;
import com.franciscoguemes.sudoku.textui.StandardPuzzlePrinter;
import com.franciscoguemes.sudoku.textui.InternalValuesPuzzlePrinter;
import com.franciscoguemes.sudoku.textui.PuzzlePrinter;

import java.util.ArrayList;
import java.util.List;

public class App {

    private static final String INTERNAL_VALUES_FLAG = "--displayInternalValues";

    public static void main(String[] args) {
        boolean useInternalValues = false;
        List<String> remainingArgs = new ArrayList<>();

        for (String arg : args) {
            if (INTERNAL_VALUES_FLAG.equals(arg)) {
                useInternalValues = true;
            } else {
                remainingArgs.add(arg);
            }
        }

        PuzzlePrinter printer = useInternalValues
                ? new InternalValuesPuzzlePrinter()
                : new StandardPuzzlePrinter();

        ConsoleUI ui = new ConsoleUI(printer);
        ui.run(remainingArgs.toArray(new String[0]));
    }
}
