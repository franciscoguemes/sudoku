package com.franciscoguemes.sudoku.model;

import java.util.*;

public class Generator {

    public Puzzle generateRandomSudoku(PuzzleType puzzleType) {
        Puzzle puzzle = new Puzzle(puzzleType);
        Puzzle copy = new Puzzle(puzzle);

        Random randomGenerator = new Random();
        int[] possibleValues = getPossibleValuesInPuzzle(copy);

        //Initialize first column in the Sudoku
        List<Integer> valuesNotUsedYet = new ArrayList<>(Arrays.stream(possibleValues).boxed().toList());
        for(int r = 0; r < puzzleType.getRows(); r++) {
            int randomValue = randomGenerator.nextInt(valuesNotUsedYet.size());
            copy.makeMove(r, 0, valuesNotUsedYet.get(randomValue), true);
            valuesNotUsedYet.remove(randomValue);
        }

        //Initialize first row in the Sudoku
        HashSet<Integer> usedValuesInBox = new HashSet<>();
        final int BOX_HEIGHT = puzzleType.getBoxHeight();
        for(int r=0; r<BOX_HEIGHT; r++){
            usedValuesInBox.add(copy.getValue(r,0));
        }
        valuesNotUsedYet = new ArrayList<>(Arrays.stream(possibleValues).filter(value -> !usedValuesInBox.contains(value)).boxed().toList());
        final int BOX_WIDTH = puzzleType.getBoxWidth();
        for(int c = 1; c < BOX_WIDTH; c++) {
            int randomValue = randomGenerator.nextInt(valuesNotUsedYet.size());
            copy.makeMove(0, c, valuesNotUsedYet.get(randomValue), true);
            valuesNotUsedYet.remove(randomValue);
        }
        valuesNotUsedYet.addAll(usedValuesInBox.stream().filter(value -> value!= copy.getValue(0,0)).toList());
        for(int c = BOX_WIDTH-1; c < puzzleType.getColumns(); c++) {
            int randomValue = randomGenerator.nextInt(valuesNotUsedYet.size());
            copy.makeMove(0, c, valuesNotUsedYet.get(randomValue), true);
            valuesNotUsedYet.remove(randomValue);
        }

        //Bottleneck here need to improve this so that way 16x16 puzzles can be generated
        backtrackSudokuSolver(0, 0, copy);

        int numberOfValuesToKeep = (int)(0.22222*(copy.getPuzzleType().getRows()*copy.getPuzzleType().getRows()));

        for(int i = 0;i < numberOfValuesToKeep;) {
            int randomRow = randomGenerator.nextInt(puzzleType.getRows());
            int randomColumn = randomGenerator.nextInt(puzzleType.getColumns());

            if(puzzle.isSlotAvailable(randomRow, randomColumn)) {
                puzzle.makeMove(randomRow, randomColumn, copy.getValue(randomRow, randomColumn), false);
                i++;
            }
        }

        return puzzle;
    }

    public boolean solve(Puzzle puzzle) {
        return backtrackSudokuSolver(0, 0, puzzle);
    }

    /**
     * Solves the sudoku puzzle
     * Pre-cond: r = 0,c = 0
     * Post-cond: solved puzzle
     * @param r: the current row
     * @param c: the current column
     * @return valid move or not or done
     * Responses: Erroneous data
     */
    private boolean backtrackSudokuSolver(int r,int c,Puzzle puzzle) {
        //If the move is not valid return false
        if(!puzzle.inRange(r,c)) {
            return false;
        }

        int[] possibleValues = getPossibleValuesInPuzzle(puzzle);

        //if the current space is empty
        if(puzzle.isSlotAvailable(r, c)) {

            //loop to find the correct value for the space
            for(int i = 0;i < possibleValues.length;i++) {

                //if the current number works in the space
                if(puzzle.isValidMove(r,c,possibleValues[i])){

                    //make the move
                    puzzle.makeMove(r, c, possibleValues[i], true);

                    //if puzzle solved return true
                    if(puzzle.boardFull()) {
                        return true;
                    }

                    //go to next move
                    if(puzzle.isLastRow(r)) {
                        if(backtrackSudokuSolver(0,c + 1,puzzle)) return true;
                    } else {
                        if(backtrackSudokuSolver(r + 1,c,puzzle)) return true;
                    }
                }
            }
        } else {  //if the current space is not empty
            //got to the next move
            if(puzzle.isLastRow(r)) {
                return backtrackSudokuSolver(0,c + 1,puzzle);
            } else {
                return backtrackSudokuSolver(r + 1,c,puzzle);
            }
        }

        //undo move
        puzzle.makeSlotEmpty(r, c);

        //backtrack
        return false;
    }

    private static int[] getPossibleValuesInPuzzle(Puzzle puzzle) {
        int minValue = puzzle.getPuzzleType().getMinValue();
        int maxValue = puzzle.getPuzzleType().getMaxValue();
        int [] possibleValues = new int[maxValue-minValue+1];
        for(int i = 0; i< possibleValues.length; i++){
            possibleValues[i]=minValue+i;
        }
        return possibleValues;
    }
}
