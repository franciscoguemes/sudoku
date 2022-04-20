
## Naming convention
The naming convention for my projects is the following:
  
```
groupId: com.francisconguemes.[name_of_aggregator]  
artifactId: name_of_the_project
```

In this case since this is a maven aggregator project the groupId is  _com.franciscoguemes.sudoku_


## Generating the project structure

Normally I would generate the project structure with [spring initializr](https://start.spring.io/) selecting the following dependencies:
 - Lombok
 - Picocli

Since spring initializr does not allow multimodule projects, all you can do is to select the dependencies you want and then add them manually in the pom.xml of the BOM file of my multimodule template

// TODO: Add here the link to my multimodule maven project skeleton

// TODO: [Create Maven archetype from multimodule project](https://maven.apache.org/archetype/maven-archetype-plugin/examples/create-multi-module-project.html)


## Adding .gitignore for Java projects

The gitignore file for this project is taken from my own [gitignore project](https://github.com/franciscoguemes/gitignore)

// TODO: Update the link to the exact directory/file

## Problems solved by order of appearance and branches:

### 0000_Parse_arguments

1. Create a [Spring Boot console only application](https://www.baeldung.com/spring-boot-console-app), [without a server](https://www.baeldung.com/spring-boot-no-web-server)

1. Execute the application and pass arguments in the command line  
```
./gradlew bootRun --args='arg1 arg2'
```  

1. [Why doesn't PicoCli recognize my option from the command line?](https://stackoverflow.com/questions/62963202/why-doesnt-picocli-recognize-my-option-from-the-command-line). If you invoke the command with java, you don't need to specify the first command name, just the subcomand and the options. This applies also for running the application from the IDE.

1. Parse command line arguments using PicoCLI. The best way to accomplish this goal is to have a look at the [Spring Boot example](https://picocli.info/#_spring_boot_example) and then to the [git example](https://github.com/remkop/picocli/blob/main/picocli-examples/src/main/java/picocli/examples/git/Git.java) that illustrates perfectly the usage of sub-commands. On the other hand side the git example mentioned before, illustrates perfectly [how to print help without specify the help option in the command line](https://github.com/remkop/picocli/issues/854)
    
1. [Show customized help in PicoCli](https://stackoverflow.com/questions/59004229/customized-help-display-in-picocli)

1. [Hide Spring boot banner](https://www.baeldung.com/spring-boot-disable-banner)

1. [Set level logging in SpringBoot](https://www.baeldung.com/spring-boot-logging). [Configure logging level in an application.yml](https://howtodoinjava.com/spring-boot2/logging/configure-logging-application-yml/)


