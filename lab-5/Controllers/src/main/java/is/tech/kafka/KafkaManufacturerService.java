package is.tech.kafka;


import is.tech.dtos.CarManufacturerDto;
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
public class KafkaManufacturerService {
    private final ReplyingKafkaTemplate<String, Object, Object> replyingTemplate;

    public KafkaManufacturerService(ReplyingKafkaTemplate<String, Object, Object> replyingTemplate) {
        this.replyingTemplate = replyingTemplate;
    }

    @SneakyThrows
    public List<CarManufacturerDto> findAll() { //0
        Object reply = sendMessage(
                "findAll",
                AppConstants.MANUFACTURER_TOPIC,
                AppConstants.MANUFACTURER_REPLIES,
                Integer.valueOf(AppConstants.MANU_FIND_ALL_PARTITION)
        );
        CarManufacturerDto[] manus = (CarManufacturerDto[]) reply;


        List<CarManufacturerDto> manufacturerDtos = new ArrayList<>(Arrays.asList(manus));
        System.out.println(manufacturerDtos);

        return manufacturerDtos;
    }

    @SneakyThrows
    public CarManufacturerDto getById(Long id) { //1
        Object reply = sendMessage(
                id,
                AppConstants.MANUFACTURER_TOPIC,
                AppConstants.MANUFACTURER_REPLIES,
                Integer.valueOf(AppConstants.MANU_GET_BY_ID_PARTITION)
        );

        CarManufacturerDto dto = (CarManufacturerDto) reply;

        System.out.println(dto);
        return dto;
    }

    @SneakyThrows
    public List<CarManufacturerDto> findAllByName(String name) { //2
        Object reply = sendMessage(
                name,
                AppConstants.MANUFACTURER_TOPIC,
                AppConstants.MANUFACTURER_REPLIES,
                Integer.valueOf(AppConstants.MANU_FIND_ALL_BY_NAME_PARTITION)
        );

        CarManufacturerDto[] dto = (CarManufacturerDto[]) reply;

        List<CarManufacturerDto> dtos = new ArrayList<>(Arrays.asList(dto));

        return dtos;
    }

    @SneakyThrows
    public CarManufacturerDto save(CarManufacturerDto carManufacturerDto) { //3
        Object reply = sendMessage(
                carManufacturerDto,
                AppConstants.MANUFACTURER_TOPIC,
                AppConstants.MANUFACTURER_REPLIES,
                Integer.valueOf(AppConstants.MANU_SAVE_PARTITION)
        );

        CarManufacturerDto dto = (CarManufacturerDto) reply;

        return dto;
    }

    public void deleteById(Long id) {
        send(id, AppConstants.MANUFACTURER_TOPIC, AppConstants.MANU_DELETE_BY_ID_PARTITION);
    }

    @SneakyThrows
    public void deleteAll() {
        send("deleteAll", AppConstants.MANUFACTURER_TOPIC, AppConstants.MANU_DELETE_ALL_PARTITION);
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
