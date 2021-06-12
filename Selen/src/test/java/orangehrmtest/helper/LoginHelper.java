package orangehrmtest.helper;

import orangehrmtest.AppManager;
import orangehrmtest.Settings;
import orangehrmtest.data.AccountData;
import org.openqa.selenium.By;

public class LoginHelper extends HelperBase {
    public LoginHelper(AppManager appManager) {
        super(appManager);
    }

    public void login(AccountData account) {
        appManager.getNavigationHelper().openHomePage();
        System.out.println("Opened login page");

        if (isLoggedIn()) {
            if (isLoggedIn(account.getUsername())) {
                return;
            }
            logout();
        }

        driver.findElement(By.id("txtUsername")).click();
        driver.findElement(By.id("txtUsername")).clear();
        driver.findElement(By.id("txtUsername")).sendKeys(account.getUsername());
        driver.findElement(By.id("txtPassword")).click();
        driver.findElement(By.id("txtPassword")).clear();
        driver.findElement(By.id("txtPassword")).sendKeys(account.getPassword());
        System.out.println("Entered username and password");
        driver.findElement(By.id("btnLogin")).click();
    }

    public void logout() {
        driver.get(appManager.getBaseUrl() + Settings.getInstance().getLogoutPage());
    }

    public boolean isLoggedIn() {
        if (appManager.getDriver().findElements(By.id("logInPanelHeading")).size() > 0) return false;
        return appManager.getDriver().findElement(By.id("welcome")).getText().contains("Welcome");
    }

    public boolean isLoggedIn(String username) {
        if (appManager.getDriver().findElements(By.id("logInPanelHeading")).size() > 0) return false;
        return appManager.getDriver().findElement(By.id("welcome")).getText().equals("Welcome " + username);
    }
}
