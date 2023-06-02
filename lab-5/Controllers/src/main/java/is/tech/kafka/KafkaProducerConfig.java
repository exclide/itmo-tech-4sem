package is.tech.kafka;

import is.tech.utils.AppConstants;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;


import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    ReplyingKafkaTemplate<String, Object, Object> rkt(ProducerFactory<String, Object> pf,
                                                      ConcurrentKafkaListenerContainerFactory<String, Object> factory,
                                                      KafkaTemplate<String, Object> template) {

        factory.setReplyTemplate(template);
        ConcurrentMessageListenerContainer<String, Object> container =
                factory.createContainer(AppConstants.MANUFACTURER_REPLIES, AppConstants.MODEL_REPLIES);
        container.getContainerProperties().setGroupId(AppConstants.MANUFACTURER_REPLIES);
        return new ReplyingKafkaTemplate<>(pf, container);
    }

    @Bean
    KafkaTemplate<String, Object> template(ProducerFactory<String, Object> pf) {
        return new KafkaTemplate<>(pf);
    }
}
