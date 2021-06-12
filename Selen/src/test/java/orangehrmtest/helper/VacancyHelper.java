package orangehrmtest.helper;

import orangehrmtest.AppManager;
import orangehrmtest.data.VacancyData;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class VacancyHelper extends HelperBase {
    public VacancyHelper(AppManager appManager) {
        super(appManager);
    }

    public void addVacancy(VacancyData vacancy) {
        appManager.getNavigationHelper().openVacancyPage();

        driver.findElement(By.id("btnAdd")).click();
        driver.findElement(By.id("addJobVacancy_jobTitle")).click();
        new Select(driver.findElement(By.id("addJobVacancy_jobTitle"))).selectByVisibleText(vacancy.getJobTitle());
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='*'])[1]/following::option[2]")).click();
        driver.findElement(By.id("addJobVacancy_name")).click();
        driver.findElement(By.id("addJobVacancy_name")).clear();
        driver.findElement(By.id("addJobVacancy_name")).sendKeys(vacancy.getName());
        driver.findElement(By.id("addJobVacancy_hiringManager")).click();
        driver.findElement(By.id("addJobVacancy_hiringManager")).clear();
        driver.findElement(By.id("addJobVacancy_hiringManager")).sendKeys(vacancy.getHiringManager());
        driver.findElement(By.id("btnSave")).click();
    }

}
