#!/usr/bin/env bash

set -euo pipefail

JAVAFX_VERSION=25.0.2
JAVAFX_JMODS=~/javafx-jmods/${JAVAFX_VERSION}
PROJECT_DIR=~/git/francisco/github/sudoku
OUTPUT_DIR="${PROJECT_DIR}/target/custom-jre"
JDK_HOME=$(java -XshowSettings:properties -version 2>&1 | grep java.home | awk '{print $3}')

#Debugging...
echo "JAVAFX_JMODS: $JAVAFX_JMODS"
echo "JDK_HOME: $JDK_HOME"
echo "OUTPUT_DIR: $OUTPUT_DIR"

# Clean previous output
rm -rf "${OUTPUT_DIR}"

jlink \
  --module-path "${JDK_HOME}/jmods:${JAVAFX_JMODS}" \
  --add-modules java.base,java.desktop,java.logging,java.naming,jdk.jfr,\
javafx.controls,javafx.fxml,javafx.graphics,javafx.base,\
jdk.unsupported \
  --strip-debug \
  --no-header-files \
  --no-man-pages \
  --compress zip-6 \
  --output "${OUTPUT_DIR}"

echo "Custom JRE size: $(du -sh ${OUTPUT_DIR} | cut -f1)"

# The `--compress zip-6` flag (Java 21+ syntax, replaces the old `--compress=2`) gives you a good size/speed tradeoff. `jdk.unsupported` is mandatory for JavaFX as it uses `sun.misc.Unsafe` internally.

