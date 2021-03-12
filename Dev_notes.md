
## Naming convention
The naming convention for my projects is the following:
  
```
groupId: com.francisconguemes.[name_of_aggregator]  
artifactId: name_of_the_project
```

In this case since this is not a maven aggregator project the groupId is simply _com.franciscoguemes_


## Generating the project structure

Project structure generated with the following command:

```sh
mvn archetype:generate -DgroupId=com.franciscoguemes -DartifactId=sudoku -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```
For further information on the command used you can see a brief tutorial on [maven](https://maven.apache.org/guides/getting-started/index.html#How_do_I_make_my_first_Maven_project) or simply on [generating projects with maven](https://mkyong.com/maven/how-to-create-a-java-project-with-maven/)
