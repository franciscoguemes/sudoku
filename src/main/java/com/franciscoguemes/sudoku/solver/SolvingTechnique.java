package com.franciscoguemes.sudoku.solver;

public interface SolvingTechnique {
    TechniqueLevel getLevel();
    boolean apply(CandidateGrid grid);
    String getName();
}
