#!/usr/bin/env bash

cd ~/git/francisco/github/sudoku
mvn clean package          # produces thin JAR + lib/ directory

# run your jlink script    # produces target/custom-jre
./utils/create_custom_jre.sh


# Copy snap definition to target, build from there
mkdir -p target/snap-build/snap/local
cp -R snap/ target/snap-build/
#cp snap/snapcraft.yaml target/snap-build/snap/
#cp snap/local/launcher target/snap-build/snap/local/

cd target/snap-build
pwd
echo "Here"
snapcraft --destructive-mode