# antifa-test-maven-plugin

## Antifa

Antifa is an automated interface testing framework that interprets instructions in natural language and converts them to webdriver commands, making it possible to write complete interface tests in a fast and understandable way, even for non-programmers.

This project is under development and in an experimental stage.

## How to use?

1. Clone this project.

2. Build the src code with maven.

3. Run your tests passing the input and output directories on `mvn` command:
```
mvn com.gotriva:antifa-test-maven-plugin:0.0.1-SNAPSHOT:ui-test -DinputDirectory=<your_input_directory> -DoutputDirectory=<your_output_directory>
```
