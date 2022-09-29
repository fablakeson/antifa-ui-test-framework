package com.gotriva.nlp.antifa.strategy.impl;

import com.gotriva.nlp.antifa.element.impl.Selection;
import com.gotriva.nlp.antifa.strategy.InteractableActionStrategy;

public class SelectStrategy implements InteractableActionStrategy<Selection> {

  @Override
  public String getAction() {
    return "select";
  }

  @Override
  public void perform(Selection selection, String text) {
    selection.selectItem(text);
  }
}
