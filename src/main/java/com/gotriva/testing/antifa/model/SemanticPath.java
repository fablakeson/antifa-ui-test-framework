package com.gotriva.testing.antifa.model;

import com.google.common.collect.ImmutableList;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.GrammaticalRelation;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This class represents a path traversal on a semantic graph. */
public class SemanticPath {

  private static final Logger LOGGER = LoggerFactory.getLogger(SemanticPath.class);

  /** The builder for semantic path traversal. */
  public static class Builder {

    /** Builder steps that guarantees no steps will repeat source and destination. */
    private LinkedHashSet<Step> steps;

    private Builder(LinkedHashSet<Step> steps) {
      this.steps = steps;
    }

    /** Get a new Buider for {@link SemanticPath}. */
    public static Builder newBuilder() {
      return new Builder(new LinkedHashSet<>());
    }

    /**
     * Adds a new step to this path.
     *
     * @param step the step builder
     * @return the Builder
     */
    public Builder newStep(Step.Builder step) {
      steps.add(step.build());
      return this;
    }

    /**
     * @return
     */
    public SemanticPath build() {
      return new SemanticPath(ImmutableList.copyOf(steps));
    }
  }

  /** The visitation steps triples. */
  private ImmutableList<Step> steps;

  /** Default constructor */
  private SemanticPath(ImmutableList<Step> steps) {
    this.steps = steps;
  }

  /**
   * Traverses the semantic graph on given path order and returns the interpreted action.
   *
   * @param graph the semantic graph
   * @return the decoded action if any
   */
  public Optional<Command> traverse(final SemanticGraph graph) {
    LOGGER.info("============== Start Traversing ===========");
    LOGGER.info("Steps: {}", steps);

    /** Get the graph root */
    IndexedWord root = graph.getFirstRoot();

    /** Sets the action root */
    Map<Command.ComponentType, Deque<IndexedWord>> actionsMap = new HashMap<>();
    putOrAppend(actionsMap, Command.ComponentType.COMMAND, root);

    LOGGER.info("My root is: {}", root);

    /** Traverses the graph from root */
    for (Step step : steps) {
      LOGGER.info("Current step: {}", step);

      /** Get source. */
      IndexedWord source = getFirst(actionsMap, step.getSource());
      LOGGER.info("Source: {}", source);

      /** Get edge. */
      GrammaticalRelation edge = step.getEdge();
      LOGGER.info("Edge: {}", edge);

      /** Get destination. */
      IndexedWord destination = graph.getChildWithReln(source, edge);
      LOGGER.info("Dest: {}", destination);

      /** Check if destination was found */
      if (destination == null) {
        LOGGER.info("Desination not found. Ending traversal.");
        LOGGER.info("========================================");
        return Optional.empty();
      }

      /** Add destination to the graph */
      putOrAppend(actionsMap, step.getDestination(), destination);
    }

    /** Build command from actions map with mandatory fields. */
    final Command.Builder commandBuilder =
        Command.builder()
            .setCommand(concatString(actionsMap.get(Command.ComponentType.COMMAND)))
            .setObject(concatString(actionsMap.get(Command.ComponentType.OBJECT)));

    /** Optional components */
    if (actionsMap.containsKey(Command.ComponentType.PARAMETER)) {
      commandBuilder.setParameter(concatString(actionsMap.get(Command.ComponentType.PARAMETER)));
    }
    if (actionsMap.containsKey(Command.ComponentType.TYPE)) {
      commandBuilder.setType(concatString(actionsMap.get(Command.ComponentType.TYPE)));
    }

    LOGGER.info("Map: {}", actionsMap);
    LOGGER.info("========================================");

    return Optional.of(commandBuilder.build());
  }

  /**
   * @return the {@link SemanticPath} builder.
   */
  public static Builder builder() {
    return Builder.newBuilder();
  }

  /**
   * Put or appends a single value to the map key.
   *
   * @param map the map
   * @param key the key
   * @param value the value
   */
  private <T> void putOrAppend(Map<T, Deque<IndexedWord>> map, T key, IndexedWord value) {
    if (map.containsKey(key)) {
      map.get(key).addFirst(value);
    } else {
      LinkedList<IndexedWord> list = new LinkedList<>();
      list.add(value);
      map.put(key, list);
    }
  }

  /**
   * Gets the first value from map for a given key.
   *
   * @param <T>
   * @param map
   * @param key
   * @return
   */
  private <T> IndexedWord getFirst(Map<T, Deque<IndexedWord>> map, T key) {
    return map.get(key).peekFirst();
  }

  /**
   * Contatenates the original text strings from the given list.
   *
   * @param list the list to concat strings
   * @return the contatenated string
   */
  private String concatString(Deque<IndexedWord> list) {
    return list.stream().map(IndexedWord::originalText).collect(Collectors.joining("-"));
  }
}
