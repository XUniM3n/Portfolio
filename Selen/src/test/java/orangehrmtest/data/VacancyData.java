package orangehrmtest.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class VacancyData {
    private String jobTitle;
    private String name;
    private String hiringManager;
}
