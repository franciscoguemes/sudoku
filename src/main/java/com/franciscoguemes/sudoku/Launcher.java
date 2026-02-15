package com.franciscoguemes.sudoku;

import com.franciscoguemes.sudoku.gui.GameApp;
import com.franciscoguemes.sudoku.gui.EditorApp;

import java.util.Arrays;

/**
 * Non-Application launcher class required for running JavaFX from a fat JAR.
 * <p>
 * JavaFX checks that the main class extends {@code Application} when loaded via
 * the module system. By using a plain launcher, the fat JAR can be executed with
 * {@code java -jar sudoku.jar} without needing {@code --module-path}.
 * </p>
 * <p>Usage:</p>
 * <ul>
 *     <li>{@code java -jar sudoku.jar} — launches the Sudoku Game (default)</li>
 *     <li>{@code java -jar sudoku.jar --editor} — launches the Sudoku Editor</li>
 *     <li>{@code java -jar sudoku.jar --console} — launches the Console App</li>
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
