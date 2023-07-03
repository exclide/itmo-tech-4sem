package is.tech.kafka;

import is.tech.dtos.CarManufacturerDto;
import is.tech.mappers.CarManufacturerMapper;
import is.tech.models.CarManufacturer;
import is.tech.service.CarManufacturerService;
import is.tech.utils.AppConstants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class KafkaManufacturerConsumer {
    private final CarManufacturerService carManufacturerService;

    public KafkaManufacturerConsumer(CarManufacturerService carManufacturerService) {
        this.carManufacturerService = carManufacturerService;
    }

    @KafkaListener(
            id = AppConstants.MANUFACTURER_LISTENER_0,
            topicPartitions = @TopicPartition(topic = AppConstants.MANUFACTURER_TOPIC, partitions = {AppConstants.MANU_FIND_ALL_PARTITION})
    )
    @SendTo
    public CarManufacturerDto[] findAll(String message) {
        var manufacturers = carManufacturerService.findAll();

        CarManufacturerDto[] dtos = CarManufacturerMapper.INSTANCE.map(manufacturers);
        System.out.println(Arrays.toString(dtos));

        return dtos;
    }

    @KafkaListener(
            id = AppConstants.MANUFACTURER_LISTENER_1,
            topicPartitions = @TopicPartition(topic = AppConstants.MANUFACTURER_TOPIC, partitions = {AppConstants.MANU_GET_BY_ID_PARTITION})
    )
    @SendTo
    public CarManufacturerDto getById(Long id) {
        var manufacturer = carManufacturerService.getById(id);

        return CarManufacturerMapper.INSTANCE.map(manufacturer);
    }

    @KafkaListener(
            id = AppConstants.MANUFACTURER_LISTENER_2,
            topicPartitions = @TopicPartition(topic = AppConstants.MANUFACTURER_TOPIC, partitions = {AppConstants.MANU_FIND_ALL_BY_NAME_PARTITION})
    )
    @SendTo
    public CarManufacturerDto[] findAllByName(String name) {
        var manufacturers = carManufacturerService.findAllByName(name);
        CarManufacturerDto[] dtos = CarManufacturerMapper.INSTANCE.map(manufacturers);

        return dtos;
    }

    @KafkaListener(
            id = AppConstants.MANUFACTURER_LISTENER_3,
            topicPartitions = @TopicPartition(topic = AppConstants.MANUFACTURER_TOPIC, partitions = {AppConstants.MANU_SAVE_PARTITION})
    )
    @SendTo
    public CarManufacturerDto save(CarManufacturerDto carManufacturerDto) {
        CarManufacturer manufacturer = CarManufacturerMapper.INSTANCE.map(carManufacturerDto);
        var manu = carManufacturerService.save(manufacturer);
        CarManufacturerDto dto = CarManufacturerMapper.INSTANCE.map(manu);

        return dto;
    }

    @KafkaListener(
            id = AppConstants.MANUFACTURER_LISTENER_5,
            topicPartitions = @TopicPartition(topic = AppConstants.MANUFACTURER_TOPIC, partitions = {AppConstants.MANU_DELETE_BY_ID_PARTITION})
    )
    @SendTo
    public void deleteById(Long id) {
        carManufacturerService.deleteById(id);
    }

    @KafkaListener(
            id = AppConstants.MANUFACTURER_LISTENER_6,
            topicPartitions = @TopicPartition(topic = AppConstants.MANUFACTURER_TOPIC, partitions = {AppConstants.MANU_DELETE_ALL_PARTITION})
    )
    @SendTo
    public void deleteAll() {
        carManufacturerService.deleteAll();
    }
}
