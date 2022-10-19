# Antifa UI Test Framework

![build workflow](https://github.com/fablakeson/antifa-ui-test-framework/actions/workflows/maven.yml/badge.svg)

## What is Antifa Framework?

Antifa (Automated NLP-based Test Integration Framework Application) is an automated user interface testing framework that interprets instructions in natural language and converts them to webdriver commands, making it possible to write complete interface tests in a fast and understandable way, even for non-programmers.

*This project is under development and in an experimental stage.*

## How to use?

1. Clone this project.

2. Build the src code with maven.

3. Run your tests passing the input and output directories on `mvn` command:
```
mvn com.gotriva:antifa-test-maven-plugin:0.0.1-SNAPSHOT:ui-test \
    -DinputDirectory=<your_input_directory> \
    -DoutputDirectory=<your_output_directory> \
    -DoutputFormat=<HTML|XML>
```

## How to write tests?

1. Create a text (.txt or whathever extension you want) file.

2. Name this file with your test title using `_` instead space.<br>
Ex.: `login_with_correct_credentials_then_success_message_appears.txt`

3. Write your test following this instructions:

- You **must** write an individal sentence for each *command*.<br>
  Ex.: `Click on the @Login button.`

- The *command* **must** be a valid regular english language phrase.

- The *command* is divided in 4 parts: *action*, *object*, *type* and *parameter*.

- The sentences **must** begin with an *action*.<br>
  Possible *actions* are described in the next section.

- The sentences **must** end with a period `.`

- Your test **should** start with the *open* command.

- Your **must** *declare* the page elements before interact with then (see *declare* action on next section).

- Some *actions* requires a *parameter*, it *must* be written between quotes.<br>
  Ex.: `Write "Hello World" on the @Message box.`
  
- If you are **interacting** with the *object* for the **first time**, you **must** inform the *type alias* after the *name*.<br>
  Ex.: `Write "123456" on the @PhoneNumber input.`<br>
  The *input type alias* follows *@phoneNumber name*. The *types* are described on the next section.

## Types and Actions

The roles in this framework are divided into two groups: *types* and *actions*. An **element** of some *type* is capable of perform some *actions*. The actions are generally bounded to element types. Some elements are capable of multiple actions. Each *type* has one or more *aliases* to be used on instructions and make it more readable accordingly to the context.

### Element Actions

These are the available element *types* and *actions* on framework:

- **Button:** represents a HTML `input` of `button` type. Is capable of *click* action.<br>
Alias: `button`.<br>
Ex.: `Click on the @Login button.`

- **Checkbox:** represents an HTML `input` of `checkbox` type. Is capable of *check* and *uncheck* actions.<br>
Alias: `checkbox`, `opt-in`.<br>
Ex.: `Check @Agree checkbox.`<br>
Ex.: `Uncheck the @UserTermsAcceptance opt-in.`

- **File:** represents an HTML `input` of `file` type. Is capable of *upload* action.<br>
Alias: `file`.<br>
Ex.: `Upload path "C:/Documents/MyUser/Images/photo.jpg" to @UserPhoto file.`

- **Image:** represents an HTML image element. Is capable of *click* and *hover* actions.<br>
Alias: `image`, `icon`, `figure`.<br>
Ex.: `Click on the @UserProfile image.`<br>
Ex.: `Hover on @Info icon.`

- **Label:** represents an HTML textual element. Is capable of *hover*, *click* actions.<br>
Alias: `label`, `tag`, `header`.<br>
Ex.: `Click on @About label.`<br>
Ex.: `Click on @Menu tag.`<br>
Ex.: `Hover on @PageTitle header.`<br>

- **Radio:** represents an HTML `input` element of `radio` type. Is capable of *check* action.<br>
Alias: `radio`, `option`.<br>
Ex.: `Check the @Red option`.

- **Range:** represents an HTML `input`element of type `range`. Is capable of *set* action.<br>
Alias: `range`.<br>
Ex.: `Set @Volume range to 35%.`<br>
Ex.: `Set @MinimumPrice range to 750.`

- **Text:** represents an HTML `input` input of type `text`, `password` or a `textarea`. Is capable of *write* and *read* action.<br>
Alias: `textbox`, `input`, `field`, `display`, `message`.<br>
Ex.: `Write "testuser@testmail.com" to @Username input.`<br>
Ex.: `Write "This is a test message" to @Message textbox.`<br>
Ex.: `Read "Approved" on @Status message.`<br>
Ex.: `Read "Jhon Doe" on the @FullName field.`<br>

### Page Actions

Some actions are bounded to the parent of all *elements*, the document *page object*. We call it *page actions*. The page actions are:

- <em>**Declare**:</em> this actions associates a **name** to a reader friendly **label** and a HTML element found by a given **locator**.

  * The **name** must have the prefix `@` (*at*) and should not have spaces. You can use any naming convention you want (camelCase, snake_case, PascalCase), but use just one of then for good readability.<br>
  * The **label** must be enclosed in double quotes (`"`) and can contains any characters except double quotes.<br>
  * The **locator** must be a valid *CSS Locator* and must be enclosed in double quotes as well.<br>

  Look the examples below:

  Ex.: `Declare @UserName as "User Name" located by "input[name=username]".`<br>
  Ex.: `Declare @Password as "Password" located by "#password".`<br>
  Ex.: `Declare @Submit as "Submit" located by "input[type=submit]".`

- <em>**Store:**</em> this action store the value of given element on a variable declared with `$`. You can use this stored value on *Assert* action.<br>
Ex.: `Store @message display value on $message.`<br>
Ex.: `Store @result field value on $result.`<br>

- <em>**Assert:**</em> this action compares the expected (first) value with the value found on some variable (last). Currently just `equals` operation is supported.<br>
Ex.: `Assert "Success" equals to $result.`<br>
Ex.: `Assert "Some Username" equals to $username.`<br>

- <em>**Open:**</em> this action indicates the opening of a new page or visual context (dialog, modal, panel, tab). It can actively open a new page if an URL is provided. Using this declaration on to begin your tests.<br>
Ex.: `Open the login page on "http://www.mywebsite.com/login".`<br>
Ex.: `Open the settings tab.`

- <em>**Close:**</em> this action closes the current opened page or visual context, if any.<br>
  Ex.: `Close the message page.`

- <em>**Read:**</em> this action reads some visible text on page corpus.<br>
  Ex.: `Read "Login with success!" on the page.`<br>

- <em>**Scroll:**</em> this action scrolls the page *up* or *down*.<br>
  Ex.: `Scroll "down" the page.`<br>
  Ex.: `Scroll "up" the page.`


## Example Test Input File


File: `login_with_correct_info_then_success.txt`
```
### Your test may have comments like this

### Start test at page login
Open the login page at "http://some-website/login.html".

## Page elements definitions
Define @username as "User Name" located by "#username".
Define @password as "User Password" located by "#password".
Define @submit as "Submit" located by "#login".

## Test instructions
Write "teste@mail.com.br" to @username input.
Write "testpassword" to the @password input.
Click on the @submit button.
Read "Login with success!!!" on the page.

## Here your test ends
```

## Example Test Output (XML)

Test output contains the capitalized **test name** and some general **test stats**. For each step it includes the **start time** and **end time** of each instruction, the **status** (SUCCESS or FAIL), the **elapsed time** (ms), the **screenshot** `base64` and the interpreted **command**. In cases of FAIL, it includes the **fail reason** mesasge.

The **file name** is identified by the `antifa_test_` prefix and a **test time milisecs** suffix.

File: `antifa_test_login_with_correct_info_then_success_1663981217806.xml`
```xml
<executionResult>
  <testName>Login with correct info then success</testName>
  <startTime>2022-09-23T22:00:16.633797901</startTime>
  <endTime>2022-09-23T22:00:17.731487390</endTime>
  <elapsedTime unit="ms">1097</elapsedTime>
  <status>SUCCESS</status>
  <steps>
    <step>
      <startTime>2022-09-23T22:00:16.633797901</startTime>
      <endTime>2022-09-23T22:00:17.068923189</endTime>
      <elapsedTime unit="ms">435</elapsedTime>
      <result>SUCCESS</result>
      <screenshot content-type="image/jpeg">iVBORw0KG...pYQAAAAASUVORK5CYII=</screenshot>
      <command>
        <instruction>Open the login page at "http://some-website/login.html".</instruction>
        <action>open</action>
        <object>login</object>
        <parameter>http://some-website/login.html</parameter>
        <type>page</type>
      </command>
    </step>
    <step>
      <startTime>2022-09-23T22:00:17.068956434</startTime>
      <endTime>2022-09-23T22:00:17.068982205</endTime>
      <elapsedTime unit="ms">0</elapsedTime>
      <result>SUCCESS</result>
      <command>
        <instruction>Define @username as "User Name" located by "#username".</instruction>
        <action>define</action>
        <object>@username</object>
        <parameter>#username	User Name</parameter>
      </command>
    </step>
    ...
  </steps>
</executionResult>
```

## Reasons to use Antifa Framework

Here is a short list of reasons why you should consider adopting this framework:

1. Write tests faster using natural language, you don't even need to know a programming language.

2. It is agnostic to automation frameworks, you can use whatever automation framework you prefer (Selenium, Appium, UiAutomator, etc) without changing your test files.*

3. It's free and always will be! This is a community-maintained project and you can also help evolve it.

4. You support the anti-fascist struggle and want to make that clear to your organization and coworkers!

<small>* At current state we only support Selenium, but it other implementations should be added in future.</small>

## Are you a real NERD? Really? Then read our paper!

* [ANTIFA: An NLP-based Automated User Interface Test Framework](#)

## Just one more thing:

Please consider giving us a ⭐ to keep our ❤️ warm, thanks.
