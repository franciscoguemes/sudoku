#!/usr/bin/env bash

PROJECT_DIR=~/git/francisco/github/sudoku
JAR_FILE="${PROJECT_DIR}"/target/sudoku-1.0-SNAPSHOT.jar

# Normal *.jar file
#jdeps --multi-release 25 \
#      --ignore-missing-deps \
#      --print-module-deps \
#      ${JAR_FILE}

# Fat *.jar file and classpath dependencies
#jdeps --multi-release 25 \
#      --ignore-missing-deps \
#      --print-module-deps \
#      --class-path 'lib/*' \
#      ${JAR_FILE}

# Useful for debugging
jdeps --multi-release 25 \
      -recursive \
      --ignore-missing-deps \
      -summary \
      ${JAR_FILE}

# If you have JavaFX installed in your system
#JAVAFX_HOME=/path/to/javafx-sdk/lib
#jdeps --multi-release 25 \
#      --ignore-missing-deps \
#      --print-module-deps \
#      --module-path ${JAVAFX_HOME} \
#      ${JAR_FILE}

# If you use JavaFx as Maven dependency
JAVAFX_MODS=~/.m2/repository/org/openjfx
JAVAFX_VERSION=25.0.2 # See this value inside the JAVAFX_MODS directory
jdeps --multi-release 25 \
      --ignore-missing-deps \
      --print-module-deps \
      --module-path "$JAVAFX_MODS/javafx-base/${JAVAFX_VERSION}/javafx-base-${JAVAFX_VERSION}-linux.jar:\
$JAVAFX_MODS/javafx-controls/${JAVAFX_VERSION}/javafx-controls-${JAVAFX_VERSION}-linux.jar:\
$JAVAFX_MODS/javafx-fxml/${JAVAFX_VERSION}/javafx-fxml-${JAVAFX_VERSION}-linux.jar:\
$JAVAFX_MODS/javafx-graphics/${JAVAFX_VERSION}/javafx-graphics-${JAVAFX_VERSION}-linux.jar" \
      ${JAR_FILE}