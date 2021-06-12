package orangehrmtest.base;

import orangehrmtest.AppManager;
import orangehrmtest.data.AccountData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestBase {

    protected AppManager appManager;

    @BeforeEach
    public void setUp() throws Exception {
        appManager = AppManager.getInstance();
    }
}
