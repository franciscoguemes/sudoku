#!/usr/bin/env bash

PROJECT_DIR=~/git/francisco/github/sudoku
JRE_DIR="${PROJECT_DIR}/target/custom-jre"
JAVA="${JRE_DIR}/bin/java"

$JAVA --module-path target/lib:target/sudoku-1.0-SNAPSHOT.jar \
     --enable-native-access=javafx.graphics \
     -m com.franciscoguemes.sudoku/com.franciscoguemes.sudoku.Launcher