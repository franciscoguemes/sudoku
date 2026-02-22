#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
TARGET_DIR="${PROJECT_DIR}/target"

exec java \
  --module-path "$TARGET_DIR/sudoku-1.0-SNAPSHOT.jar:$TARGET_DIR/lib" \
  --enable-native-access=javafx.graphics \
  --module com.franciscoguemes.sudoku/com.franciscoguemes.sudoku.Launcher \
  "$@"

