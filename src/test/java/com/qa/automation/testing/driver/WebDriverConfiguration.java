package com.qa.automation.testing.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.thucydides.core.webdriver.DriverSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.util.Properties;

@Configuration
public class WebDriverConfiguration implements DriverSource {
    Properties properties;
    WebDriver driver = null;

    public WebDriverConfiguration() {

    }

    @Override
    public WebDriver newDriver() {
        this.properties = new Properties();

        try {
            String propertyfile = System.getProperty("user.dir") + "/src/test/resources/serenity.properties";
            System.out.println("Load Properties file from " + propertyfile);
            FileInputStream inputStream = new FileInputStream(propertyfile);
            this.properties.load(inputStream);
            ProvidedBrowser browserName = ProvidedBrowser.valueOf(this.properties.getProperty("test.webdriver").isEmpty() ? "chrome".toUpperCase() : this.properties.getProperty("test.webdriver").toUpperCase());
            switch (browserName) {
                case CHROME:
                    WebDriverManager.chromedriver().setup();
                    return new ChromeDriver();
                case CHROME_HEADLESS:
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeHeadlessOptions = new ChromeOptions();
                    chromeHeadlessOptions.setHeadless(true);
                    this.driver = new ChromeDriver(chromeHeadlessOptions);
                    return this.driver;
                case FIREFOX:
                    WebDriverManager.firefoxdriver().setup();
                    return new FirefoxDriver();
                case FIREFOX_HEADLESS:
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.setHeadless(true);
                    this.driver = new FirefoxDriver(firefoxOptions);
                    return this.driver;
                case IE:
                    WebDriverManager.iedriver().setup();
                    return new InternetExplorerDriver();
                case EDGE:
                    WebDriverManager.edgedriver().setup();
                    return new EdgeDriver();
                case PHANTOMJS:
                    WebDriverManager.phantomjs().setup();
                    return new PhantomJSDriver();
                case SAFARI:
                    return new SafariDriver();
                case OPERA:
                    WebDriverManager.operadriver().setup();
                    return new OperaDriver();
                default:
                    WebDriverManager.chromedriver().setup();
                    return new ChromeDriver();
            }
        } catch (Exception var6) {
            return null;
        }
    }

    @Override
    public boolean takesScreenshots() {
        return true;
    }
}
