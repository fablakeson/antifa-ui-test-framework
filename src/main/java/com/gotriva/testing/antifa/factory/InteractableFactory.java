package com.gotriva.testing.antifa.factory;

import com.gotriva.testing.antifa.element.Interactable;
import com.gotriva.testing.antifa.exception.InteractableTypeNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.openqa.selenium.WebElement;


/** This class creates interactables from given type name or default action. */
public class InteractableFactory {

    /** */
    private Map<String, Function<WebElement, Interactable>> typeMap;

    public InteractableFactory() {
        this.typeMap = new HashMap<>();
    }

    /**
     * Registers a creator function for a given type name.
     * 
     * @param <T>
     * @param type
     * @param creator
     * @return
     */
    public <T extends Interactable> InteractableFactory registerInteractableType(
            String type,
            Function<WebElement, Interactable> creator) {
        typeMap.put(type, creator);
        return this;
    }

    /**
     * Creates an interactable from a type name.
     * 
     * @param element
     * @param typeName
     * @return
     */
    public Interactable createInteractableFromType(WebElement element, String typeName) {
        Function<WebElement, Interactable> creator = typeMap.get(typeName);
        if (creator == null) {
            throw new InteractableTypeNotFoundException(typeName);
        }
        return creator.apply(element);
    }
}
