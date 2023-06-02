package is.tech.kafka;

import is.tech.dtos.CarManufacturerDto;
import is.tech.dtos.CarModelDto;
import is.tech.utils.AppConstants;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


@Service
public class KafkaModelService {
    private final ReplyingKafkaTemplate<String, Object, Object> replyingTemplate;

    public KafkaModelService(ReplyingKafkaTemplate<String, Object, Object> replyingTemplate) {
        this.replyingTemplate = replyingTemplate;
    }

    @SneakyThrows
    public List<CarModelDto> findAll() { //0
        System.out.println("Model service called");
        Object reply = sendMessage(
                "findAll",
                AppConstants.MODEL_TOPIC,
                AppConstants.MODEL_REPLIES,
                Integer.valueOf(AppConstants.MOD_FIND_ALL_PARTITION)
        );
        CarModelDto[] manus = (CarModelDto[]) reply;

        System.out.println("Model service exited");

        List<CarModelDto> modelDtos = new ArrayList<>(Arrays.asList(manus));

        return modelDtos;
    }

    @SneakyThrows
    public CarModelDto getById(Long id) { //1
        Object reply = sendMessage(
                id,
                AppConstants.MODEL_TOPIC,
                AppConstants.MODEL_REPLIES,
                Integer.valueOf(AppConstants.MOD_GET_BY_ID_PARTITION)
        );

        CarModelDto dto = (CarModelDto) reply;

        return dto;
    }

    @SneakyThrows
    public List<CarModelDto> findAllByName(String name) { //2
        Object reply = sendMessage(
                name,
                AppConstants.MODEL_TOPIC,
                AppConstants.MODEL_REPLIES,
                Integer.valueOf(AppConstants.MOD_FIND_ALL_BY_NAME_PARTITION)
        );

        CarModelDto[] dto = (CarModelDto[]) reply;

        List<CarModelDto> dtos = new ArrayList<>(Arrays.asList(dto));

        return dtos;
    }

    @SneakyThrows
    public CarModelDto save(CarModelDto carModelDto) { //3
        Object reply = sendMessage(
                carModelDto,
                AppConstants.MODEL_TOPIC,
                AppConstants.MODEL_REPLIES,
                Integer.valueOf(AppConstants.MOD_SAVE_PARTITION)
        );

        CarModelDto dto = (CarModelDto) reply;

        return dto;
    }

    public void deleteById(Long id) {
        send(id, AppConstants.MODEL_TOPIC, AppConstants.MOD_DELETE_BY_ID_PARTITION);
    }

    @SneakyThrows
    public void deleteAll() {
        send("deleteAll", AppConstants.MODEL_TOPIC, AppConstants.MOD_DELETE_ALL_PARTITION);
    }

    @SneakyThrows
    public List<CarModelDto> findAllByCarManufacturerId(Long manufacturerId) {
        Object reply = sendMessage(
                manufacturerId,
                AppConstants.MODEL_TOPIC,
                AppConstants.MODEL_REPLIES,
                Integer.valueOf(AppConstants.MOD_FIND_ALL_BY_MANU_PARTITION)
        );

        CarModelDto[] dto = (CarModelDto[]) reply;

        List<CarModelDto> dtos = new ArrayList<>(Arrays.asList(dto));

        return dtos;
    }

    @SneakyThrows
    public void deleteAllByCarManufacturerId(Long manufacturerId) {
        send(manufacturerId, AppConstants.MODEL_TOPIC, AppConstants.MOD_DELETE_ALL_BY_MANU_PARTITION);
    }

    public void send(Object message, String topic, String partition) {
        replyingTemplate.send(topic, Integer.valueOf(partition), partition, message);
    }

    public Object sendMessage(Object message, String topic, String replyTopic, Integer partition) throws ExecutionException, InterruptedException {
        ProducerRecord<String, Object> record = new ProducerRecord<>(topic, partition, partition.toString(), message);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_PARTITION, intToBytesBigEndian(partition)));
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes()));

        RequestReplyFuture<String, Object, Object> requestReply = replyingTemplate.sendAndReceive(record);

        ConsumerRecord<String, Object> consumerRecord = requestReply.get();

        return consumerRecord.value();
    }

    private static byte[] intToBytesBigEndian(final int data) {
        return new byte[] {
                (byte)((data >> 24) & 0xff), (byte)((data >> 16) & 0xff),
                (byte)((data >> 8) & 0xff), (byte)((data >> 0) & 0xff),
        };
    }
}
