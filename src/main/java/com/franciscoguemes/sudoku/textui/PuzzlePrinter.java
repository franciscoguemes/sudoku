package com.franciscoguemes.sudoku.textui;

import com.franciscoguemes.sudoku.model.Puzzle;

public interface PuzzlePrinter {

    void print(Puzzle puzzle);

    String render(Puzzle puzzle);
}
