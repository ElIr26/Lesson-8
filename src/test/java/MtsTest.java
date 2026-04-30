import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class MtsTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    private void acceptCookies() {
        try {
            Thread.sleep(2000);
            WebElement cookie = driver.findElement(By.xpath("//button[contains(text(),'Принять')]"));
            if (cookie.isDisplayed()) {
                cookie.click();
                Thread.sleep(500);
            }
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testBlockTitle() {
        driver.get("https://www.mts.by/");
        acceptCookies();
        String page = driver.getPageSource();
        Assertions.assertTrue(page.contains("Онлайн пополнение") && page.contains("без комиссии"));
        System.out.println("1. Название блока - OK");
    }

    @Test
    public void testPaymentLogos() {
        driver.get("https://www.mts.by/");
        acceptCookies();
        String page = driver.getPageSource();
        Assertions.assertTrue(page.contains("Visa"));
        Assertions.assertTrue(page.contains("МИР"));
        System.out.println("2. Логотипы Visa и МИР - OK");
    }

    @Test
    public void testDetailLink() {
        driver.get("https://www.mts.by/");
        acceptCookies();
        String page = driver.getPageSource();
        Assertions.assertTrue(page.contains("Подробнее о сервисе"));
        System.out.println("3. Ссылка Подробнее о сервисе - OK");
    }

    @Test
    public void testContinueButton() {
        driver.get("https://www.mts.by/");
        acceptCookies();
        try {
            WebElement serviceTab = driver.findElement(By.xpath("//*[contains(text(),'Услуги связи')]"));
            serviceTab.click();
            Thread.sleep(1000);
        } catch (Exception ignored) {
        }
        WebElement phone = driver.findElement(By.xpath("//input[contains(@placeholder,'Номер')]"));
        phone.clear();
        phone.sendKeys("297777777");
        System.out.println("Введен номер: 297777777");
        WebElement sum = driver.findElement(By.xpath("//input[contains(@placeholder,'Сумма')]"));
        sum.clear();
        sum.sendKeys("10");
        System.out.println("Введена сумма: 10");
        WebElement button = driver.findElement(By.xpath("//button[contains(text(),'Продолжить')]"));
        button.click();
        System.out.println("Кнопка Продолжить нажата");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {
        }
        String page = driver.getPageSource();
        Assertions.assertTrue(page.contains("Оплатить") || page.contains("карту"));
        System.out.println("4. Форма оплаты открылась - OK");
    }

    @Test
    public void runAllTests() {
        System.out.println("\n=== ЗАПУСК ВСЕХ ТЕСТОВ ===\n");
        testBlockTitle();
        testPaymentLogos();
        testDetailLink();
        testContinueButton();
        System.out.println("\n=== ВСЕ ТЕСТЫ ПРОЙДЕНЫ ===");
    }
}