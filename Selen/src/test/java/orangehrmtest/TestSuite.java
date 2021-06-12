package orangehrmtest;

import org.junit.jupiter.api.AfterAll;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
//@SelectClasses({LoginTests.class})
@SelectPackages("orangehrmtest.test")
public class TestSuite {

    @AfterAll
    public static void tearDown() {
        System.out.println("Teardown");
        AppManager.getInstance().stop();
    }
}
