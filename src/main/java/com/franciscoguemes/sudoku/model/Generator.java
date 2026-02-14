package com.franciscoguemes.sudoku.model;

import com.franciscoguemes.sudoku.solver.BacktrackingSolver;
import com.franciscoguemes.sudoku.solver.SolveResult;
import com.franciscoguemes.sudoku.solver.SudokuSolver;
import com.franciscoguemes.sudoku.solver.TechniqueLevel;
import com.franciscoguemes.sudoku.solver.TechniqueSolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Generator {

    private static final Logger LOG = LoggerFactory.getLogger(Generator.class);

    private final SudokuSolver solver;

    public Generator() {
        this(new BacktrackingSolver());
    }

    public Generator(SudokuSolver solver) {
        this.solver = solver;
    }

    public Puzzle generateRandomSudoku(PuzzleType puzzleType) {
        return generateRandomSudoku(puzzleType, Difficulty.MEDIUM);
    }

    public Puzzle generateRandomSudoku(PuzzleType puzzleType, Difficulty difficulty) {
        LOG.info("Generating random {} puzzle, difficulty={}", puzzleType, difficulty);
        Puzzle solved = generateFullSolution(puzzleType);
        int totalCells = puzzleType.getRows() * puzzleType.getColumns();
        int targetClues = difficulty.getTargetClueCount(puzzleType);

        // Build a list of all cell positions and shuffle
        List<int[]> positions = new ArrayList<>();
        for (int r = 0; r < puzzleType.getRows(); r++) {
            for (int c = 0; c < puzzleType.getColumns(); c++) {
                positions.add(new int[]{r, c});
            }
        }
        Collections.shuffle(positions);

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

    public Puzzle generateTechniqueGradedSudoku(PuzzleType puzzleType, Difficulty difficulty) {
        LOG.info("Generating technique-graded {} puzzle, difficulty={}", puzzleType, difficulty);
        // For EXTREME: use the existing clue-count method (backtracking is allowed)
        if (difficulty == Difficulty.EXTREME) {
            return generateRandomSudoku(puzzleType, difficulty);
        }

        TechniqueSolver solver = new TechniqueSolver();
        TechniqueLevel maxLevel = difficulty.getTechniqueLevel();

        Puzzle solved = generateFullSolution(puzzleType);
        int totalCells = puzzleType.getRows() * puzzleType.getColumns();

        List<int[]> positions = new ArrayList<>();
        for (int r = 0; r < puzzleType.getRows(); r++) {
            for (int c = 0; c < puzzleType.getColumns(); c++) {
                positions.add(new int[]{r, c});
            }
        }
        Collections.shuffle(positions);

        for (int[] pos : positions) {
            int r = pos[0], c = pos[1];
            int savedValue = solved.getValue(r, c);
            if (savedValue == Puzzle.NO_VALUE) continue;

            solved.makeSlotEmpty(r, c);

            // Build a test puzzle with current clues as immutable
            Puzzle test = buildImmutablePuzzle(puzzleType, solved);
            SolveResult result = solver.solve(test, maxLevel);

            if (result.solved()) {
                // Removal is accepted — puzzle is still solvable at this level
            } else {
                // Removal makes it too hard — restore
                solved.makeMove(r, c, savedValue, true);
            }
        }

        return buildImmutablePuzzle(puzzleType, solved);
    }

    private Puzzle buildImmutablePuzzle(PuzzleType type, Puzzle source) {
        Puzzle result = new Puzzle(type);
        for (int r = 0; r < type.getRows(); r++) {
            for (int c = 0; c < type.getColumns(); c++) {
                int v = source.getValue(r, c);
                if (v != Puzzle.NO_VALUE) result.makeMove(r, c, v, false);
            }
        }
        return result;
    }

    public boolean solve(Puzzle puzzle) {
        return solver.solve(puzzle);
    }

    private Puzzle generateFullSolution(PuzzleType puzzleType) {
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
