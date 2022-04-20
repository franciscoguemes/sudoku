package com.franciscoguemes.sudoku.board;

/**
 * Represents each cell in the board.
 * A cell can have two states: Empty or Full, regarding it has been assigned a value or not.
 * 
 * @author francisco
 *
 */
public class Cell {

	Board board;
	
	private int row;
	
	private int col;

	Value value;
	
	private boolean [] candidates = new boolean [Value.values().length];
	
	public Cell (Board board, int x, int y) {
		this.board=board;
		this.row=x;
		this.col=y;
		for(boolean b : candidates){
			b=true;
		}
	}
	
//	public Cell(Value value) {
//		this.value=value;
//	}
	
	public boolean isEmpty() {
		return this.value==null;
	}
	
	public boolean isOccupied() {
		return !this.isEmpty();
	}
	
	public boolean isCandidate(Value value) {
		return this.candidates[value.ordinal()];
	}
	
	public Value getValue() throws EmptyCellException {
		if(this.isEmpty()) {
			throw new EmptyCellException();
		}
		return this.value;
	}
	
	public void setValue(Value value) throws OccupiedCellException, InvalidCellValueException {
		if( this.isOccupied() ) {
			throw new OccupiedCellException();
		}
		
		if(!this.isCandidate(value)) {
			throw new InvalidCellValueException();
		}
		
		this.value = value;
		this.board.updateCandidates(this);
	}
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
}
