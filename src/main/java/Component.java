import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Component {

  private String name;
  private List<LogMessage> logMessages;

  public Component(String name, List<LogMessage> logMessages) {
    this.name = name;
    this.logMessages = logMessages;
  }

  public String getName() {
    return name;
  }

  public List<LogMessage> getLogMessages() {
    return logMessages;
  }

  public void setLogMessages(List<LogMessage> logMessages) {
    this.logMessages = logMessages;
  }

  public static List<Component> createComponents(Map<String, String> map) {
    List<Component> components = new ArrayList<>();
    for (Map.Entry<String, String> entry : map.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      String[] logValues = value.split("\\r?\\n");
      List<LogMessage> logMessages = new ArrayList<>();
      for (String logValue : logValues) {
//        System.out.println("logValue = " + logValue);
        if (logValue.length()<13){
          continue;
        }
        // TODO
        String[] parts = logValue.split(" ");
        String date = parts[0];
        String time = parts[1];
        String logType = parts[3];
        logMessages.add(new LogMessage(date, time, logType));
      }
      boolean componentExists = false;
      for (Component component : components) {
        if (component.getName().equals(key)) {
          component.getLogMessages().addAll(logMessages);
          componentExists = true;
          break;
        }
      }
      if (!componentExists) {
        components.add(new Component(key, logMessages));
      }
    }
    return components;
  }

  public int countLogMessagesByType(String type) {
    int count = 0;
    for (LogMessage logMessage : logMessages) {
      if (logMessage.getLogType().equals(type)) {
        count++;
      }
    }
    return count;
  }

  public boolean hasExceededThresholdInLastFiveMinutes(String type, int threshold) {
    int count = 0;
    long currentTime = System.currentTimeMillis();
    long fiveMinutesAgo = currentTime - (5 * 60 * 1000);
    for (LogMessage logMessage : logMessages) {
      if (logMessage.getLogType().equals(type)) {
        String timestampString = logMessage.getDate() + " " + logMessage.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        try {
          Date timestamp = dateFormat.parse(timestampString);
          if (timestamp.getTime() >= fiveMinutesAgo && timestamp.getTime() <= currentTime) {
            count++;
          }
        } catch (ParseException e) {
          // handle parse exception
        }
      }
    }
    return count > threshold;
  }

}