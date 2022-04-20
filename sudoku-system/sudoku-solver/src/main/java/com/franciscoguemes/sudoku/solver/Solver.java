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
		
		while(!board.isComplete()) {
			
		}
		
	}
	
	
	


}
