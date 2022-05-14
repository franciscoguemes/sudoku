package com.franciscoguemes.sudoku.solver.board;

public interface InitializedBoard {
	
	public boolean isComplete();
	
	public void setValue(Value value, int row, int col) throws OccupiedSquareException, InvalidCandidateValueForSquareException;
	
	public boolean hasValue(int row, int col);
	
	public Value getValue(int row, int col) throws EmptySquareException;
	

//	public Tuple<int, int> getClosestSquareSingleCandidate(int x, int y);

}
