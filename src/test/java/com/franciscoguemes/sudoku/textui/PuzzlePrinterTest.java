package com.franciscoguemes.sudoku.textui;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.model.PuzzleType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PuzzlePrinter Tests")
class PuzzlePrinterTest {

    @Nested
    @DisplayName("Internal values mode (InternalValuesPuzzlePrinter)")
    class InternalValuesMode {

        private final PuzzlePrinter printer = new InternalValuesPuzzlePrinter();

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

    @Nested
    @DisplayName("Display values mode (DisplayValuesPuzzlePrinter)")
    class DisplayValuesMode {

        private final PuzzlePrinter printer = new StandardPuzzlePrinter();

        @Test
        @DisplayName("Renders 9x9 puzzle identically to internal mode")
        void testRender9x9() {
            Puzzle puzzle = new Puzzle(PuzzleType.SUDOKU);
            puzzle.makeMove(0, 0, 8, false);
            puzzle.makeMove(1, 2, 3, true);
            puzzle.makeMove(1, 3, 6, true);

            String rendered = printer.render(puzzle);

            assertTrue(rendered.contains("+-------+-------+-------+"));
            assertTrue(rendered.contains("| 8 . . | . . . | . . . |"));
            assertTrue(rendered.contains("| . . 3 | 6 . . | . . . |"));
        }

        @Test
        @DisplayName("Renders values above 9 as letters (A=10, B=11, C=12)")
        void testValuesAbove9RenderedAsLetters() {
            Puzzle puzzle = new Puzzle(PuzzleType.BIG_SUDOKU);
            puzzle.makeMove(0, 0, 10, false);
            puzzle.makeMove(0, 1, 11, false);
            puzzle.makeMove(0, 4, 12, false);
            puzzle.makeMove(0, 8, 1, false);

            String rendered = printer.render(puzzle);

            assertTrue(rendered.contains("| A B . . | C . . . | 1 . . . |"));
        }

        @Test
        @DisplayName("Uses single-character cell width for 12x12 puzzles")
        void testSingleCharCellWidth12x12() {
            Puzzle puzzle = new Puzzle(PuzzleType.BIG_SUDOKU);

            String rendered = printer.render(puzzle);

            // cellWidth=1: segmentWidth = 4*(1+1)+1 = 9
            assertTrue(rendered.contains("+---------+---------+---------+"));
            assertTrue(rendered.contains("| . . . . | . . . . | . . . . |"));
        }

        @Test
        @DisplayName("Renders all letter values for 16x16 puzzles (A through G)")
        void testAllLetterValues16x16() {
            Puzzle puzzle = new Puzzle(PuzzleType.MAXI_SUDOKU);
            puzzle.makeMove(0, 0, 10, false);
            puzzle.makeMove(0, 1, 11, false);
            puzzle.makeMove(0, 2, 12, false);
            puzzle.makeMove(0, 3, 13, false);
            puzzle.makeMove(0, 4, 14, false);
            puzzle.makeMove(0, 5, 15, false);
            puzzle.makeMove(0, 6, 16, false);

            String rendered = printer.render(puzzle);

            assertTrue(rendered.contains("| A B C D | E F G . | . . . . | . . . . |"));
        }

        @Test
        @DisplayName("Empty cells render as dots")
        void testEmptyCellsAsDots() {
            Puzzle puzzle = new Puzzle(PuzzleType.BIG_SUDOKU);

            String rendered = printer.render(puzzle);

            assertTrue(rendered.contains("| . . . . | . . . . | . . . . |"));
            assertFalse(rendered.contains("0"));
        }
    }
}
