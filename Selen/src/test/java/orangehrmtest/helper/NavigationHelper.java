package orangehrmtest.helper;

import orangehrmtest.AppManager;
import orangehrmtest.Settings;

public class NavigationHelper extends HelperBase {
    private String baseUrl;

    public NavigationHelper(AppManager appManager, String baseUrl) {
        super(appManager);
        this.baseUrl = baseUrl;
    }

    public void openHomePage() {
        driver.get(baseUrl);
    }

    public void openVacancyPage() {
        driver.get(baseUrl + Settings.getInstance().getVacancyPage());
    }
}
