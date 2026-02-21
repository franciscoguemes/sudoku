#!/bin/bash
# Use the JRE bundled inside the snap
JAVA="$SNAP/jre/bin/java"

exec "$JAVA" \
  --module-path "$SNAP/javafx/lib" \
  --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.web \
  -jar "$SNAP/sudoku.jar" "$@"