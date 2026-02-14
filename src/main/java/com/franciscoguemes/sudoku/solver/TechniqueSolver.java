package com.franciscoguemes.sudoku.solver;

import com.franciscoguemes.sudoku.model.Puzzle;
import com.franciscoguemes.sudoku.solver.techniques.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TechniqueSolver implements SudokuSolver {

    private static final Logger LOG = LoggerFactory.getLogger(TechniqueSolver.class);

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
        LOG.info("Solving puzzle (maxLevel={})", maxLevel);
        CandidateGrid grid = new CandidateGrid(puzzle);
        TechniqueLevel hardest = null;

        while (!grid.isSolved()) {
            boolean progress = false;
            for (SolvingTechnique tech : techniques) {
                if (tech.getLevel().getRank() > maxLevel.getRank()) continue;
                LOG.trace("Attempting technique: {}", tech.getName());
                if (tech.apply(grid)) {
                    LOG.debug("Technique applied: {}", tech.getName());
                    progress = true;
                    if (hardest == null || tech.getLevel().getRank() > hardest.getRank()) {
                        hardest = tech.getLevel();
                    }
                    break; // restart from easiest technique
                }
            }
            if (!progress) break;
        }

        SolveResult result = new SolveResult(grid.isSolved(), hardest);
        LOG.info("Solve result: solved={}, hardestLevel={}", result.solved(), result.hardestTechnique());
        return result;
    }

    @Override
    public boolean solve(Puzzle puzzle) {
        return solve(puzzle, TechniqueLevel.ADVANCED).solved();
    }

    public SolveResult grade(Puzzle puzzle) {
        LOG.info("Grading puzzle difficulty");
        SolveResult result = solve(puzzle, TechniqueLevel.ADVANCED);
        if (!result.solved()) {
            LOG.info("Puzzle requires backtracking (not solvable with techniques alone)");
            return new SolveResult(false, TechniqueLevel.BACKTRACKING);
        }
        LOG.info("Puzzle graded: hardestLevel={}", result.hardestTechnique());
        return result;
    }
}
