package is.tech.kafka;


import is.tech.dtos.CarModelDto;
import is.tech.mappers.CarModelMapper;
import is.tech.models.CarModel;
import is.tech.service.CarModelService;
import is.tech.utils.AppConstants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;


@Component
public class KafkaModelConsumer {
    private final CarModelService carModelService;

    public KafkaModelConsumer(CarModelService carModelService) {
        this.carModelService = carModelService;
    }

    @KafkaListener(
            id = AppConstants.MODEL_LISTENER_0,
            topicPartitions = @TopicPartition(topic = AppConstants.MODEL_TOPIC, partitions = {AppConstants.MOD_FIND_ALL_PARTITION})
    )
    @SendTo
    public CarModelDto[] findAll(String message) {
        System.out.println("Model consumer called");
        var models = carModelService.findAll();

        CarModelDto[] dtos = CarModelMapper.INSTANCE.map(models);

        return dtos;
    }

    @KafkaListener(
            id = AppConstants.MODEL_LISTENER_1,
            topicPartitions = @TopicPartition(topic = AppConstants.MODEL_TOPIC, partitions = {AppConstants.MOD_GET_BY_ID_PARTITION})
    )
    @SendTo
    public CarModelDto getById(Long id) {
        var model = carModelService.getById(id);

        return CarModelMapper.INSTANCE.map(model);
    }

    @KafkaListener(
            id = AppConstants.MODEL_LISTENER_2,
            topicPartitions = @TopicPartition(topic = AppConstants.MODEL_TOPIC, partitions = {AppConstants.MOD_FIND_ALL_BY_NAME_PARTITION})
    )
    @SendTo
    public CarModelDto[] findAllByName(String name) {
        var models = carModelService.findAllByName(name);
        CarModelDto[] dtos = CarModelMapper.INSTANCE.map(models);

        return dtos;
    }

    @KafkaListener(
            id = AppConstants.MODEL_LISTENER_3,
            topicPartitions = @TopicPartition(topic = AppConstants.MODEL_TOPIC, partitions = {AppConstants.MOD_SAVE_PARTITION})
    )
    @SendTo
    public CarModelDto save(CarModelDto carModelDto) {
        CarModel model = CarModelMapper.INSTANCE.map(carModelDto);
        var mod = carModelService.save(model);
        CarModelDto dto = CarModelMapper.INSTANCE.map(mod);

        return dto;
    }

    @KafkaListener(
            id = AppConstants.MODEL_LISTENER_5,
            topicPartitions = @TopicPartition(topic = AppConstants.MODEL_TOPIC, partitions = {AppConstants.MOD_DELETE_BY_ID_PARTITION})
    )
    @SendTo
    public void deleteById(Long id) {
        carModelService.deleteById(id);
    }

    @KafkaListener(
            id = AppConstants.MODEL_LISTENER_6,
            topicPartitions = @TopicPartition(topic = AppConstants.MODEL_TOPIC, partitions = {AppConstants.MOD_DELETE_ALL_PARTITION})
    )
    @SendTo
    public void deleteAll() {
        carModelService.deleteAll();
    }

    @KafkaListener(
            id = AppConstants.MODEL_LISTENER_7,
            topicPartitions = @TopicPartition(topic = AppConstants.MODEL_TOPIC, partitions = {AppConstants.MOD_FIND_ALL_BY_MANU_PARTITION})
    )
    @SendTo
    public CarModelDto[] findAllByCarManufacturerId(Long manufacturerId) {
        var models = carModelService.findCarModelsByManufacturerId(manufacturerId);
        CarModelDto[] dtos = CarModelMapper.INSTANCE.map(models);

        return dtos;
    }

    @KafkaListener(
            id = AppConstants.MODEL_LISTENER_8,
            topicPartitions = @TopicPartition(topic = AppConstants.MODEL_TOPIC, partitions = {AppConstants.MOD_DELETE_ALL_BY_MANU_PARTITION})
    )
    @SendTo
    public void deleteAllByCarManufacturerId(Long manufacturerId) {
        carModelService.deleteCarModelsByManufacturerId(manufacturerId);
    }
}
