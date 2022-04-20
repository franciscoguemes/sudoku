package com.franciscoguemes.sudoku.solver.board;

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
		for(boolean b : candidates){
			b=true;
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
	
	public Value getValue() throws EmptySquareException {
		if(this.isEmpty()) {
			String msg = this.getPrintableCoordinates() + ": Is empty. It has no value!";
			throw new EmptySquareException(msg);
		}
		return this.value;
	}
	
	public void setValue(Value value) throws OccupiedSquareException, InvalidCandidateValueForSquareException {
		if( this.isOccupied() ) {
			String msg = this.getPrintableCoordinates() + ": Is already occupied with the value \"" + this.value.getLabel()  + "\"";
			throw new OccupiedSquareException(msg);
		}
		
		if(!this.isCandidate(value)) {
			String msg = this.getPrintableCoordinates() + ": The supplied value \"" + value.getLabel()  + "\" is an invalid candidate for the square.";
			throw new InvalidCandidateValueForSquareException(msg);
		}
		
		this.value = value;
	
		for(boolean candidate : candidates) {
			candidate=false;
		}
		
	}
	
	public void updateCandidates(Value value) {
		this.candidates[value.ordinal()]=false;
	}
	
	
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
	private String getPrintableCoordinates() {
		StringBuilder sb = new StringBuilder();
		sb.append("Square [row=")
		.append(this.getRow())
		.append("][col=")
		.append(this.getCol())
		.append("]");
		return sb.toString();
	}
	
}
