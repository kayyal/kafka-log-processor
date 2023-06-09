import java.util.*;
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
    String topic = "testt";

    Map<String, String> logFiles = new HashMap<>();

    // create Consumer Config
    Properties properties = new Properties();
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        StringDeserializer.class.getName());
    properties.setProperty("group.id", groupId);
    properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    // create a consumer
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

    // subscribe to a topic
    consumer.subscribe(Arrays.asList(topic));

    // poll for data
    try {
      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
        for (ConsumerRecord<String, String> record : records) {
          logFiles.put(extractComponentName(record.key()), record.value());
          List<Component> components = Component.createComponents(logFiles);

          for (Component component : components) {
            for (LogMessage logMessage : component.getLogMessages()) {
            }
            LogAnalyzer.hasExceededThresholdWithinTimeWindow(component.getLogMessages(), 5, "ERROR",
                5);
            RuleEvaluator ruleEvaluator = new RuleEvaluator(
                "C:\\Users\\amirk\\IdeaProjects\\kafka-log-processor\\logs\\rules.txt");
            ruleEvaluator.evaluate(component.getLogMessages());
          }

        }

      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
//      consumer.close();
    }


  }


  public static String extractComponentName(String logFilePath) {
    String[] parts = logFilePath.split("\\\\"); // split the file path by backslashes
    String fileName = parts[parts.length - 1]; // get the file name from the last part of the path
    String[] fileNameParts = fileName.split("-"); // split the file name by hyphens
    return fileNameParts[0];
  }

}