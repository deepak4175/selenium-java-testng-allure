package com.functional.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.time.Duration;
import java.util.PrimitiveIterator;

public class AmazonProductPage extends BasePage {
    // Locators using relative XPath
    // Filter locators
    private final By brandFilter = By.xpath("//span[text()='Brands']/following::span[contains(text(), 'Logitech')]");
    private final By priceFilterSection = By.xpath("//span[text()='Price']/parent::div");
    private final By priceRangeFilter = By.xpath("//span[text()='Price']/../..//input[contains(@id,'slider-item_upper')]");
    
    // Product and action locators
    private final By firstProduct = By.xpath("(//div[contains(@class, 's-result-item') and @data-component-type='s-search-result'])[1]//h2");
    private final By addToCartButton = By.xpath("(//div[contains(@class, 's-result-item') and @data-component-type='s-search-result'])[1]//button");
    private final By proceedToCheckoutButton = By.xpath("//input[@name='proceedToRetailCheckout']");

    // Results locator
    private final By filterResults = By.cssSelector("[data-component-type='s-search-result']");
    
    // Timeout for explicit waits
    private final Duration ELEMENT_TIMEOUT = Duration.ofSeconds(15);
    private final WebDriverWait wait;
    private final Actions action;
    private static final Logger logger = LoggerFactory.getLogger(AmazonProductPage.class);

    public AmazonProductPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, ELEMENT_TIMEOUT);
        this.action = new Actions(driver);
        logger.debug("Initialized AmazonProductPage with timeout: {}", ELEMENT_TIMEOUT);
    }

    public void applyBrandFilter() {
        logger.info("Applying brand filter");
        try {
            // Wait for brand filter to be visible and clickable
            logger.debug("Waiting for brand filter to be clickable");
            WebElement brandFilterElement = wait.until(ExpectedConditions.elementToBeClickable(brandFilter));
            
            // Scroll to the element to ensure it's in view
            logger.trace("Scrolling to brand filter element");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'})", brandFilterElement);
            
            // Add a small delay to ensure the element is ready for interaction
            try {
                logger.trace("Waiting for element to be ready for interaction");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logger.warn("Thread was interrupted while waiting for element to be ready", e);
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread was interrupted while applying brand filter", e);
            }
            
            // Click using JavaScript to avoid any potential click interception
            logger.debug("Clicking brand filter using JavaScript");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click()", brandFilterElement);
            
            // Wait for results to update after applying brand filter
            logger.debug("Waiting for results to update after applying brand filter");
            wait.until(ExpectedConditions.visibilityOfElementLocated(filterResults));
            logger.info("Successfully applied brand filter");
            
        } catch (Exception e) {
            String errorMsg = "Failed to apply brand filter: " + e.getMessage();
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    public void applyPriceRangeFilter(String EXPECTED_price) {
        logger.info("Applying price range filter");
        try {
            // Wait for price filter section to be visible
            logger.debug("Waiting for price filter section to be visible");
            WebElement priceSection = wait.until(ExpectedConditions.visibilityOf(driver.findElement(priceFilterSection)));
            // Scroll to price filter section to ensure it's in view
            logger.trace("Scrolling to price filter section");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'})", priceSection);
            
            // Add a small delay to ensure the element is ready for interaction
            try {
                logger.trace("Waiting for price filter to be ready for interaction");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logger.warn("Thread was interrupted while waiting for price filter to be ready", e);
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread was interrupted while applying price filter", e);
            }
            
            // Wait for price range filter to be visible and clickable
            logger.debug("Waiting for price range filter to be clickable");
            WebElement priceRangeElement = wait.until(ExpectedConditions.elementToBeClickable(priceRangeFilter));
            
            // Click using JavaScript to avoid any potential click interception
            logger.debug("Clicking price range filter using JavaScript");
            Boolean price=false;
            while(price==false){
                priceRangeElement.sendKeys(Keys.ARROW_LEFT);
                if(priceRangeElement.getAttribute("aria-valuetext").equals("₹"+EXPECTED_price)){
                    price=true;
                }
            }
            //action.clickAndHold(priceRangeElement).moveByOffset(-50, 0).release().build().perform();
            //Wait for results to update after applying price filter
            logger.debug("Waiting for results to update after applying price filter");
            wait.until(ExpectedConditions.visibilityOfElementLocated(filterResults));
            logger.info("Successfully applied price range filter");
        } catch (Exception e) {
            String errorMsg = "Failed to apply price range filter: " + e.getMessage();
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    public void selectFirstProduct() {
        logger.info("Selecting first product from the list");
        try {
            click(firstProduct);
            logger.debug("Successfully clicked on first product");
        } catch (Exception e) {
            String errorMsg = "Failed to select first product: " + e.getMessage();
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    public void FirstProductAddToCart() {
        logger.info("Adding product to cart");
        try {
            click(addToCartButton);
            logger.info("Successfully added product to cart");
        } catch (Exception e) {
            String errorMsg = "Failed to add product to cart: " + e.getMessage();
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    public void proceedToCheckout() {
        logger.info("Proceeding to checkout");
        try {
            click(firstProduct);
            click(proceedToCheckoutButton);
            logger.info("Successfully proceeded to checkout");
        } catch (Exception e) {
            String errorMsg = "Failed to proceed to checkout: " + e.getMessage();
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }
} 