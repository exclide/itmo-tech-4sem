package is.tech.kafka;

import is.tech.utils.AppConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic carManufacturerTopic() {
        return TopicBuilder.name(AppConstants.MANUFACTURER_TOPIC)
                .partitions(10)
                .build();
    }

    @Bean
    public NewTopic carManufacturerReplies() {
        return TopicBuilder.name(AppConstants.MANUFACTURER_REPLIES)
                .partitions(10)
                .build();
    }

    @Bean
    public NewTopic carModelTopic() {
        return TopicBuilder.name(AppConstants.MODEL_TOPIC)
                .partitions(10)
                .build();
    }

    @Bean
    public NewTopic carModelReplies() {
        return TopicBuilder.name(AppConstants.MODEL_REPLIES)
                .partitions(10)
                .build();
    }
}
