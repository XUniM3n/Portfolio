package my.suhd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class RideCurrent {
    private Long rideId;
    private Long passengerId;
    private Long driverId;
    private Float latStart;
    private Float lonStart;
    private Float latEnd;
    private Float lonEnd;
    private Integer status;
    private Long timePublish;
    private Long timeTook;
}
