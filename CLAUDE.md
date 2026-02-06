# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A Sudoku solver engine and desktop game implementation in Java. The project supports multiple Sudoku variants including standard 9x9, mini 6x6, big 12x12, and maxi 16x16 puzzles.

## Build & Test Commands

**Build the project:**
```bash
mvn clean compile
```

**Run all tests:**
```bash
mvn test
```

**Run specific test class:**
```bash
mvn test -Dtest=PuzzleTest
mvn test -Dtest=GeneratorTest
```

**Package as JAR:**
```bash
mvn package
```

**Clean build artifacts:**
```bash
mvn clean
```

## Technology Stack

- **Language**: Java 17
- **Build Tool**: Maven
- **Testing**: JUnit 5 (Jupiter)
- **Package**: `com.franciscoguemes.sudoku`

## Current Architecture

### Package Structure

```
com.franciscoguemes.sudoku
├── model/
│   ├── Puzzle.java          # Core board representation and validation
│   ├── PuzzleType.java      # Enum defining puzzle variants (6x6, 9x9, 12x12, 16x16)
│   └── Generator.java       # Puzzle generation using backtracking algorithm
└── App.java                 # Main application entry point
```

### Core Classes

**PuzzleType** (Enum)
- Defines four Sudoku variants: MINI_SUDOKU (6x6), SUDOKU (9x9), BIG_SUDOKU (12x12), MAXI_SUDOKU (16x16)
- Each type specifies: rows, columns, box dimensions (width/height), and valid value range (min/max)
- Flexible design allows easy extension to other puzzle sizes

**Puzzle**
- Represents a Sudoku board with validation logic
- Uses two 2D arrays: `board[][]` for values, `mutable[][]` for tracking given vs. player-filled cells
- Key methods:
  - `makeMove(row, col, value, isMutable)` - Places a number with validation
  - `isValidMove(row, col, value)` - Checks if move violates Sudoku rules
  - `numInRow/numInCol/numInBox` - Constraint checking for rows, columns, and boxes
  - `getBoard()` - Returns defensive copy to prevent external modification
- Copy constructor creates independent puzzle instances
- Cells marked immutable (`isMutable=false`) represent the puzzle's given values

**Generator**
- Generates random valid Sudoku puzzles using backtracking algorithm
- Process:
  1. Creates empty puzzle
  2. Randomly fills first column with valid values
  3. Uses `backtrackSudokuSolver()` to fill remaining cells
  4. Removes ~78% of cells (keeps ~22% as given values)
  5. Given values are marked immutable
- Currently has performance bottleneck for 16x16 puzzles (noted in code comment at line 25)

### Algorithm Details

**Backtracking Solver**
- Recursive depth-first search algorithm
- For each empty cell, tries values 1 through N (where N is board size)
- Validates each value against row, column, and box constraints
- Backtracks when no valid value exists
- Continues until board is full or all possibilities exhausted

**Box Calculation Logic**
- Box position computed as: `boxRow = row / boxHeight`, `boxCol = col / boxWidth`
- Box starting cell: `startRow = boxRow * boxHeight`, `startCol = boxCol * boxWidth`
- Works for any rectangular box dimensions (e.g., 3x2 for 6x6, 3x3 for 9x9, 4x3 for 12x12)

## Testing

Comprehensive test coverage with 36 tests:
- **PuzzleTest** (24 tests): Tests board operations, validation, mutability, copy constructor, defensive copying
- **GeneratorTest** (12 tests): Validates puzzle generation, constraint checking, randomness, difficulty levels

All tests use JUnit 5 with descriptive `@DisplayName` annotations.

## Outstanding TODOs

### Immediate Next Steps
1. **User Interface**: No GUI implemented yet - need to choose between Swing or JavaFX
2. **Difficulty Levels**: Generator creates puzzles at fixed difficulty (~22% filled). Need to implement Easy/Medium/Hard/Expert levels
3. **Hint System**: No hint functionality implemented
4. **Undo/Redo**: No action history or undo mechanism
5. **Save/Load**: No persistence layer

### Known Issues
- **Performance**: Generator bottleneck for 16x16 puzzles (noted in Generator.java:25) - needs optimization
- **Difficulty Algorithm**: Current removal percentage (22%) is hardcoded and not scientifically calibrated

### Future Features
- Timer functionality
- Score tracking
- Multiple puzzle packs
- Puzzle uniqueness validation (ensure single solution)
- Puzzle solver UI to show solutions

## Development Notes

- Puzzle uses defensive copying in `getBoard()` to protect internal state
- Immutable cells prevent players from changing given values
- Flexible `PuzzleType` design makes adding new puzzle sizes straightforward
- Backtracking algorithm is generic and works for all puzzle sizes
