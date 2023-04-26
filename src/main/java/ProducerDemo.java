import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemo {


  private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());

  public static void main(String[] args) throws IOException {

    Path path = Path.of("C:\\Users\\amirk\\IdeaProjects\\kafka-log-processor\\logs\\logs");

    FileIngesture fileIngesture = new FileIngesture(path);
    new Thread(()->{
      try {
        fileIngesture.start();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

    }).start();
    Map<String, String> logFiles = fileIngesture.getLogContents();

    log.info("A Kafka Producer");
    String bootstrapServers = "127.0.0.1:9092";
//    String bootstrapServers = "172.26.178.65:9092";

    // create Producer properties
    Properties properties = new Properties();
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class.getName());
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class.getName());

    // create a producer
    KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

//    // create a producer record
//    ProducerRecord<String, String> producerRecord
//        = new ProducerRecord<>("demo_java", "Hello world ");
//
//    // send data in asynchronous way
//    producer.send(producerRecord);

    // create producer record and send data in asynchronous way
    try {
      for (Map.Entry<String, String> entry : logFiles.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        producer.send(new ProducerRecord<>("testt", key, value));
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
//      producer.close();
    }
  }

}

