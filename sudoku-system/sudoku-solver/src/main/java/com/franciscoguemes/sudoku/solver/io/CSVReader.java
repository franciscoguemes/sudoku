package com.franciscoguemes.sudoku.solver.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.franciscoguemes.sudoku.solver.Solver;
import com.franciscoguemes.sudoku.solver.board.Board;
import com.franciscoguemes.sudoku.solver.board.Value;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CSVReader {


	private static final String EMPTY = "";

	public Board read(String filePath) {
		
		Board board=null;
		try {
			
			log.info("Reading the file: {}", filePath );
			//TODO: Improve the reading process
			List<List<String>> records = this.getRecords(filePath);
			this.printContent(records);
			
			// Purify and filter input
			String [][] values = filterInput(records);
			board = new Board(values);
			
			//TODO: Improve the error handling
		}catch(Exception e) {
			log.error("Error while reading the file {}. {}", filePath, e);
		}
		
		return board;
	}
	
	private String[][] filterInput(List<List<String>> records) {

		String[][] values = new String[records.size()][];
		
		for(int row =0; row < records.size(); row++) {
			List<String> columns = records.get(row);
			values[row] = new String[columns.size()];
			for(int col=0; col < columns.size(); col++) {
				String value = columns.get(col);
				if(Value.isValidValue(value)){
					values[row][col]=value;
				}else {
					values[row][col]= EMPTY;
				}
			}
		}
		
		return values;
	}

	private void printContent(List<List<String>> records) {
		for(List<String> record : records) {
			for(String value : record) {
				System.out.print(value);
				System.out.print(",");
			}
			System.out.println();
		}
	}
	
	private static final Pattern COMMA_DELIMITER = Pattern.compile(",");

	private List<List<String>> getRecords(String filePath) throws FileNotFoundException {
		List<List<String>> records = new ArrayList<>();
		try (Scanner scanner = new Scanner(new File(filePath));) {
			while (scanner.hasNextLine()) {
				records.add(this.getRecordFromLine(scanner.nextLine()));
			}
		}
		return records;
	}

	private List<String> getRecordFromLine(String line) {
		List<String> values = new ArrayList<String>();
		try (Scanner rowScanner = new Scanner(line)) {
			rowScanner.useDelimiter(COMMA_DELIMITER);
			while (rowScanner.hasNext()) {
				values.add(rowScanner.next());
			}
		}
		return values;
	}


}
