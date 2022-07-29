package com.gotriva.testing.antifa.element.impl;

import com.gotriva.testing.antifa.element.AbstractElement;
import com.gotriva.testing.antifa.element.Settable;
import java.util.Optional;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

/** This class represents a range input on UI. */
public class Range extends AbstractElement implements Settable {

  public Range(WebElement element) {
    super(element);
  }

  @Override
  public void setValue(String value) {
    /** Put focus on element. */
    focus();
    /** Check value type */
    if (value.endsWith("%")) {
      /** Value is relative */
      setRelativeValue(Double.parseDouble(value.replace("%", "")) / 100.0);
    } else {
      /** Value is absolute */
      setAbsuluteValue(Integer.parseInt(value));
    }
  }

  /**
   * Sets the rage to approximated percent value.
   *
   * @param percent the range percent to set
   */
  private void setRelativeValue(Double percent) {
    /** Get the absolute value */
    Integer range = getMax() - getMin();
    Integer value = (int) (range * percent);
    setAbsuluteValue(value);
  }

  /**
   * Sets an absolute value to the range. Get the closest lowest value if it is not present.
   *
   * @param value the value to set.
   */
  private void setAbsuluteValue(Integer value) {
    /** Get the closest <= value */
    for (Integer currentValue = getValue(); currentValue < value; currentValue = increment())
      ;
    for (Integer currentValue = getValue(); currentValue > value; currentValue = decrement())
      ;
  }

  /** Put focus on current element. */
  private void focus() {
    element.click();
  }

  /** Gets element value property. */
  private Integer getValue() {
    return Integer.parseInt(element.getAttribute("value"));
  }

  /** Gets element max property. */
  private Integer getMax() {
    return Optional.ofNullable(element.getAttribute("max"))
        .map(Integer::parseInt)
        .orElse(Integer.MAX_VALUE);
  }

  /** Gets element min property. */
  private Integer getMin() {
    return Optional.ofNullable(element.getAttribute("min"))
        .map(Integer::parseInt)
        .orElse(Integer.MIN_VALUE);
  }

  /** Increment current range step. */
  private Integer increment() {
    element.sendKeys(Keys.RIGHT);
    return getValue();
  }

  /** Decrement current range step. */
  private Integer decrement() {
    element.sendKeys(Keys.LEFT);
    return getValue();
  }
}
