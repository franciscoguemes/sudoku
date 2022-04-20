package com.franciscoguemes.sudoku.solver.board;

/**
 * The cell already has a value so no other value can be assigned.
 * 
 * @author francisco
 *
 */
public class OccupiedSquareException extends Exception {

	public OccupiedSquareException(String msg) {
		super(msg);
	}

}
