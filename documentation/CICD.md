CI/CD
====================================


<!-- TOC -->
* [CI/CD](#cicd)
  * [Goal](#goal)
  * [Building process](#building-process)
  * [Caveats](#caveats)
    * [JLink](#jlink)
  * [GitHub Actions workdlows](#github-actions-workdlows)
<!-- TOC -->

## Goal

The final goals of the entire CI/CD process are the following:
 - Run the test automation on every PR (Pull Request)
 - Generate a tag when a new version is released
 - Generate different artifacts for different platforms
 - Publish automatically each artifact on its corresponding Marketplace

## Building process

In order to have a higher control on the building process we opted out for building a
"thin jar" (`maven-jar-plugin`), by doing this it is easier to find the dependencies 
with `jdep` and then create a custom JRE with `jlink`. 
Finally, it is possible to use the custom JRE as input for `jpackage` and generate the 
different artifacts for each platform.

```
your-app-thin.jar          (only your classes, no dependencies shaded in)
     +
dependencies on classpath  (JavaFX JARs, any other libs)
     +
jlink → custom JRE image
     |
     ├── jpackage → .msi (Windows runner)
     ├── jpackage → .dmg (macOS runner)  
     ├── jpackage → .deb (Linux runner)
     ├── snapcraft → .snap
     └── appimagetool → .AppImage
```

---

## CI GitHub Actions workdlows

The workflow is described in the file `ci.yml`. If you inspect the file you will see some sections commented, this is
done on purpose. These sections can be activated on-demand (uncommented) whenever it is necessary.

<<< Insert here a Mermaid flow diagram about the pipeline. Include the commented sections as grayed and include 
a legend >>>

---

## CD GitHub Actions workdlows

At the moment there are three files:
 - [build-installers.yml](../.github/workflows/build-installers.yml)
 - [build-installers-improved.yml](../.github/workflows/build-installers-improved.yml)
 - [build-snap-package.yml](../.github/workflows/build-snap-package.yml)

The three files are outdated since the project does not generate a fat jar anymore.

The new approach is to have one workflow for each type of artifact. E.g. `build-snap-package.yml` and add progressively
new workflows as far as we generate new artifacts for different platforms (`.AppImage`, `*.msi`,`*.dmg`, etc ...).
These independent workflows must be compatible with the [building process](#building-process) described previously, and
they will trigger only when a new version of the application is released (a new tag is generated).




---

## Caveats

### JLink
`jlink` requires that all modules on the module path have a `module-info.class` — they must be proper named modules, 
not automatic modules (`*.jar` files that are dropped in the `classpath`). JavaFX satisfies this since it has been fully 
modularized for years. However, if you introduce any third-party dependency (e.g. `junit`, `slf4j`, `logback`, etc...) 
that is still a plain classpath JAR (no module-info), it becomes an automatic module and `jlink` will refuse to include 
it.

At the moment the way this is solved is by using the `maven-dependency-plugin` and copying the modular dependencies
under `/target/lib/modular` and the non-modular dependencies (pain jar files in the classpath) under `/target/lib/classpath`.
Later on when executing the application from the command line, including all the `*.jar` files in the directory 
`target/lib/classpath` in the `--class-path` option. 
Finally including the option `--add-reads` with the keyword `ALL-UNNAMED`. `ALL-UNNAMED` is a special JVM token meaning 
"the unnamed module" (i.e. the classpath). This is the quickest fix and perfectly legitimate for dependencies that 
aren't yet proper modules. You can see the script `/utils/run_app.sh` for the entire functionality:
```bash
exec java \
  --module-path "$TARGET_DIR/sudoku-1.0-SNAPSHOT.jar:$TARGET_DIR/lib/modular" \
  --class-path "$CP" \
  --add-reads com.franciscoguemes.sudoku=ALL-UNNAMED \
  --enable-native-access=javafx.graphics \
  --module com.franciscoguemes.sudoku/com.franciscoguemes.sudoku.Launcher \
```

As you can see this solution is not scalable, this is just a workaround for allowing the pipelines to work.
The practical mitigation this problem is the `moditect` maven plugin, which can generate module-info descriptors for 
non-modular JARs.

