# antifa-test-maven-plugin

## Antifa

Antifa is an automated interface testing framework that interprets instructions in natural language and converts them to webdriver commands, making it possible to write complete interface tests in a fast and understandable way, even for non-programmers.

This project is under development and in an experimental stage.

## How to use?

1. Clone this project.

2. Build the src code with maven.

3. Run your tests passing the input and output directories on `mvn` command:
```
mvn com.gotriva:antifa-test-maven-plugin:0.0.1-SNAPSHOT:ui-test \
    -DinputDirectory=<your_input_directory> \
    -DoutputDirectory=<your_output_directory>
```

## How to write tests?

1. Create a text (.txt or whathever extension you want) file.

2. Name this file with your test using `_` instead space.<br>
Ex.: `login_with_correct_credentials_then_success_message_appears.txt`

3. Write your test following this instructions:

- You **must** write individal sentences for each **command**.<br>
  Ex.: `Click on the login button.`

- Your **command** must be a valid regular english language phrase.

- The **command** is divided in 4 parts: **action**, **object**, **type** and **parameter**.

- The sentences **must** begin with an **action**.<br>
  Possible **actions** are described in the next section.

- The sentences **must** end with a period `.`

- Some **actions** requires a **parameter**, it must be written between quotes.<br>
  Ex.: `Write "Hello World" on the message box.`
  
- You must include the **object** name in your command. The framework use this to search your object *id* on webpage.

- If your **object** name is composited, the framework contatenates it with the `-` prefix for each word after the first.<br>
  Ex.: `Write "123456" on the phone number` --> `object_name: phone-number`.

- If you are interacting with the object for the first time, you must inform the **type** after the **name**.<br>
  Ex.: `Write "123456" on the phone number input` --> `type_name: input/text`.<br>
  Possible **types** are described on the next section.

## Types and Actions

### Element types

- **Button:** represents a HTML button, is capable of **click** action.

### Element Actions

### Page Actions
