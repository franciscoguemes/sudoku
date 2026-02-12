TODOs
=====================================

<!-- TOC -->

* [TODOs](#todos)
    * [Topics](#topics)
    * [Specific tasks](#specific-tasks)
        * [Project wise](#project-wise)
        * [Game](#game)
        * [Editor](#editor)
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
4. **Difficulty Levels**: Generator creates puzzles at fixed difficulty (~22% filled). Need to implement
   Easy/Medium/Hard/Expert levels
5. **Graphical User Interface**: JavaFX interface
6. **Undo/Redo**: Action history and undo mechanism
7. **Candidates**: Let the user mark the candidates in the cells.
8. **Hint System**: No hint functionality implemented
9. **Testing**: Test the UI and test coverage.

## Specific tasks

### Project wise

- [x] Add difficulty levels on generation of the Sudoku puzzles
- [ ] Refactor model package into
  - [ ] Sudoku: Based class that contains a sudoku ?
  - [ ] Puzzle: Contains the sudoku plus the solution ?
  - [ ] Game: Contains the state of the game (Logic + UI => The puzzle, the score, timer, error count, notes, etc ...) ?
- [ ] Add logging to the app
- [ ] Launch apps using JBang
- [ ] Create documentation about the UI

### Game

- [x] Difficulty levels
- [x] Technique based grades of sudoku generation
- [x] Numbers exhaustion: When a number is completed in all areas of the board, the button with the number is disabled
  in the numbers panel.
- [x] Update notes after input (if value is valid)
- [x] Timer functionality
- [ ] Hints button
- [ ] Score tracking. The user will get points for each empty cell in the board that he completes successfully. The
  score may vary depending on the difficulty of the puzzle. For the category Easy will be 10 points per cell and for the
  category Extreme will be 35 points per cell. If the user makes more than 3 mistakes (Game Over) he loses all the
  points until that moment.
- [ ] 3.3. Save game state. Saves the current state of the game. That means to save the current state of the board (
  immutable cells, completed cells, notes), the timer, the mistakes counter, and any other thing that may be important
  for restoring the state of the game.
- [ ] 3.4. Load game state

### Editor

- [ ] Add difficulty levels for generating the Sudoku puzzles
- [ ] Review the EditorApp GUI and delete what is no longer valid.
- [ ] Fix bug on solving the sudoku. The app gets hanged.
- [ ] Add a more efficient way of solving the sudoku
- [ ] Create launcher script ??
- [ ] Finish textui version
- [x] Load sudoku puzzle
    - [x] CSV
    - [x] sudoku
- [x] 3.1. Save sudoku puzzle
    - [x] CSV
    - [x] sudoku

### Advanced Features (Future)

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
- Create a format (JSON or whatever) that is able to represent not only the internal state of the puzzle, but also the
  state of the application (scoreboard, timer, statistics, etc...)
