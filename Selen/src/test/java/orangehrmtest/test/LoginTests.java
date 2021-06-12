package orangehrmtest.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import orangehrmtest.Settings;
import orangehrmtest.base.TestBase;
import orangehrmtest.data.AccountData;
import orangehrmtest.data.VacancyData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTests extends TestBase {
    public static List<AccountData> getValidAccountDataFromJson() {
        List<AccountData> accounts = new ArrayList<AccountData>();
        try {
            ObjectMapper mapper = new ObjectMapper(new JsonFactory());
            accounts = mapper.readValue(new File(Settings.getInstance().getAccountsValidPath()), new TypeReference<List<AccountData>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public static List<AccountData> getInvalidAccountDataFromJson() {
        List<AccountData> accounts = new ArrayList<AccountData>();
        try {
            ObjectMapper mapper = new ObjectMapper(new JsonFactory());
            accounts = mapper.readValue(new File(Settings.getInstance().getAccountsInvalidPath()), new TypeReference<List<AccountData>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @ParameterizedTest
    @MethodSource("getValidAccountDataFromJson")
    public void loginWithValidData(AccountData account){
        appManager.getLoginHelper().logout();
        appManager.getLoginHelper().login(account);
        assertTrue(appManager.getLoginHelper().isLoggedIn(account.getUsername()));
        System.out.println("Successfully logined");
    }

    @ParameterizedTest
    @MethodSource("getInvalidAccountDataFromJson")
    public void loginWithInvalidData(AccountData account){
        appManager.getLoginHelper().logout();
        appManager.getLoginHelper().login(account);
        assertFalse(appManager.getLoginHelper().isLoggedIn());
        System.out.println("Successfully entered invalid login");
    }
}
