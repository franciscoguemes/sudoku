package com.franciscoguemes.sudoku.textui;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PuzzlePrinter Tests")
class PuzzlePrinterTest {

    private final PuzzlePrinter printer = new PuzzlePrinter();

    @Test
    @DisplayName("Renders 9x9 puzzle with correct grid lines and values")
    void testRender9x9() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
        puzzle.makeMove(0, 0, 8, false);
        puzzle.makeMove(1, 2, 3, true);
        puzzle.makeMove(1, 3, 6, true);
        puzzle.makeMove(2, 1, 7, false);
        puzzle.makeMove(2, 4, 9, false);

        String rendered = printer.render(puzzle);

        assertTrue(rendered.contains("+-------+-------+-------+"));
        assertTrue(rendered.contains("| 8 . . | . . . | . . . |"));
        assertTrue(rendered.contains("| . . 3 | 6 . . | . . . |"));
        assertTrue(rendered.contains("| . 7 . | . 9 . | . . . |"));
    }

    @Test
    @DisplayName("Renders empty 9x9 puzzle with all dots")
    void testRenderEmpty9x9() {
        Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);

        String rendered = printer.render(puzzle);

        assertTrue(rendered.contains("| . . . | . . . | . . . |"));
        assertFalse(rendered.contains("0"));
    }

    @Test
    @DisplayName("Renders 6x6 mini puzzle with correct grid lines")
    void testRender6x6() {
        Puzzle puzzle = new Puzzle(PuzzleType.MINI_SUDOKU);
        puzzle.makeMove(0, 0, 5, false);
        puzzle.makeMove(0, 3, 2, false);

        String rendered = printer.render(puzzle);

        assertTrue(rendered.contains("+-------+-------+"));
        assertTrue(rendered.contains("| 5 . . | 2 . . |"));
    }

    @Test
    @DisplayName("Renders 12x12 puzzle with two-digit cell width")
    void testRender12x12() {
        Puzzle puzzle = new Puzzle(PuzzleType.BIG_SUDOKU);
        puzzle.makeMove(0, 0, 12, false);
        puzzle.makeMove(0, 4, 1, false);

        String rendered = printer.render(puzzle);

        assertTrue(rendered.contains("+-------------+-------------+-------------+"));
        assertTrue(rendered.contains("12"));
        assertTrue(rendered.contains(" 1"));
    }
}
