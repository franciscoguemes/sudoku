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
10. **CI/CD**: All related with the CI/CD
11. **Documentation**



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

- [x] Define multiple profiles (Production, Development, Test, etc...)
- [x] Define settings regarding the profiles
- [x] Configure test coverage

### Maven verify warning

I executed `mvn clean verify` and I got the following warnings:
```
[WARNING] Discovered module-info.class. Shading will break its strong encapsulation.
[WARNING] Discovered module-info.class. Shading will break its strong encapsulation.
[WARNING] Discovered module-info.class. Shading will break its strong encapsulation.
[WARNING] Discovered module-info.class. Shading will break its strong encapsulation.
[WARNING] Discovered module-info.class. Shading will break its strong encapsulation.
[WARNING] javafx-controls-25.0.2-linux.jar, javafx-graphics-25.0.2-linux.jar define 4 overlapping resources: 
[WARNING]   - META-INF/substrate/config/reflectionconfig-aarch64-android.json
[WARNING]   - META-INF/substrate/config/reflectionconfig-arm64-ios.json
[WARNING]   - META-INF/substrate/config/reflectionconfig-x86_64-ios.json
[WARNING]   - META-INF/substrate/config/resourcebundles
[WARNING] javafx-base-25.0.2-linux.jar, javafx-controls-25.0.2-linux.jar, javafx-graphics-25.0.2-linux.jar define 1 overlapping resource: 
[WARNING]   - META-INF/substrate/config/reflectionconfig.json
[WARNING] javafx-base-25.0.2-linux.jar, javafx-base-25.0.2.jar, javafx-controls-25.0.2-linux.jar, javafx-controls-25.0.2.jar, javafx-graphics-25.0.2-linux.jar, javafx-graphics-25.0.2.jar, logback-classic-1.5.16.jar, logback-core-1.5.16.jar, slf4j-api-2.0.16.jar, sudoku-1.0-SNAPSHOT.jar define 1 overlapping resource: 
[WARNING]   - META-INF/MANIFEST.MF
[WARNING] maven-shade-plugin has detected that some files are
[WARNING] present in two or more JARs. When this happens, only one
[WARNING] single version of the file is copied to the uber jar.
[WARNING] Usually this is not harmful and you can skip these warnings,
[WARNING] otherwise try to manually exclude artifacts based on
[WARNING] mvn dependency:tree -Ddetail=true and the above output.
[WARNING] See https://maven.apache.org/plugins/maven-shade-plugin/
[INFO] Replacing original artifact with shaded artifact.
```
I understand this is related to the packaging phase. Is there any way to fix these warnings taking into account that:

## 1. **Text based User Interface**: Console

- Find a proper curses library (if possible)
- Represent the current state of the puzzle in the console
- Allow the user to input values on specific cells
- Highlight errors (if needed)

## 2. **Logging System**:

Create a real logging system where it is possible to troubleshoot the application.

- [x] Use SLF4J to log
- [x] Define logging format and appenders (File, console, etc, ...)
- [x] Log output to the console and to a file

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


## 4. **Difficulty Levels**

## 5. **Graphical User Interface**

## 6. **Undo/Redo**: Action history and undo mechanism

## 7. **Candidates**: Let the user mark the candidates in the cells.

## 8. **Hint System**: No hint functionality implemented

## 9. **Testing**: Test the UI and test coverage.


## 10. **CI/CD**

### One Workflow per artifact
Generate one independent workflow per installer to build.

## 11. **Documentation**

### Gnerating notes on building a snap artifact in the computer

See this [article](https://medium.com/geekculture/how-i-made-my-first-snap-470d22eb621a)


### Generate notes on building snap artifact with GitHub actions
See the [DeepSeek converstion](https://chat.deepseek.com/a/chat/s/a653a5dd-4be6-4efe-b221-89da87143bce)

### Generating notes on publising artifact on Snapcraft

See the file: ([Notes_on_building_snap_package.md](../../../../Desktop/Notes_on_building_snap_package.md))
See the [DeepSeek converstion](https://chat.deepseek.com/a/chat/s/a653a5dd-4be6-4efe-b221-89da87143bce)

### Generate notes on cross platform packaging strategy
This is more generic on how to generate multiple installers for different platforms
See the [DeepSeek converstion](https://chat.deepseek.com/a/chat/s/3066037c-b599-4e1d-89b6-f133f97adb92)