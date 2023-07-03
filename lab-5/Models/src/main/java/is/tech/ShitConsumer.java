package is.tech;

import is.tech.dtos.CarModelDto;
import is.tech.mappers.CarModelMapper;
import is.tech.models.CarModel;
import is.tech.utils.AppConstants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
@Component("SHITCONSUMER")
public class ShitConsumer {
    @KafkaListener(
            id = AppConstants.MODEL_LISTENER_0,
            topicPartitions = @TopicPartition(topic = AppConstants.MODEL_TOPIC, partitions = {AppConstants.MOD_FIND_ALL_PARTITION})
    )
    @SendTo
    public CarModelDto[] findAll(String message) {
        System.out.println("shit consumer called");
        CarModelDto dto = new CarModelDto(0L, "kek", 5, 6, "Shit", null);
        return new CarModelDto[]{dto};
    }
}**/
