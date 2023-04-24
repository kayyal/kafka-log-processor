
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
public class ConsumerDemo {



  private static final Logger log = LoggerFactory.getLogger(ConsumerDemo.class.getSimpleName());
  public static void main(String[] args) {
    log.info("kafka Consumer");

    String bootstrapServer = "127.0.0.1:9092";
    String groupId = "my-java-application";
    String topic = "demo_java";

    // create Consumer Config
    Properties properties = new Properties();
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG , bootstrapServer);
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG , StringDeserializer.class.getName());
    properties.setProperty("group.id", groupId);
    properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG , "earliest");


    // create a consumer
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

    // subscribe to a topic
    consumer.subscribe(Arrays.asList(topic));

    // poll for data

    try {
      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records) {
          System.out.printf("Received message: key=%s, value=%s%n", record.key(), record.value());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      consumer.close();
    }

//    while (true) {
//
//      log.info("Polling");
//
//      ConsumerRecords<String, String> records =
//          consumer.poll(Duration.ofMillis(1000));
//
//      for (ConsumerRecord<String, String> record: records) {
//        log.info("Key: " + record.key() + ", Value: " + record.value());
//        log.info("Partition: " + record.partition() + ", Offset: " + record.offset());
//      }
//
//
//    }

  }

}
