package com.franciscoguemes.sudoku.solver.board;

/**
 * The sudoku board
 * 
 * @author francisco
 *
 */
public class Board {
	
	
	public static final int ROW_LENGTH = Value.values().length;
	public static final int COL_LENGTH = Value.values().length;
	private static final int SECTOR_ROWS = 3;
	private static final int SECTOR_COLUMNS = 3;
	

	private Cell [][] cells;
	
	private Sector [][] sectors;
	
	public Board () {
		this.cells = new Cell[ROW_LENGTH][COL_LENGTH];
		this.sectors = new Sector[SECTOR_ROWS][SECTOR_COLUMNS];
		
		// Initialize the sector matrix...
		for(int row=0 ; row< SECTOR_ROWS ; row++) {
			for(int col=0; col<SECTOR_COLUMNS; col++) {
				this.sectors[row][col]=new Sector();
			}
		}
		
		// Initialize the cell matrix...
		for(int row=0 ; row< ROW_LENGTH ; row++) {
			for(int col=0; col<COL_LENGTH; col++) {
				Cell cell = new Cell(this, row, col); 
				cells[row][col] = cell; 
				// Place the cell in the sector
				sectors[toSectorRow(row)][toSectorColumn(col)].placeCell(cell);
			}
		}
	}
	
	private int toSectorRow(int row) {
		return row/Sector.ROW_LENGTH ;
	}
	
	private int toSectorColumn(int col) {
		return col/Sector.COL_LENGTH;
	}
	
	public void updateCandidates(Cell cell) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isComplete() {
		for(int row=0 ; row< ROW_LENGTH ; row++) {
			for(int col=0; col<COL_LENGTH; col++) {
				if(cells[row][col].isEmpty()) {
					// TODO: Debug here which cell you want to print as empty ?
					return false;
				}
			}
		}
		return true;
	}

	class Sector {
		
		public static final int ROW_LENGTH = 3;
		public static final int COL_LENGTH = 3;
		
		private Cell [][] cells;
		
		public Sector() {
			this.cells = new Cell[ROW_LENGTH][COL_LENGTH];
		}

		public void placeCell(Cell cell) {
			cells[this.toSectorRow(cell.getRow())][this.toSectorColumn(cell.getCol())]=cell; 
		}
		
		private int toSectorRow(int row) {
			return row % ROW_LENGTH;
		}
		
		private int toSectorColumn(int col) {
			return col % COL_LENGTH;
		}
	}
	
}
