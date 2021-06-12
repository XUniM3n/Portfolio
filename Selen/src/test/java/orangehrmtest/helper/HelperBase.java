package orangehrmtest.helper;

import orangehrmtest.AppManager;
import org.openqa.selenium.*;

public abstract class HelperBase {
    protected AppManager appManager;
    protected WebDriver driver;
    private boolean acceptNextAlert = true;

    public HelperBase(AppManager appManager) {
        this.appManager = appManager;
        this.driver = appManager.getDriver();
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
