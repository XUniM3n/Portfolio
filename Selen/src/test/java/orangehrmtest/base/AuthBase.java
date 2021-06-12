package orangehrmtest.base;

import orangehrmtest.AppManager;
import orangehrmtest.Settings;
import orangehrmtest.data.AccountData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthBase {

    protected AppManager appManager;
    protected AccountData account;

    @BeforeEach
    public void setUp() throws Exception {
        appManager = AppManager.getInstance();
        Settings settings = Settings.getInstance();
        account = new AccountData(settings.getUsername(), settings.getPassword());
        appManager.getLoginHelper().login(account);
    }
}
