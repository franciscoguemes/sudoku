package com.franciscoguemes.sudoku.solver.board;

/**
 * The cell is empty, so no value can be retrieved.
 * 
 * @author francisco
 *
 */
public class EmptySquareException extends Exception {

	public EmptySquareException(String msg) {
		super(msg);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
