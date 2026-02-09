TODOs
=====================================

<!-- TOC -->
* [TODOs](#todos)
  * [Topics](#topics)
  * [Specific tasks](#specific-tasks)
    * [Advanced Features (Future)](#advanced-features-future)
  * [0. **Project Setup**:](#0-project-setup)
  * [1. **Text based User Interface**: Console](#1-text-based-user-interface-console)
  * [2. **Logging System**:](#2-logging-system-)
  * [3. **Save/Load**:](#3-saveload-)
<!-- TOC -->


## Topics

0. **Project Setup**
1. **Text based User Interface**: Console
2. **Logging System**: Create a real logging system where it is possible to troubleshoot the application.
3. **Save/Load**: Persistence layer. Load and save from files.
4. **Difficulty Levels**: Generator creates puzzles at fixed difficulty (~22% filled). Need to implement Easy/Medium/Hard/Expert levels
5. **Graphical User Interface**: JavaFX interface
6. **Undo/Redo**: Action history and undo mechanism
7. **Candidates**: Let the user mark the candidates in the cells.
8. **Hint System**: No hint functionality implemented


## Specific tasks

- [x] Load sudoku puzzle
    - [x] CSV
    - [x] sudoku
- [ ] 3.1. Save sudoku puzzle
    - [ ] CSV
    - [ ] sudoku
- [ ] 3.3. Save application state
- [ ] 3.4. Load application state

### Advanced Features (Future)
- Timer functionality
- Score tracking
- Multiple puzzle packs
- Puzzle solver
- Puzzle creator


## 0. **Project Setup**:
 - Define multiple profiles (Production, Development, Test, etc...)
 - Define settings regarding the profiles
 - Configure test coverage

## 1. **Text based User Interface**: Console
 - Find a proper curses library (if possible)
 - Represent the current state of the puzzle in the console
 - Allow the user to input values on specific cells
 - Highlight errors (if needed)


## 2. **Logging System**: 
Create a real logging system where it is possible to troubleshoot the application.
- Use SLF4J to log
- Define logging format and appenders (File, console, etc, ...)
- Log output to the console and to a file

## 3. **Save/Load**: 
Persistence layer. Load and save from files.
 - Allow multiple formats for loading a puzzle:
   - CSV
   - JSON
   - `*.sudoku`
   - ...
  - Create a Mapper that maps the input file to the internal state of the puzzle.
  - Allow multiple formats for export a puzzle:
    - CSV
    - JSON
    - `*.sudoku`
    - ...
 - Create a format (JSON or whatever) that is able to represent not only the internal state of the puzzle, but also the state of the application (scoreboard, timer, statistics, etc...)
