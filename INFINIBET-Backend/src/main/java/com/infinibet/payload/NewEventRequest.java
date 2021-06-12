package com.infinibet.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class NewEventRequest {
    @NotNull
    private String title;
    private String description;
    @NonNull
    private List<Outcome> outcomes;

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    public static class Outcome {
        @NotNull
        private String name;
        @NotNull
        private BigDecimal coefficient;
    }
}
