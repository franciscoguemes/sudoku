package com.franciscoguemes.sudoku.solver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import lombok.extern.slf4j.Slf4j;


@Slf4j
@SpringBootApplication
public class SudokuSolverApplication implements CommandLineRunner{

	private Solver solver;
	
	public SudokuSolverApplication(Solver solver) {
		this.solver=solver;
	}
	
	
	public static void main(String[] args) {
		SpringApplication.run(SudokuSolverApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("EXECUTING : command line runner");
		
        for (int i = 0; i < args.length; ++i) {
            log.info("args[{}]: {}", i, args[i]);
        }
        
        
        //TODO: Check here the arguments ....
        if(args.length==1) {
        	solver.solve(args[0]);
        }
        
		
	}

}

