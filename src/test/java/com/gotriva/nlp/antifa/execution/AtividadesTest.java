package com.gotriva.nlp.antifa.execution;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class AtividadesTest {
    
    WebDriver driver;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
    }

    @Test
    public void avidade_extra() {
        // Start test at google
        driver.get("http://www.math.com/students/calculators/source/basic.htm");

        // Page Definitions
        WebElement one = driver.findElement(By.cssSelector("input[name=one]"));
        WebElement two = driver.findElement(By.cssSelector("input[name=two]"));
        WebElement three = driver.findElement(By.cssSelector("input[name=three]"));
        WebElement plus = driver.findElement(By.cssSelector("input[name=plus]"));
        WebElement calculate = driver.findElement(By.cssSelector("input[name=DoIt]"));

        // Insert first term
        one.click();
        two.click();
        three.click();    
        // Plus operation
        plus.click();      
        // Insert second term
        three.click();
        two.click();
        one.click();
        // Get result
        calculate.click();
        
        // Assert value on display equals "444"
        WebElement display = driver.findElement(By.cssSelector("input[name=Input]"));
        assertEquals("444", display.getAttribute("value"));
    }

}
