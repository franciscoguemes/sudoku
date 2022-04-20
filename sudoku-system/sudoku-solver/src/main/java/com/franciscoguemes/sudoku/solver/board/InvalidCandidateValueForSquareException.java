package com.franciscoguemes.sudoku.solver.board;

/**
 * You are trying to assign an invalid value to the square.
 * 
 * @author francisco
 *
 */
public class InvalidCandidateValueForSquareException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidCandidateValueForSquareException(String message) {
		super(message);
	}

}
