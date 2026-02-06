CLAUDE.md
=================================

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A Sudoku solver engine and desktop game implementation in Java.

## Current Status

This is a new project in initial setup phase. No source code has been written yet.

## Project Requirements

### Core Features to Implement
1. **Board Generation**: Generate valid Sudoku puzzles with multiple difficulty levels (Easy, Medium, Hard, Expert) ensuring unique solutions
2. **Game Play**: Number input, real-time move validation, error highlighting, hints
3. **User Interface**: Clean desktop interface with cell selection, number input, undo/redo, save/load game state
4. **Game Logic**: Board state validation, completion checking, conflict detection (row, column, 3x3 box)

### Future Advanced Features
- Timer functionality
- Score tracking
- Multiple puzzle packs
- Puzzle solver
- Puzzle creator

## Development Priorities

When setting up and developing this project, follow this sequence:
1. Set up Java project structure and build system (Maven or Gradle)
2. Implement Sudoku board representation (9x9 grid with 3x3 boxes)
3. Create puzzle validation logic
4. Build puzzle generator
5. Develop user interface
6. Add game state management
7. Implement persistence (save/load)
8. Write comprehensive tests

## Architecture Notes

When implementing the architecture:
- **Board Representation**: Will need efficient data structure for 9x9 grid with row, column, and 3x3 box access
- **Puzzle Generation**: Implement backtracking algorithm to generate valid puzzles, then remove numbers based on difficulty
- **Validation**: Separate validation logic for row, column, and box constraints
- **UI**: Desktop application (Swing/JavaFX to be determined during implementation)
