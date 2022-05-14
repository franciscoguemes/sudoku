package com.franciscoguemes.sudoku.solver.board;

import java.util.Arrays;

/**
 * Represents each square in the board.
 * A square can have two states: Empty or Full, regarding it has been assigned a value or not.
 * 
 * @author francisco
 *
 */
public class Square {
	
	private int row;
	
	private int col;

	private Value value;
	
	private boolean [] candidates = new boolean [Value.values().length];
	
	public Square (int x, int y) {
		this.row=x;
		this.col=y;
		
		for(int i=0; i<candidates.length; i++) {
			candidates[i]=true;
		}
	}
	
	public boolean isEmpty() {
		return this.value==null;
	}
	
	public boolean isOccupied() {
		return !this.isEmpty();
	}
	
	public boolean isCandidate(Value value) {
		return this.candidates[value.ordinal()];
	}
	
	public Value getValue() {
		return this.value;
	}
	
//	public Value getValue2() throws EmptySquareException {
//		if(this.isEmpty()) {
//			String msg = this.getPrintableCoordinates() + ": Is empty. It has no value!";
//			throw new EmptySquareException(msg);
//		}
//		return this.value;
//	}
	
	public void setValue(Value value) {
		this.value = value;
	
		for(boolean candidate : candidates) {
			candidate=false;
		}
		
	}
	
//	public void setValue2(Value value) throws OccupiedSquareException, InvalidCandidateValueForSquareException {
//		if( this.isOccupied() ) {
//			String msg = this.getPrintableCoordinates() + ": Is already occupied with the value \"" + this.value.getLabel()  + "\"";
//			throw new OccupiedSquareException(msg);
//		}
//		
//		if(!this.isCandidate(value)) {
//			String msg = this.getPrintableCoordinates() + ": The supplied value \"" + value.getLabel()  + "\" is an invalid candidate for the square.";
//			throw new InvalidCandidateValueForSquareException(msg);
//		}
//		
//		this.value = value;
//	
//		for(boolean candidate : candidates) {
//			candidate=false;
//		}
//		
//	}
	
	/**
	 * Update the given candidate value to false.
	 * 
	 * @param value The candidate value to be updated.
	 */
	public void updateCandidate(Value value) {
		this.candidates[value.ordinal()]=false;
	}
	
	
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
	protected String getPrintableCoordinates() {
		StringBuilder sb = new StringBuilder();
		sb.append("Square [row=")
		.append(this.getRow())
		.append("][col=")
		.append(this.getCol())
		.append("]");
		return sb.toString();
	}
	
}
