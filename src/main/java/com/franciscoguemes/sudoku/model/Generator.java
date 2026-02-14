package com.franciscoguemes.sudoku.model;

import com.franciscoguemes.sudoku.solver.SudokuSolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public interface Generator {

    Logger LOG = LoggerFactory.getLogger(Generator.class);

    Puzzle generate(PuzzleType puzzleType, Difficulty difficulty);

    static Puzzle generateFullSolution(PuzzleType puzzleType, SudokuSolver solver) {
        LOG.debug("Generating full solution for {}", puzzleType);
        Puzzle puzzle = new Puzzle(puzzleType);
        Random randomGenerator = new Random();
        int[] possibleValues = puzzle.getPuzzleType().getPossibleValuesInPuzzle();

        // Initialize first column
        List<Integer> valuesNotUsedYet = new ArrayList<>(Arrays.stream(possibleValues).boxed().toList());
        for (int r = 0; r < puzzleType.getRows(); r++) {
            int randomValue = randomGenerator.nextInt(valuesNotUsedYet.size());
            puzzle.makeMove(r, 0, valuesNotUsedYet.get(randomValue), true);
            valuesNotUsedYet.remove(randomValue);
        }

        // Initialize first row
        HashSet<Integer> usedValuesInBox = new HashSet<>();
        final int BOX_HEIGHT = puzzleType.getBoxHeight();
        for (int r = 0; r < BOX_HEIGHT; r++) {
            usedValuesInBox.add(puzzle.getValue(r, 0));
        }
        valuesNotUsedYet = new ArrayList<>(Arrays.stream(possibleValues).filter(value -> !usedValuesInBox.contains(value)).boxed().toList());
        final int BOX_WIDTH = puzzleType.getBoxWidth();
        for (int c = 1; c < BOX_WIDTH; c++) {
            int randomValue = randomGenerator.nextInt(valuesNotUsedYet.size());
            puzzle.makeMove(0, c, valuesNotUsedYet.get(randomValue), true);
            valuesNotUsedYet.remove(randomValue);
        }
        valuesNotUsedYet.addAll(usedValuesInBox.stream().filter(value -> value != puzzle.getValue(0, 0)).toList());
        for (int c = BOX_WIDTH; c < puzzleType.getColumns(); c++) {
            int randomValue = randomGenerator.nextInt(valuesNotUsedYet.size());
            puzzle.makeMove(0, c, valuesNotUsedYet.get(randomValue), true);
            valuesNotUsedYet.remove(randomValue);
        }

        // Bottleneck here need to improve this so that way 16x16 puzzles can be generated
        LOG.debug("Starting solver for full solution ({})", puzzleType);
        solver.solve(puzzle);
        LOG.debug("Full solution generated for {}", puzzleType);

        return puzzle;
    }

    static Puzzle buildImmutablePuzzle(PuzzleType type, Puzzle source) {
        Puzzle result = new Puzzle(type);
        for (int r = 0; r < type.getRows(); r++) {
            for (int c = 0; c < type.getColumns(); c++) {
                int v = source.getValue(r, c);
                if (v != Puzzle.NO_VALUE) result.makeMove(r, c, v, false);
            }
        }
        return result;
    }

    static List<int[]> shuffledPositions(PuzzleType puzzleType) {
        List<int[]> positions = new ArrayList<>();
        for (int r = 0; r < puzzleType.getRows(); r++) {
            for (int c = 0; c < puzzleType.getColumns(); c++) {
                positions.add(new int[]{r, c});
            }
        }
        Collections.shuffle(positions);
        return positions;
    }
}
