package com.gotriva;

import com.gotriva.testing.antifa.element.impl.Button;
import com.gotriva.testing.antifa.element.impl.Text;
import com.gotriva.testing.antifa.execution.Executor;
import com.gotriva.testing.antifa.factory.InteractableFactory;
import com.gotriva.testing.antifa.handler.Handler;
import com.gotriva.testing.antifa.handler.impl.ClickableHandler;
import com.gotriva.testing.antifa.handler.impl.ClosePageHandler;
import com.gotriva.testing.antifa.handler.impl.OpenPageHandler;
import com.gotriva.testing.antifa.handler.impl.WriteHandler;
import com.gotriva.testing.antifa.model.Command;
import com.gotriva.testing.antifa.parsing.Interpreter;
import com.gotriva.testing.antifa.parsing.Parser;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

/**
 * Goal which touches a timestamp file.
 *
 * @goal touch
 * @phase process-sources
 */
public class MyMojo extends AbstractMojo {

  // TODO: Add missing interactable handlers.
  // TODO: add component injectable annotation to the handler services.
  // TODO: Add read tests from text files support.
  // TODO: Add post execution report support.
  // TODO: Add HTML report.
  // TODO: Refactor sysouts in Logger.
  // TODO: Refactor into dependency injection pattern.
  // TODO: Add plugin parameterization support.
  // TODO: Add unitary tests to each class.

  /** The handler strategies. */
  private static final Map<String, Handler> HANDLER_STRATEGIES =
      Map.of(
          /** Add text handler */
          "write", new WriteHandler(),
          /** Add click action hadndler */
          "click", new ClickableHandler(),
          /** Open page handler */
          "open", new OpenPageHandler(),
          /** Close page handler */
          "close", new ClosePageHandler());

  /** The default action interactable types types. */
  private static final Map<String, String> DEFAULT_INTERACTABLE_TYPES =
      Map.of(
          /** Add write default interactable type */
          "write", "text",
          /** Add click default interactable type */
          "click", "button");

  /** The interactable factory. */
  private static final InteractableFactory INTERACTABLE_FACTORY =
      new InteractableFactory()
          /** Register type interactable creators */
          .registerInteractableType("button", element -> new Button(element))
          .registerInteractableType("text", element -> new Text(element));

  /**
   * Location of the file.
   *
   * @parameter expression="project.build.directory"
   * @required
   */
  private File outputDirectory;

  /*
   * (non-Javadoc)
   *
   * @see org.apache.maven.plugin.Mojo#execute()
   */
  public void execute() throws MojoExecutionException {

    /** Enable just basic dependency parsing. */
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize,ssplit,pos,parse");

    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

    Parser parser = new Parser(pipeline, new Interpreter());

    List<String> instructions =
        Arrays.asList(
            "open the login page at \"http://localhost:8000/login.html\".",
            "write the username \"fabiojunior@gmail.com.br\".",
            "write the password \"password123\".",
            "write \"1234 1234 1234 1234\" to the credit card input.",
            "click on the login button.");

    List<Command> commands = parser.parse(instructions);

    /** Print decoded commands */
    System.out.println("The decoded commands: ");
    System.out.println(commands);
    System.out.println();

    Executor executor =
        new Executor(
            HANDLER_STRATEGIES,
            DEFAULT_INTERACTABLE_TYPES,
            INTERACTABLE_FACTORY,
            commands,
            new Properties());

    executor.execute();
  }
}
