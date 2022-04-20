package com.franciscoguemes.sudoku.solver;

import org.springframework.stereotype.Component;

import com.franciscoguemes.sudoku.solver.board.InitializedBoard;
import com.franciscoguemes.sudoku.solver.io.CSVReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Solver {
	
	private CSVReader reader;
	
	public Solver(CSVReader reader) {
		this.reader = reader;
	}
	
	public void solve(String filePath) {
		
		
		InitializedBoard board = reader.read(filePath);
		
		// Get the number of empty cells and set it to the number of iterations instead 99
		int iterations = 99;
		
		while(!board.isComplete() && iterations > 0) {
			
			// Find next square with a single candidate
			
			// Setup the value of the square to the single candidate
			
			// If there are no square with a single candidate
			
			// Find a candidate that is unique in a row, a column or a sector
			
			// Set the value of that cell to the candidate
			
			// decrease the number of iterations
			iterations --;
		}
		
	}
	
	
	


}
