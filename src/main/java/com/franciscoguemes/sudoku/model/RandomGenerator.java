package com.franciscoguemes.sudoku.model;

import com.franciscoguemes.sudoku.solver.BacktrackingSolver;
import com.franciscoguemes.sudoku.solver.SudokuSolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RandomGenerator implements Generator {

    private static final Logger LOG = LoggerFactory.getLogger(RandomGenerator.class);

    private final SudokuSolver solver;

    public RandomGenerator(SudokuSolver solver) {
        this.solver = solver;
    }

    @Override
    public Puzzle generate(PuzzleType puzzleType, Difficulty difficulty) {
        LOG.info("Generating random {} puzzle, difficulty={}", puzzleType, difficulty);
        Puzzle solved = Generator.generateFullSolution(puzzleType, solver);
        int totalCells = puzzleType.getRows() * puzzleType.getColumns();
        int targetClues = difficulty.getTargetClueCount(puzzleType);

        List<int[]> positions = Generator.shuffledPositions(puzzleType);

        // Remove cells one by one while preserving unique solution
        int currentClues = totalCells;
        for (int[] pos : positions) {
            if (currentClues <= targetClues) {
                break;
            }
            int r = pos[0];
            int c = pos[1];
            int savedValue = solved.getValue(r, c);
            if (savedValue == Puzzle.NO_VALUE) {
                continue;
            }
            solved.makeSlotEmpty(r, c);
            if (countSolutions(solved, 2) == 1) {
                currentClues--;
                LOG.debug("Removed cell [{},{}] (value={}), clues remaining={}", r, c, savedValue, currentClues);
            } else {
                // Restore — removal breaks uniqueness
                solved.makeMove(r, c, savedValue, true);
                LOG.debug("Restored cell [{},{}] (value={}) — removal breaks uniqueness", r, c, savedValue);
            }
        }

        // Build the final puzzle with remaining clues marked immutable
        Puzzle result = new Puzzle(puzzleType);
        int finalClues = 0;
        for (int r = 0; r < puzzleType.getRows(); r++) {
            for (int c = 0; c < puzzleType.getColumns(); c++) {
                int value = solved.getValue(r, c);
                if (value != Puzzle.NO_VALUE) {
                    result.makeMove(r, c, value, false);
                    finalClues++;
                }
            }
        }
        LOG.info("Generated {} puzzle with {} clues (target={})", puzzleType, finalClues, targetClues);
        return result;
    }

    private int countSolutions(Puzzle puzzle, int maxCount) {
        Puzzle copy = new Puzzle(puzzle);
        int[] count = {0};
        countSolutionsRecursive(0, 0, copy, count, maxCount);
        return count[0];
    }

    private void countSolutionsRecursive(int r, int c, Puzzle puzzle, int[] count, int maxCount) {
        if (count[0] >= maxCount) {
            return;
        }

        if (!puzzle.inRange(r, c)) {
            return;
        }

        int nextR, nextC;
        if (puzzle.isLastRow(r)) {
            nextR = 0;
            nextC = c + 1;
        } else {
            nextR = r + 1;
            nextC = c;
        }

        if (puzzle.getValue(r, c) != Puzzle.NO_VALUE) {
            countSolutionsRecursive(nextR, nextC, puzzle, count, maxCount);
            return;
        }

        int[] possibleValues = puzzle.getPuzzleType().getPossibleValuesInPuzzle();
        for (int value : possibleValues) {
            if (puzzle.isValidMove(r, c, value)) {
                puzzle.makeMove(r, c, value, true);
                if (puzzle.boardFull()) {
                    count[0]++;
                    puzzle.makeSlotEmpty(r, c);
                    if (count[0] >= maxCount) {
                        return;
                    }
                    continue;
                }
                countSolutionsRecursive(nextR, nextC, puzzle, count, maxCount);
                puzzle.makeSlotEmpty(r, c);
                if (count[0] >= maxCount) {
                    return;
                }
            }
        }
    }
}
