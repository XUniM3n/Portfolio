package my.suhd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class Ride {
    private Long id;

    private Long passengerId;
    private Float latStart;
    private Float lonStart;
    private Float latEnd;
    private Float lonEnd;

    private Long timePublish;

    private Long timeStart;

    private Long timeEnd;

    private Long timeTook;
    private Long driverId;
    private Integer distance;
    private Integer cost;
}
