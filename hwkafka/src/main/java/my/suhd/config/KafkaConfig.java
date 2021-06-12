package my.suhd.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Getter
    @Value("${my.suhd.kafka.topic.driver-avaiable}")
    private String driverAvailableTopic;

    @Getter
    @Value("${my.suhd.kafka.topic.driver-event}")
    private String driverEventTopic;

    @Getter
    @Value("${my.suhd.kafka.topic.ride}")
    private String rideTopic;

    @Getter
    @Value("${my.suhd.kafka.topic.ride-current}")
    private String rideCurrentTopic;
}
