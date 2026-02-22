#!/usr/bin/env bash

JAVAFX_VERSION=25.0.2
JAVAFX_JMODS=~/javafx-jmods/${JAVAFX_VERSION}

# Create a local directory for the JMODs
mkdir -p "$JAVAFX_JMODS"

# Download from Gluon (the official JavaFX distribution)
wget -O /tmp/javafx-jmods.zip \
  https://download2.gluonhq.com/openjfx/25.0.2/openjfx-25.0.2_linux-x64_bin-jmods.zip

unzip /tmp/javafx-jmods.zip -d /tmp/javafx-jmods-extracted
cp /tmp/javafx-jmods-extracted/javafx-jmods-${JAVAFX_VERSION}/*.jmod "$JAVAFX_JMODS"