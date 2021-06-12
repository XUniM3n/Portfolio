package orangehrmtest;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;

public class Settings {
    private static Settings value;
    @Getter
    @Setter
    private String baseUrl;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String logoutPage;
    @Getter
    @Setter
    private String vacancyPage;
    @Getter
    @Setter
    private String accountsValidPath;
    @Getter
    @Setter
    private String accountsInvalidPath;
    @Getter
    @Setter
    private String vacanciesPath;

    public static Settings getInstance() {
        if (value == null) {
            try {
                ObjectMapper mapper = new ObjectMapper(new JsonFactory());
                value = mapper.readValue(new File("C:\\Archive\\Shared\\Projects\\Java\\Selen\\src\\test\\resources\\settings.json"), Settings.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }
}
