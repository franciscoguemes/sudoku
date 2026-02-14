package com.franciscoguemes.sudoku.model;

import com.franciscoguemes.sudoku.solver.BacktrackingSolver;
import com.franciscoguemes.sudoku.solver.SolveResult;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import com.franciscoguemes.sudoku.solver.TechniqueSolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TechniqueGradedGenerator implements Generator {

    private static final Logger LOG = LoggerFactory.getLogger(TechniqueGradedGenerator.class);

    private final TechniqueSolver techniqueSolver;

    public TechniqueGradedGenerator() {
        this.techniqueSolver = new TechniqueSolver();
    }

    @Override
    public Puzzle generate(PuzzleType puzzleType, Difficulty difficulty) {
        LOG.info("Generating technique-graded {} puzzle, difficulty={}", puzzleType, difficulty);

        TechniqueLevel maxLevel = difficulty.getTechniqueLevel();

        Puzzle solved = Generator.generateFullSolution(puzzleType, new BacktrackingSolver());

        List<int[]> positions = Generator.shuffledPositions(puzzleType);

        for (int[] pos : positions) {
            int r = pos[0], c = pos[1];
            int savedValue = solved.getValue(r, c);
            if (savedValue == Puzzle.NO_VALUE) continue;

            solved.makeSlotEmpty(r, c);

            // Build a test puzzle with current clues as immutable
            Puzzle test = Generator.buildImmutablePuzzle(puzzleType, solved);
            SolveResult result = techniqueSolver.solve(test, maxLevel);

            if (!result.solved()) {
                // Removal makes it too hard — restore
                solved.makeMove(r, c, savedValue, true);
            }
        }

        return Generator.buildImmutablePuzzle(puzzleType, solved);
    }
}
