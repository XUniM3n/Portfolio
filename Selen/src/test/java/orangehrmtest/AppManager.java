package orangehrmtest;

import lombok.Getter;
import orangehrmtest.helper.LoginHelper;
import orangehrmtest.helper.NavigationHelper;
import orangehrmtest.helper.VacancyHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.fail;

public class AppManager {
    @Getter
    private static AppManager value;
    @Getter
    private WebDriver driver;
    @Getter
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuilder verificationErrors = new StringBuilder();
    @Getter
    private LoginHelper loginHelper;
    @Getter
    private NavigationHelper navigationHelper;
    @Getter
    private VacancyHelper vacancyHelper;

    private AppManager() {
        System.setProperty("webdriver.gecko.driver", BrowserSetup.GECKODRIVER_PATH);
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary(BrowserSetup.FIREFOX_PATH);
        options.setProfile(BrowserSetup.firefoxProfile());
        System.out.println("WebDriver: Stage 1");
        driver = new FirefoxDriver(options);
        System.out.println("WebDriver: Stage 2");
        Settings settings = Settings.getInstance();
        baseUrl = settings.getBaseUrl();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        navigationHelper = new NavigationHelper(this, baseUrl);
        loginHelper = new LoginHelper(this);
        vacancyHelper = new VacancyHelper(this);
    }

    public static AppManager getInstance() {
        if (value == null)
            value = new AppManager();
        return value;
    }

    @Override
    public void finalize(){
        stop();
    }

    public void stop() {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}
