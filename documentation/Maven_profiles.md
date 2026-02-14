Maven Profiles
================================================

## Summary

Single `src/main/resources/` directory:

```text
src/main/resources/                                                                                                                                                                                                      
├── config/                                                                                                                                                                                                              
│   ├── dev/                                                                
│   │   ├── logback.xml          (DEBUG, logs to ./logs/)
│   │   └── application.properties (app.environment=dev)
│   └── prod/
│       ├── logback.xml          (WARN/INFO, logs to ~/.sudoku/logs/)
│       └── application.properties (app.environment=prod)
├── *.csv, *.sudoku              (common resources, unchanged)
```

`pom.xml` — profiles now only set an `<env>` property. The `<build><resources>` section uses `${env}` to pull from the
right `config/` subdirectory, while excluding the entire `config/` tree from the classpath output. The `prod`
profile is active by default.

## Usage

```shell
mvn javafx:run            # prod (default) — WARN/INFO, logs to ~/.sudoku/logs/
mvn javafx:run -Pdev      # dev — DEBUG, logs to ./logs/
mvn package               # package with prod logging
mvn package -Pdev         # package with dev logging
```

| Scenario            | Command                                                               | Config file used        | Log dir          | Level |
|---------------------|-----------------------------------------------------------------------|-------------------------|------------------|-------|
| Development         | mvn javafx:run -Pdev                                                  | config/dev/logback.xml  | ./logs/          | DEBUG |
| Production          | mvn javafx:run                                                        | config/prod/logback.xml | ~/.sudoku/logs/  | INFO  |
| Production (JAR)    | java -jar sudoku.jar                                                  | config/prod/logback.xml | ~/.sudoku/logs/  | INFO  |
| Production (custom) | java -DLOG_DIR=/var/log/sudoku -DLOG_LEVEL_ROOT=DEBUG -jar sudoku.jar | config/prod/logback.xml | /var/log/sudoku/ | DEBUG |


