package com.franciscoguemes.sudoku;

import com.franciscoguemes.sudoku.gui.GameApp;
import com.franciscoguemes.sudoku.gui.EditorApp;

import java.util.Arrays;

/**
 * Application entry point that dispatches to one of the three sub-applications.
 * <p>
 * A plain (non-{@code Application}) main class is used so that the same entry
 * point works for all three modes without requiring three separate
 * {@code --module} invocations.
 * </p>
 * <p>Usage (run as a named module):</p>
 * <ul>
 *     <li>{@code java --module-path <libs> -m com.franciscoguemes.sudoku/com.franciscoguemes.sudoku.Launcher}
 *         — launches the Sudoku Game (default)</li>
 *     <li>{@code ... Launcher --editor} — launches the Sudoku Editor</li>
 *     <li>{@code ... Launcher --console} — launches the Console App</li>
 * </ul>
 */
public class Launcher {

    public static void main(String[] args) {
        if (args.length > 0 && "--editor".equals(args[0])) {
            EditorApp.main(stripFirst(args));
        } else if (args.length > 0 && "--console".equals(args[0])) {
            ConsoleApp.main(stripFirst(args));
        } else {
            GameApp.main(args);
        }
    }

    private static String[] stripFirst(String[] args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }
}
