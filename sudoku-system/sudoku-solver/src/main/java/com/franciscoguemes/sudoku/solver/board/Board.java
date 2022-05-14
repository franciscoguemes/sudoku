package com.franciscoguemes.sudoku.solver.board;

import com.franciscoguemes.sudoku.solver.io.CSVReader;

import lombok.extern.slf4j.Slf4j;

/**
 * The sudoku board
 * 
 * @author francisco
 *
 */
@Slf4j
public class Board implements InitializedBoard {

	public static final int ROW_LENGTH = Value.values().length;
	public static final int COL_LENGTH = Value.values().length;
	private static final int SECTOR_ROWS = 3;
	private static final int SECTOR_COLUMNS = 3;

	private Square[][] squares;

	private Sector[][] sectors;

	public Board() {
		this.squares = new Square[ROW_LENGTH][COL_LENGTH];
		this.sectors = new Sector[SECTOR_ROWS][SECTOR_COLUMNS];

		// Initialize the sector matrix...
		for (int row = 0; row < SECTOR_ROWS; row++) {
			for (int col = 0; col < SECTOR_COLUMNS; col++) {
				this.sectors[row][col] = new Sector();
			}
		}

		// Initialize the square matrix...
		for (int row = 0; row < ROW_LENGTH; row++) {
			for (int col = 0; col < COL_LENGTH; col++) {
				Square cell = new Square(row, col);
				squares[row][col] = cell;
				// Place the square in the sector
				sectors[toSectorRow(row)][toSectorColumn(col)].placeCell(cell);
			}
		}
	}

	public Board(String[][] values) {
		this();
		this.initialize(values);
	}

	private int toSectorRow(int row) {
		return row / Sector.ROW_LENGTH;
	}

	private int toSectorColumn(int col) {
		return col / Sector.COL_LENGTH;
	}

	private void initialize(String[][] values) {
		try {
			for (int row = 0; row < Board.ROW_LENGTH; row++) {
				for (int col = 0; col < Board.COL_LENGTH; col++) {
					String value = values[row][col];
					if (org.springframework.util.StringUtils.hasText(value)) {
						this.setValue(Value.valueOfLabel(value), row, col);
					}
				}
			}
		} catch (Exception e) {
			log.error("An error happened during the initialization of the board", e);
			throw new IllegalStateException("An error happened during the initialization of the board", e);
		}
	}

	@Override
	public void setValue(Value value, int row, int col) throws OccupiedSquareException, InvalidCandidateValueForSquareException {
		Square square = this.squares[row][col];
		
		if( square.isOccupied() ) {
			String msg = square.getPrintableCoordinates() + ": Is already occupied with the value \"" + square.getValue().getLabel()  + "\"";
			throw new OccupiedSquareException(msg);
		}
		
		if(!square.isCandidate(value)) {
			String msg = square.getPrintableCoordinates() + ": The supplied value \"" + value.getLabel()  + "\" is an invalid candidate for the square.";
			throw new InvalidCandidateValueForSquareException(msg);
		}
		
		square.setValue(value);
		this.updateCandidates(square);
	}

	private void updateCandidates(Square square) {

		try {

			// Update candidates in row
			for (int col = 0; col < COL_LENGTH; col++) {
				this.squares[square.getRow()][col].removeCandidate(square.getValue());
			}

			// Update candidates in column
			for (int row = 0; row < Board.ROW_LENGTH; row++) {
				this.squares[row][square.getCol()].removeCandidate(square.getValue());
			}

			// Update candidates in sector
			sectors[toSectorRow(square.getRow())][toSectorColumn(square.getCol())].updateCandidates(square.getValue());

		} catch (Exception e) {
			log.error("An error happened during the update of the candidates", e);
			throw new IllegalStateException("An error happened during the update of the candidates in the board", e);
		}
	}

	@Override
	public boolean isComplete() {
		for (int row = 0; row < ROW_LENGTH; row++) {
			for (int col = 0; col < COL_LENGTH; col++) {
				if (squares[row][col].isEmpty()) {
					log.debug("The square [{}][{}] is empty", row,col);
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean hasValue(int row, int col) {
		return this.squares[row][col].isOccupied();
	}

	@Override
	public Value getValue(int row, int col) throws EmptySquareException {
		Square square = this.squares[row][col];
		if(square.isEmpty()) {
			String msg = square.getPrintableCoordinates() + ": Is empty. It has no value!";
			throw new EmptySquareException(msg);
		}
		return square.getValue();
	}

	class Sector {

		public static final int ROW_LENGTH = 3;
		public static final int COL_LENGTH = 3;

		private Square[][] squares;

		public Sector() {
			this.squares = new Square[ROW_LENGTH][COL_LENGTH];
		}

		public void placeCell(Square square) {
			squares[this.toSectorRow(square.getRow())][this.toSectorColumn(square.getCol())] = square;
		}

		private int toSectorRow(int row) {
			return row % ROW_LENGTH;
		}

		private int toSectorColumn(int col) {
			return col % COL_LENGTH;
		}

		public void updateCandidates(Value value) {
			for(int row=0; row < ROW_LENGTH; row++) {
				for(int col=0; col < COL_LENGTH; col++) {
					this.squares[row][col].removeCandidate(value);
				}
			}
		}

	}

}
