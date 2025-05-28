package com.functional.tests;

import com.functional.pages.AmazonHomePage;
import com.functional.pages.AmazonProductPage;
import com.functional.pages.AmazonCheckoutPage;
import com.functional.utils.TestUtils;
import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.manager.SeleniumManager;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;

@Feature("Amazon Order Workflow")
public class AmazonOrderTest {
    private WebDriver driver;
    private AmazonHomePage homePage;
    private AmazonProductPage productPage;
    private AmazonCheckoutPage checkoutPage;
    private static final String EMAIL = "8294381810";
    private static final String PASSWORD = "Testing@121";

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--remote-allow-origins=*");
        
        // Selenium Manager will automatically handle the driver binary
        driver = new ChromeDriver(options);
        
        // Set timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        
        // Initialize page objects
        homePage = new AmazonHomePage(driver);
        productPage = new AmazonProductPage(driver);
        checkoutPage = new AmazonCheckoutPage(driver);
    }

    @Test
    @Story("Complete Order Workflow")
    @Description("Test the complete order workflow from search to order placement and cancellation")
    public void testCompleteOrderWorkflow() {
        try {
            // Step 1: Navigate to Amazon.in
            homePage.navigateToHomePage();
            Assert.assertTrue(homePage.isHomePageLoaded(), "Homepage should load within 3 seconds");

            // Step 2: Login
            homePage.login(EMAIL, PASSWORD);

            // Step 3: Search for product
            homePage.searchProduct("wireless mouse");

            // Step 4: Apply filters
            productPage.applyBrandFilter();
            productPage.applyPriceRangeFilter("2,500");

            // Step 5: Select and add to cart
            productPage.FirstProductAddToCart();

            // Step 6: Proceed to checkout
            productPage.proceedToCheckout();

            // Step 7: Fill shipping address
            checkoutPage.fillShippingAddress(
                "Deepak",
                "s121",
                "up01, laxmi apartment",
                "Delhi",
                "Delhi",
                "110030",
                "9876543210"
            );

            // Step 8: Select payment method and place order
            checkoutPage.selectCODPayment();
            checkoutPage.placeOrder();

            // Step 9: Verify order details
            String orderNumber = checkoutPage.getOrderNumber();
            String deliveryDate = checkoutPage.getDeliveryDate();
            Assert.assertNotNull(orderNumber, "Order number should be present");
            Assert.assertNotNull(deliveryDate, "Delivery date should be present");
            // Step 10: Cancel order
            checkoutPage.cancelOrder();
        } catch (Exception e) {
            TestUtils.takeScreenshot(driver);
            throw e;
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                // Ignore any errors during driver cleanup
            }
        }
    }
} 