package my.suhd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class DriverEvent {
    private Long id;

    private Long driverId;

    private Float lat;
    private Float lon;
    private Integer status;

    private Long rideId;

    private Integer distance;
    private Integer duration;
    private Integer cost;
    private Long timePublish;
}

