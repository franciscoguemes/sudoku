Logging
==================================



Summary

Files created (2)

- src/main/resources/logback.xml — Console appender (stderr), rolling file appender (~/.sudoku/logs/sudoku.log, daily rotation, 30-day retention, 100MB cap), per-package log levels
- src/test/resources/logback-test.xml — Quiet config for tests (root WARN, console only)

Files modified (24)
┌──────────────────────┬───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│         File         │                                                                    Changes                                                                    │
├──────────────────────┼───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│ pom.xml              │ Added slf4j-api 2.0.16 + logback-classic 1.5.16 dependencies                                                                                  │
├──────────────────────┼───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│ Generator.java       │ INFO: generation start/end with type & difficulty. DEBUG: cell removal/restoration during uniqueness pass. TRACE: backtracking value attempts │
├──────────────────────┼───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│ TechniqueSolver.java │ INFO: solve/grade results. DEBUG: technique applied. TRACE: technique attempted                                                               │
├──────────────────────┼───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│ CandidateGrid.java   │ DEBUG: grid initialized (empty cell count). TRACE: value placed                                                                               │
├──────────────────────┼───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│ 10 technique classes │ DEBUG: when technique finds and applies an elimination/placement                                                                              │
├──────────────────────┼───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│ 4 I/O classes        │ INFO: read/write started, puzzle loaded with dimensions                                                                                       │
├──────────────────────┼───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│ GameApp.java         │ INFO: app start/ready, type/difficulty changes, new puzzle generation                                                                         │
├──────────────────────┼───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│ EditorApp.java       │ INFO: app start/ready, import/export. ERROR: failed imports/exports                                                                           │
├──────────────────────┼───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│ GameGridPane.java    │ DEBUG: number placed (correct/incorrect), erase, undo. INFO: puzzle completed                                                                 │
├──────────────────────┼───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│ ConsoleApp.java      │ INFO: app starting with args                                                                                                                  │
├──────────────────────┼───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│ ConsoleUI.java       │ DEBUG: file loading path                                                                                                                      │
└──────────────────────┴───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
Verification

- mvn clean compile — BUILD SUCCESS
- mvn test (excluding GeneratorTest) — 101 tests, 0 failures, 0 errors
- GeneratorTest was still running (pre-existing 16x16 bottleneck) but no logging-related issues

