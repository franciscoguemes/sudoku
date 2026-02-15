Sudoku
===================================================

A Sudoku solver engine and desktop game implementation in Java. The project supports multiple Sudoku variants:

| Variant       | Grid Size | Box Size |
|---------------|-----------|----------|
| Mini Sudoku   | 6x6       | 3x2      |
| Sudoku        | 9x9       | 3x3      |
| Big Sudoku    | 12x12     | 4x3      |
| Maxi Sudoku   | 16x16     | 4x4      |

## Technology Stack

- **Language**: Java 25
- **GUI Framework**: JavaFX 25.0.2
- **Build Tool**: Maven
- **Testing**: JUnit 5

## Applications

The project contains two graphical applications and one console application:

### Sudoku Game (`GameApp`)

An interactive Sudoku game with the following features:

- Puzzle type and difficulty selection
- Keyboard and mouse input
- Notes mode for pencil marks
- Undo support
- Erase functionality
- Mistakes tracking (game over after 3 mistakes)
- Visual highlighting of selected cell, row, column, box, and matching numbers

### Sudoku Editor (`EditorApp`)

A puzzle editor for generating, importing, exporting, and solving Sudoku puzzles. Features:

- Generate random puzzles for any supported variant
- Import puzzles from `.csv` and `.sudoku` files
- Export puzzles to `.csv` and `.sudoku` files
- Solve puzzles using the built-in backtracking solver

### Console App (`ConsoleApp`)

A command-line interface for generating and solving puzzles.

## Running the Applications

### Prerequisites

- Java 25 or later
- Maven 3.8+

### Sudoku Game (default)

```bash
mvn javafx:run
```

### Sudoku Editor

```bash
mvn javafx:run -Djavafx.mainClass=com.franciscoguemes.sudoku.gui.EditorApp
```

### Console App

**Interactive mode** (generates a random puzzle):
```bash
mvn compile exec:java -Dexec.mainClass="com.franciscoguemes.sudoku.ConsoleApp"
```

**Loading a puzzle from a file** (`.csv` or `.sudoku`):
```bash
mvn compile exec:java -Dexec.mainClass="com.franciscoguemes.sudoku.ConsoleApp" -Dexec.args="src/main/resources/Hardest_in_the_world.csv"
```

### Running without Maven

First, compile and package the project (this also copies dependencies to `target/lib/`):
```bash
mvn package -DskipTests
```

Then run the JAR directly with `java`:

**Console App:**
```bash
java -cp target/sudoku-1.0-SNAPSHOT.jar com.franciscoguemes.sudoku.ConsoleApp
```

**Sudoku Game (JavaFX):**
```bash
java --enable-native-access=javafx.graphics --module-path target/lib --add-modules javafx.controls -cp target/sudoku-1.0-SNAPSHOT.jar com.franciscoguemes.sudoku.gui.GameApp
```

**Sudoku Editor (JavaFX):**
```bash
java --enable-native-access=javafx.graphics --module-path target/lib --add-modules javafx.controls -cp target/sudoku-1.0-SNAPSHOT.jar com.franciscoguemes.sudoku.gui.EditorApp
```

> **Note:** The `--enable-native-access=javafx.graphics` flag is required on Java 21+ to avoid a native access warning from JavaFX.

## Game Controls

### Keyboard

| Key              | Action                                       |
|------------------|----------------------------------------------|
| Arrow keys       | Navigate the board                           |
| 1-9              | Place a number (or toggle a note in notes mode) |
| A-G              | Place values 10-16 (for 12x12 and 16x16 puzzles) |
| Delete/Backspace | Erase the selected cell                      |

### Toolbar

| Button | Action                                            |
|--------|---------------------------------------------------|
| Undo   | Revert the last action                            |
| Erase  | Clear the selected cell (value or all notes)      |
| Notes  | Toggle notes mode on/off                          |

## Building

```bash
mvn clean compile
```

## Testing

```bash
mvn test
```

# Sources

- [Sudoku Generator](https://www.sudokuweb.org/#google_vignette)
- [Sudoku generator in C++](https://medium.com/@donjadene/create-a-simple-sudoku-puzzle-generator-and-solver-console-application-using-c-2c025b24a26a)
- [Sudoku generator in Javascript](https://github.com/DhanushNehru/sudoku-puzzle/blob/main/src/generator.js)
