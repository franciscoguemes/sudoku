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
- **Module patching**: moditect (patches non-modular JARs with `module-info.class` so all dependencies live on `--module-path`)

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

First, compile and package the thin JAR and copy all runtime dependencies into `target/lib/`:
```bash
mvn package -DskipTests
```

This produces:
- `target/sudoku-1.0-SNAPSHOT.jar` — the named-module application JAR
- `target/lib/` — all runtime dependencies (JavaFX, SLF4J, Logback, …)

The application is a proper named JPMS module (`com.franciscoguemes.sudoku`).
Place **both** the dependency directory and the application JAR on the module path
so that the module descriptor is honoured, then use `-m` to select the entry point:

**Sudoku Game (default):**
```bash
java --module-path target/lib:target/sudoku-1.0-SNAPSHOT.jar \
     --enable-native-access=javafx.graphics \
     -m com.franciscoguemes.sudoku/com.franciscoguemes.sudoku.Launcher
```

**Sudoku Editor:**
```bash
java --module-path target/lib:target/sudoku-1.0-SNAPSHOT.jar \
     --enable-native-access=javafx.graphics \
     -m com.franciscoguemes.sudoku/com.franciscoguemes.sudoku.Launcher --editor
```

**Console App:**
```bash
java --module-path target/lib:target/sudoku-1.0-SNAPSHOT.jar \
     -m com.franciscoguemes.sudoku/com.franciscoguemes.sudoku.Launcher --console
```

> **Windows:** replace `:` with `;` in `--module-path` (e.g. `target\lib;target\sudoku-1.0-SNAPSHOT.jar`).

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

## Snap Packaging

Two helper scripts under `utils/` cover the full snap lifecycle:

| Script                  | Purpose                                                                                                                                        |
|-------------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `utils/build_snap.sh`   | Runs `mvn package`, creates the custom JRE with `jlink`, stages all artifacts, and invokes `snapcraft` to produce `target/snap-build/*.snap`   |
| `utils/install_snap.sh` | Verifies the snap artifact exists, optionally removes a previous installation, then installs with `snap install --dangerous --classic`          |

```bash
# Build the snap package
./utils/build_snap.sh

# Install (or reinstall) the snap package
./utils/install_snap.sh
```

Both scripts support `--debug`, `--dry-run`, and `-h/--help`.

## Adding New Dependencies

All runtime dependencies land in a single flat `target/lib/` directory.
Every JAR on that path must be a named JPMS module.

- **Already-modular JARs** (most modern libraries): just add the `<dependency>` to `pom.xml` and a `requires` line to `module-info.java`. No further action needed.
- **Non-modular JARs** (legacy libraries without `module-info.class`): add a `<module>` entry to the `moditect-maven-plugin` configuration in `pom.xml`. Run `mvn moditect:generate-module-info` first to get a generated descriptor as a starting point, then paste the relevant block into the plugin config.

# Sources

- [Sudoku Generator](https://www.sudokuweb.org/#google_vignette)
- [Sudoku generator in C++](https://medium.com/@donjadene/create-a-simple-sudoku-puzzle-generator-and-solver-console-application-using-c-2c025b24a26a)
- [Sudoku generator in Javascript](https://github.com/DhanushNehru/sudoku-puzzle/blob/main/src/generator.js)
