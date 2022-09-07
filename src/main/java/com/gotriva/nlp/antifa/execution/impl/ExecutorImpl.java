package com.gotriva.nlp.antifa.execution.impl;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gotriva.nlp.antifa.element.Interactable;
import com.gotriva.nlp.antifa.exception.ExecutionException;
import com.gotriva.nlp.antifa.execution.ExecutionContext;
import com.gotriva.nlp.antifa.execution.Executor;
import com.gotriva.nlp.antifa.model.Command;
import com.gotriva.nlp.antifa.model.ExecutionResult;
import com.gotriva.nlp.antifa.model.ExecutionStep;
import com.gotriva.nlp.antifa.model.GenericPageObject;
import com.gotriva.nlp.antifa.strategy.ActionStrategy;
import com.gotriva.nlp.antifa.strategy.InteractableActionStrategy;
import com.gotriva.nlp.antifa.strategy.PageObjectActionStrategy;

/** This class implements {@link Executor}. */
public class ExecutorImpl implements Executor {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorImpl.class);

  /** The strategies for the commands names. */
  private final Map<String, ActionStrategy> strategies;

  /** The execution context for this execution. */
  private final ExecutionContext context;

  /** Default constructor. */
  ExecutorImpl(Map<String, ActionStrategy> strategies, ExecutionContext context) {
    this.strategies = strategies;
    this.context = context;
  }

  @Override
  @SuppressWarnings("unchecked")
  public ExecutionResult execute(List<Command> commands) {
    LOGGER.debug("Starting execution...");
    ExecutionResult.Builder result = ExecutionResult.builder();
    /** Within execution context */
    try (context) {
      /** Commands must be executed at the given order. */
      for (Command command : commands) {
        /** Start and end time for this command execution. */
        final ExecutionStep.Builder executionStep =
            ExecutionStep.builder().setCommand(command).startNow();
        /** Try to execute the command */
        try {
          /** Get strategy strategy for this command. */
          ActionStrategy strategy = strategies.get(command.getCommand());
          /** Get appropriated strategy type. */
          if (strategy instanceof InteractableActionStrategy) {
            /** Get the interactable object from context. */
            Interactable interactable = getInteractableFor(command);
            /** Try handle this interactable. */
            handleInteractable(
                (InteractableActionStrategy<Interactable>) strategy,
                interactable,
                command.getParameter());
          } else if (strategy instanceof PageObjectActionStrategy) {
            /** Try handle this page context action. */
            handlePageObject(
                (PageObjectActionStrategy) strategy, command.getObject(), command.getParameter());
          } else {
            /** strategy not reconigsed */
            throw new ExecutionException(
                "strategy for command '" + command.getCommand() + "' not found.");
          }
          /** This execution step was successful. */
          result.addStep(
              executionStep.endNow().withSuccess().setScreenshot(context.getScreenshot()));
          LOGGER.debug("Executing command: {} ... SUCCESS", command);
        } catch (Exception e) {
          /** This execution step was failed. */
          result.addStep(executionStep.endNow().withFail());
          /** Log the fail */
          LOGGER.debug("Executing command: {} ... FAIL", command);
          /** Wraps on execution exception if needed */
          if (e instanceof ExecutionException) {
            throw e;
          }
          throw new ExecutionException(e.getMessage(), e);
        }
      }
      result.withSuccess();
    } catch (ExecutionException ex) {
      LOGGER.error("Error executing command.", ex);
      result.withFail(ex.getMessage());
    } catch (IOException ioe) {
      LOGGER.error("Error closing the driver.", ioe);
    }
    /** Set final execution status */
    LOGGER.debug("Finishing execution...");
    return result.build();
  }

  /**
   * Handles the page object with the given strategy.
   *
   * @param <T>
   * @param strategy
   * @param page
   * @param url
   */
  private <T extends PageObjectActionStrategy> void handlePageObject(
      PageObjectActionStrategy strategy, String page, String parameter) {
    strategy.perform(context, page, parameter);
  }

  /**
   * Handles the interactable with the given strategy.
   *
   * @param <T>
   * @param strategy the given strategy.
   * @param interactable the interactable.
   * @param parameter the parameter
   */
  private <T extends Interactable> void handleInteractable(
      InteractableActionStrategy<T> strategy, T interactable, String parameter) {
    strategy.perform(interactable, parameter);
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
      /** Fail if type is not present */
      if (Objects.isNull(command.getType())) {
        throw new ExecutionException(
            MessageFormat.format(
                "Command 'type' must be present when object '{0}' is referenced at first time.",
                command.getObject()));
      }
      /** Creates the object on this page */
      pageObject.addElement(command.getObject(), By.id(command.getObject()), command.getType());
    }
    /** returns the interactable element */
    return pageObject.getElement(command.getObject());
  }
}
