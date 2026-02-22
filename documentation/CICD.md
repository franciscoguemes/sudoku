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
not automatic modules (`*.jar` files placed on the classpath). JavaFX satisfies this since it has been fully
modularized for years.

All other runtime dependencies (SLF4J 2.x, Logback 1.5.x) are also named modules, so every JAR lands in a single
flat `target/lib/` directory and the application launches with a pure `--module-path`:

```bash
exec java \
  --module-path "$TARGET_DIR/sudoku-1.0-SNAPSHOT.jar:$TARGET_DIR/lib" \
  --enable-native-access=javafx.graphics \
  --module com.franciscoguemes.sudoku/com.franciscoguemes.sudoku.Launcher
```

Should a future dependency ship without a `module-info.class`, the `moditect-maven-plugin` (already wired into
`pom.xml`) can patch it with a generated descriptor. See the **Adding New Dependencies** section in `README.md`.

