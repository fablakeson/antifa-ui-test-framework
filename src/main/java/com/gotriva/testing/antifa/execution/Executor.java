package com.gotriva.testing.antifa.execution;

import com.gotriva.testing.antifa.element.Interactable;
import com.gotriva.testing.antifa.factory.InteractableFactory;
import com.gotriva.testing.antifa.handler.ActionHandler;
import com.gotriva.testing.antifa.handler.InteractableActionHandler;
import com.gotriva.testing.antifa.handler.PageObjectActionHandler;
import com.gotriva.testing.antifa.model.Command;
import com.gotriva.testing.antifa.model.ExecutionContext;
import com.gotriva.testing.antifa.model.ExecutionResult;
import com.gotriva.testing.antifa.model.ExecutionStep;
import com.gotriva.testing.antifa.model.GenericPageObject;
import com.gotriva.testing.antifa.utils.WebDriverUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/** This class is responsible for executing a list of commands for a given test. */
public class Executor {

  /** The execution status types. */
  public enum Status {
    /** The execution has not started yed. */
    NOT_STARTED,
    /** The execution is running. */
    EXECUTING,
    /** The execution was finished. */
    FINISHED
  }

  /** The handler strategies for the commands names. */
  private final Map<String, ActionHandler> handlerStrategies;

  /** The default interactable types for the command names. */
  private final Map<String, String> defaultTypes;

  /** The list of commands to be executed. */
  private final List<Command> commands;

  /** The execution web driver. */
  private final WebDriver driver;

  /** The execution context for this execution. */
  private final ExecutionContext context;

  /** The result of this execution. */
  private final ExecutionResult result;

  /** The execution status. */
  private Status status = Status.NOT_STARTED;

  /** Default constructor. */
  public Executor(
      Map<String, ActionHandler> handlerStrategies,
      Map<String, String> defaultTypes,
      InteractableFactory factory,
      List<Command> commands,
      Properties properties) {
    this.handlerStrategies = handlerStrategies;
    this.defaultTypes = defaultTypes;
    this.commands = commands;
    this.driver = WebDriverUtils.createWebDriver(properties);
    this.context = ExecutionContext.newContext(driver, factory);
    this.result = ExecutionResult.newResult();
  }

  /**
   * Executes the given commands on this executor.
   *
   * @return the execution result.
   */
  @SuppressWarnings("unchecked")
  public ExecutionResult execute() {
    System.out.println("Starting execution...");
    /** Validate execution state. */
    if (status != Status.NOT_STARTED) {
      throw new IllegalStateException("Executor has already started execution.");
    }
    /** Set execution status. */
    status = Status.EXECUTING;
    /** Commands must be executed at the given order. */
    for (Command command : commands) {
      System.out.print("Executing command: " + command + "...");
      /** Start and end time for this command execution. */
      final LocalDateTime startTime = LocalDateTime.now();
      /** Try to execute the command */
      try {
        /** Get handler strategy for this command. */
        ActionHandler handler = handlerStrategies.get(command.getCommand());
        /** Get appropriated handler type. */
        if (handler instanceof InteractableActionHandler) {
          /** Get the interactable object from context. */
          Interactable interactable = getInteractableFor(command);
          /** Try handle this interactable. */
          handleInteractable(
              (InteractableActionHandler<Interactable>) handler,
              interactable,
              command.getParameter());
        } else if (handler instanceof PageObjectActionHandler) {
          /** Try handle this page context action. */
          handlePageObject(
              (PageObjectActionHandler) handler, command.getObject(), command.getParameter());
        } else {
          /** Handler not reconigsed */
          throw new RuntimeException(
              "Handler for command '" + command.getCommand() + "' not found.");
        }
        /** Get a snapshot of current execution completion */
        String snapshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        /** This execution step was successful. */
        result.addStep(
            new ExecutionStep(
                command, startTime, LocalDateTime.now(), ExecutionStep.Result.SUCCESS, snapshot));
        System.out.println("SUCCESS");
      } catch (RuntimeException re) {
        /** This execution step was failed. */
        result.addStep(
            new ExecutionStep(
                command, startTime, LocalDateTime.now(), ExecutionStep.Result.FAIL, null));

        System.out.println("FAIL");
        re.printStackTrace();
        /** Break the command execution loop */
        break;
      }
    }

    System.out.println("Finishing execution...");

    /** Close the webdriver */
    driver.close();

    /** Set final execution status */
    status = Status.FINISHED;

    return result;
  }

  /**
   * Handles the page object with the given handler.
   *
   * @param <T>
   * @param handler
   * @param page
   * @param url
   */
  private <T extends PageObjectActionHandler> void handlePageObject(
      PageObjectActionHandler handler, String page, String parameter) {
    handler.handle(context, page, parameter);
  }

  /**
   * Handles the interactable with the given handler.
   *
   * @param <T>
   * @param handler the given handler.
   * @param interactable the interactable.
   * @param parameter the parameter
   */
  private <T extends Interactable> void handleInteractable(
      InteractableActionHandler<T> handler, T interactable, String parameter) {
    handler.handle(interactable, parameter);
  }

  /**
   * Gets the interactable on context for given command. Creates one if it does not exists.
   *
   * @param command
   * @return
   * @throws InterruptedException
   */
  private Interactable getInteractableFor(Command command) {
    /** Get current page. */
    GenericPageObject pageObject = context.getCurrentPage();
    /** Check if current object not exists on page */
    if (!pageObject.hasElement(command.getObject())) {
      /** Get default type for command if not present */
      String type =
          command.getType() != null ? command.getType() : defaultTypes.get(command.getCommand());

      /** Creates the object on this page */
      pageObject.addElement(command.getObject(), By.name(command.getObject()), type);
    }
    /** returns the interactable element */
    return pageObject.getElement(command.getObject());
  }

  /** Getters */
  public Status getStatus() {
    return status;
  }

  public ExecutionResult getResult() {
    return result;
  }
}
