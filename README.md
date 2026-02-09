Sudoku
===================================================

A Sudoku solver engine and desktop game

## Running the application

### With Maven

**Interactive mode** (generates a random puzzle):
```bash
mvn compile exec:java -Dexec.mainClass="com.franciscoguemes.sudoku.App"
```

**Loading a puzzle from a file** (`.csv` or `.sudoku`):
```bash
mvn compile exec:java -Dexec.mainClass="com.franciscoguemes.sudoku.App" -Dexec.args="src/main/resources/Hardest_in_the_world.csv"
```

### Without Maven

First, compile and package the project:
```bash
mvn package -DskipTests
```

Then run the JAR directly with `java`:

**Interactive mode:**
```bash
java -cp target/sudoku-1.0-SNAPSHOT.jar com.franciscoguemes.sudoku.App
```

**Loading a puzzle from a file:**
```bash
java -cp target/sudoku-1.0-SNAPSHOT.jar com.franciscoguemes.sudoku.App src/main/resources/Hardest_in_the_world.csv
```
