package com.franciscoguemes.sudoku.solver;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.solver.techniques.*;

import java.util.List;

public class TechniqueSolver {

    private final List<SolvingTechnique> techniques;

    public TechniqueSolver() {
        techniques = List.of(
            new NakedSingle(),
            new HiddenSingle(),
            new NakedPair(),
            new NakedTriple(),
            new PointingPair(),
            new BoxLineReduction(),
            new XWing(),
            new Swordfish(),
            new XYWing(),
            new UniqueRectangle()
        );
    }

    public SolveResult solve(Puzzle puzzle, TechniqueLevel maxLevel) {
        CandidateGrid grid = new CandidateGrid(puzzle);
        TechniqueLevel hardest = null;

        while (!grid.isSolved()) {
            boolean progress = false;
            for (SolvingTechnique tech : techniques) {
                if (tech.getLevel().getRank() > maxLevel.getRank()) continue;
                if (tech.apply(grid)) {
                    progress = true;
                    if (hardest == null || tech.getLevel().getRank() > hardest.getRank()) {
                        hardest = tech.getLevel();
                    }
                    break; // restart from easiest technique
                }
            }
            if (!progress) break;
        }

        return new SolveResult(grid.isSolved(), hardest);
    }

    public SolveResult solve(Puzzle puzzle) {
        return solve(puzzle, TechniqueLevel.ADVANCED);
    }

    public SolveResult grade(Puzzle puzzle) {
        SolveResult result = solve(puzzle, TechniqueLevel.ADVANCED);
        if (!result.solved()) {
            return new SolveResult(false, TechniqueLevel.BACKTRACKING);
        }
        return result;
    }
}
