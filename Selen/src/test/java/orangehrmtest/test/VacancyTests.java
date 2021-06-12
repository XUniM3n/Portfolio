package orangehrmtest.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import orangehrmtest.Settings;
import orangehrmtest.base.AuthBase;
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

import static org.junit.jupiter.api.Assertions.assertTrue;

public class VacancyTests extends AuthBase {
    public static List<VacancyData> getVacancyDataFromJson() {
        List<VacancyData> vacancies = new ArrayList<VacancyData>();
        try {
            ObjectMapper mapper = new ObjectMapper(new JsonFactory());
            return mapper.readValue(new File(Settings.getInstance().getVacanciesPath()), new TypeReference<List<VacancyData>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vacancies;
    }

    @ParameterizedTest
    @MethodSource("getVacancyDataFromJson")
    public void testAddVacancy(VacancyData vacancy) {
        appManager.getVacancyHelper().addVacancy(vacancy);
        appManager.getNavigationHelper().openVacancyPage();
        assertTrue(appManager.getDriver().findElement(By.id("resultTable")).getText().contains(vacancy.getName()));
        System.out.println("Test complete");
    }
}
