#!/bin/bash

TARGET_DIR=~/git/francisco/github/sudoku/target

# Build classpath from all JARs in the classpath directory
CP=""
for jar in "$TARGET_DIR/lib/classpath/"*.jar; do
    CP="$CP:$jar"
done

exec java \
  --module-path "$TARGET_DIR/sudoku-1.0-SNAPSHOT.jar:$TARGET_DIR/lib/modular" \
  --class-path "$CP" \
  --add-reads com.franciscoguemes.sudoku=ALL-UNNAMED \
  --enable-native-access=javafx.graphics \
  --module com.franciscoguemes.sudoku/com.franciscoguemes.sudoku.Launcher \
  "$@"

