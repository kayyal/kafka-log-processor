import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemo {


  private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());
  public static void main(String[] args) {
//    System.out.println("Hello word");
    log.info("A Kafka Producer");
    String bootstrapServers = "127.0.0.1:9092";
//    String bootstrapServers = "172.26.178.65:9092";

    // create Producer properties
    Properties properties = new Properties();
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    // create a producer
    KafkaProducer<String , String> producer = new KafkaProducer<>(properties);
    // create a producer record
    ProducerRecord<String, String> producerRecord
        = new ProducerRecord<>("demo_java", "Hello world ");

    // send data in asynchronous way
    producer.send(producerRecord);

    // flush data - asynchronous

    producer.flush();

    // flush and close producer


    producer.close();


  }

}
