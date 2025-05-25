package com.functional.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmazonCheckoutPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(AmazonCheckoutPage.class);
    // Locators using relative XPath
    private final By fullNameInput = By.xpath("//input[@name='enterAddressFullName']");
    private final By addressLine1Input = By.xpath("//input[@name='enterAddressAddressLine1']");
    private final By addressLine2Input = By.xpath("//input[@name='enterAddressAddressLine2']");
    private final By cityInput = By.xpath("//input[@name='enterAddressCity']");
    private final By stateInput = By.xpath("//input[@name='enterAddressStateOrRegion']");
    private final By pincodeInput = By.xpath("//input[@name='enterAddressPostalCode']");
    private final By phoneNumberInput = By.xpath("//input[@name='enterAddressPhoneNumber']");
    private final By codPaymentMethod = By.xpath("//input[@name='ppw-instrumentRowSelection' and contains(@value, 'COD')]");
    private final By placeOrderButton = By.xpath("//input[@name='placeYourOrder1']");
    private final By orderNumber = By.xpath("//div[contains(@class, 'order-number')]//span");
    private final By deliveryDate = By.xpath("//div[contains(@class, 'delivery-date')]//span");
    private final By cancelOrderButton = By.xpath("//a[contains(text(), 'Cancel')]");
    private final By confirmCancelButton = By.xpath("//input[@value='Cancel Order']");

    public AmazonCheckoutPage(WebDriver driver) {
        super(driver);
        logger.debug("Initialized AmazonCheckoutPage");
    }

    public void fillShippingAddress(String fullName, String addressLine1, String addressLine2, 
                                  String city, String state, String pincode, String phoneNumber) {
        logger.info("Filling shipping address form");
        try {
            logger.debug("Entering full name: {}", maskSensitiveData(fullName));
            sendKeys(fullNameInput, fullName);
            
            logger.debug("Entering address line 1: {}", maskSensitiveData(addressLine1));
            sendKeys(addressLine1Input, addressLine1);
            
            logger.debug("Entering address line 2: {}", maskSensitiveData(addressLine2));
            sendKeys(addressLine2Input, addressLine2);
            
            logger.debug("Entering city: {}", maskSensitiveData(city));
            sendKeys(cityInput, city);
            
            logger.debug("Entering state: {}", maskSensitiveData(state));
            sendKeys(stateInput, state);
            
            logger.debug("Entering pincode: {}", maskSensitiveData(pincode));
            sendKeys(pincodeInput, pincode);
            
            logger.debug("Entering phone number: {}", maskSensitiveData(phoneNumber));
            sendKeys(phoneNumberInput, phoneNumber);
            
            logger.info("Successfully filled shipping address form");
        } catch (Exception e) {
            String errorMsg = "Failed to fill shipping address: " + e.getMessage();
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }
    
    private String maskSensitiveData(String data) {
        if (data == null || data.length() < 4) {
            return "[MASKED]";
        }
        return data.substring(0, 2) + "****" + data.substring(data.length() - 2);
    }

    public void selectCODPayment() {
        logger.info("Selecting Cash on Delivery payment method");
        try {
            click(codPaymentMethod);
            logger.info("Successfully selected Cash on Delivery");
        } catch (Exception e) {
            String errorMsg = "Failed to select COD payment: " + e.getMessage();
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    public void placeOrder() {
        logger.info("Placing order");
        try {
            click(placeOrderButton);
            logger.info("Successfully placed order");
        } catch (Exception e) {
            String errorMsg = "Failed to place order: " + e.getMessage();
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    public String getOrderNumber() {
        logger.debug("Retrieving order number");
        try {
            String orderNum = getText(orderNumber);
            logger.info("Retrieved order number: {}", orderNum);
            return orderNum;
        } catch (Exception e) {
            String errorMsg = "Failed to get order number: " + e.getMessage();
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    public String getDeliveryDate() {
        logger.debug("Retrieving delivery date");
        try {
            String date = getText(deliveryDate);
            logger.info("Retrieved delivery date: {}", date);
            return date;
        } catch (Exception e) {
            String errorMsg = "Failed to get delivery date: " + e.getMessage();
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    public void cancelOrder() {
        logger.info("Starting order cancellation process");
        try {
            logger.debug("Clicking cancel order button");
            click(cancelOrderButton);
            
            logger.debug("Confirming order cancellation");
            click(confirmCancelButton);
            
            logger.info("Successfully cancelled the order");
        } catch (Exception e) {
            String errorMsg = "Failed to cancel order: " + e.getMessage();
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }
} 