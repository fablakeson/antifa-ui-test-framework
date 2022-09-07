# Antifa UI Test Framework

## What is Antifa?

Antifa (Automated NLP-based Test Integration Framework Application) is an automated interface testing framework that interprets instructions in natural language and converts them to webdriver commands, making it possible to write complete interface tests in a fast and understandable way, even for non-programmers.

*This project is under development and in an experimental stage.*

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

2. Name this file with your test title using `_` instead space.<br>
Ex.: `login_with_correct_credentials_then_success_message_appears.txt`

3. Write your test following this instructions:

- You **must** write an individal sentence for each *command*.<br>
  Ex.: `Click on the login button.`

- The *command* **must** be a valid regular english language phrase.

- The *command* is divided in 4 parts: *action*, *object*, *type* and *parameter*.

- The sentences **must** begin with an *action*.<br>
  Possible *actions* are described in the next section.

- The sentences **must** end with a period `.`

- Some *actions* requires a *parameter*, it *must* be written between quotes.<br>
  Ex.: `Write "Hello World" on the message box.`
  
- You **must** include the *object* name in your command. The framework use this to search the element *id* on HTML.

- If your *object* name is composited, the framework puts a hyphen `-` between the words after the first.<br>
  Ex.: `Write "123456" on the phone number.` &#8594; `object_name: phone-number`.

- If you are interacting with the *object* for the **first time**, you **must** inform the *type* after the *name*.<br>
  Ex.: `Write "123456" on the phone number input.` &#8594; `type_name: input/text`.<br>
  Possible *types* are described on the next section.

## Types and Actions

The roles in this framework are divided into two groups: *types* and *actions*. An **element** of some *type* is capable of perform some *actions*. The actions are generally bounded to element types. Some elements are capable of multiple actions. Each *type* has one or more *aliases* to be used on instructions and make it more readable accordingly to the context.

### Element Actions

These are the available element *types* and *actions* on framework:

- **Button:** represents a HTML `input` of `button` type. Is capable of *click* action.<br>
Alias: `button`.<br>
Ex.: `Click on the login button.`

- **Checkbox:** represents an HTML `input` of `checkbox` type. Is capable of *check* and *uncheck* actions.<br>
Alias: `checkbox`, `opt-in`.<br>
Ex.: `Check agree checkbox.`<br>
Ex.: `Uncheck user terms acceptance checkbox.`

- **File:** represents an HTML `input` of `file` type. Is capable of *upload* action.<br>
Alias: `file`.<br>
Ex.: `Upload path "C:/Documents/MyUser/Images/photo.jpg" to user photo file.`

- **Image:** represents an HTML image element. Is capable of *click* and *hover* actions.<br>
Alias: `image`, `icon`, `figure`.<br>
Ex.: `Click on the user profile image.`
Ex.: `Hover on the info icon.`

- **Label:** represents an HTML textual element. Is capable of *hover*, *click* actions.<br>
Alias: `label`, `tag`, `header`.<br>
Ex.: `Click on about label.`<br>
Ex.: `Click on menu tag.`<br>
Ex.: `Hover on page title header.`<br>

- **Radio:** represents an HTML `input` element of `radio` type. Is capable of *check* action.<br>
Alias: `radio`, `option`.<br>
Ex.: `Check the red option`.

- **Range:** represents an HTML `input`element of type `range`. Is capable of *set* action.<br>
Alias: `range`.<br>
Ex.: `Set the volume range to 35%.`<br>
Ex.: `Set the minimum price range to 750.`

- **Text:** represents an HTML `input` input of type `text`, `password` or a `textarea`. Is capable of *write* action.<br>
Alias: `textbox`, `input`, `field`.<br>
Ex.: `Write the email "testuser@testmail.com" on username input.`<br>
Ex.: `Write "This is a test message" to message textbox.`

### Page Actions

Some actions are bounded to the parent of all *elements*, the document *page object*. We call it *page actions*. The page actions are:

- <em>**Open:**</em> this action indicates the opening of a new page or visual context (dialog, modal, panel, tab). It can actively open a new page if an URL is provided.<br>
Ex.: `Open the login page on "http://www.mywebsite.com/login".`<br>
Ex.: `Open the settings tab.`

- <em>**Close:**</em> this action closes the current opened page or visual context, if any.<br>
  Ex.: `Close the message page.`

- <em>**Read:**</em> this action reads some visible text on page corpus.<br>
  Ex.: `Read the text "Profile".`<br>
  Ex.: `Read the message "Account blocked" on login status.`

- <em>**Roll:**</em> this action scrolls the page *up* or *down*.<br>
  Ex.: `Roll "down" the page.`<br>
  Ex.: `Roll "up" the page.`
