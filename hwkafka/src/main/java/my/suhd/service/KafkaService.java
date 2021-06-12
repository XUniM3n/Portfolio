package my.suhd.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import my.suhd.config.KafkaConfig;
import my.suhd.model.DriverAvailable;
import my.suhd.model.DriverEvent;
import my.suhd.model.Ride;
import my.suhd.model.RideCurrent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
    private final KafkaConfig kafkaConfig;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper mapper = new ObjectMapper();

    public KafkaService(KafkaTemplate<String, String> kafkaTemplate, KafkaConfig kafkaConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaConfig = kafkaConfig;
    }

    public void sendDriverAvailable(DriverAvailable obj) {
        try {
            String json = mapper.writeValueAsString(obj);
            kafkaTemplate.send(kafkaConfig.getDriverAvailableTopic(), json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendDriverEvent(DriverEvent obj) {
        try {
            String json = mapper.writeValueAsString(obj);
            kafkaTemplate.send(kafkaConfig.getDriverEventTopic(), json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendRide(Ride obj) {
        try {
            String json = mapper.writeValueAsString(obj);
            kafkaTemplate.send(kafkaConfig.getRideTopic(), json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendRideCurrent(RideCurrent obj) {
        try {
            String json = mapper.writeValueAsString(obj);
            kafkaTemplate.send(kafkaConfig.getRideCurrentTopic(), json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
