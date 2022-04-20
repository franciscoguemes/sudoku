package com.franciscoguemes.sudoku.board;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the Value that each cell can have in the board.
 * 
 * @author francisco
 *
 */
public enum Value {
	
	ONE(1),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5),
	SIX(6),
	SEVEN(7),
	EIGHT(8),
	NINE(9);
	
    private static final Map<String, Value> BY_LABEL = new HashMap<>();
    private static final Map<Integer, Value> BY_NUMERICAL_VALUE = new HashMap<>();
	
	static {
        for (Value v : values()) {
        	BY_NUMERICAL_VALUE.put(v.getNumericalValue(),v);
            BY_LABEL.put(v.getLabel(), v);
        }
    }
	
	private int numericalValue;
	
	private Value(int i) {
		this.numericalValue=i;
	}
	
	public int getNumericalValue() {
		return this.numericalValue;
	}
	
	public String getLabel() {
		return Integer.toString(this.getNumericalValue());
	}
	
	public static Value valueOfNumericalValue(int i) {
		return Value.valueOfNumericalValue(i);
	}
	
	public static Value valueOfLabel(String label) {
		return Value.valueOfLabel(label);
	}
	
	public static Value getMinValue() {
		return Value.values()[0];
	}
	
//	public static int getMinNumericalValue() {
//		return getMinValue().getNumericalValue();
//	}
	
	public static Value getMaxValue() {
		return Value.values()[Value.values().length-1] ;
	}
	
//	public static int getMaxNumericalValue() {
//		return getMaxValue().getNumericalValue() ;
//	}

}
