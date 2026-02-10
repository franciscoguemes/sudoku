Sudoku
===================================================

A Sudoku solver engine and desktop game

## Running the application

### With Maven

**Interactive mode** (generates a random puzzle):
```bash
mvn compile exec:java -Dexec.mainClass="com.franciscoguemes.sudoku.ConsoleApp"
```

**Loading a puzzle from a file** (`.csv` or `.sudoku`):
```bash
mvn compile exec:java -Dexec.mainClass="com.franciscoguemes.sudoku.ConsoleApp" -Dexec.args="src/main/resources/Hardest_in_the_world.csv"
```

### Without Maven

First, compile and package the project:
```bash
mvn package -DskipTests
```

Then run the JAR directly with `java`:

**Interactive mode:**
```bash
java -cp target/sudoku-1.0-SNAPSHOT.jar com.franciscoguemes.sudoku.ConsoleApp
```

**Loading a puzzle from a file:**
```bash
java -cp target/sudoku-1.0-SNAPSHOT.jar com.franciscoguemes.sudoku.ConsoleApp src/main/resources/Hardest_in_the_world.csv
```



# Sources

- [Sudoku Generator](https://www.sudokuweb.org/#google_vignette)
- [Sudoku generator in C++](https://medium.com/@donjadene/create-a-simple-sudoku-puzzle-generator-and-solver-console-application-using-c-2c025b24a26a)
- [Sudoku generator in Javascript](https://github.com/DhanushNehru/sudoku-puzzle/blob/main/src/generator.js)
- []()