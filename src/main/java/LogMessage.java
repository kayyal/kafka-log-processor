import java.util.*;

public class LogMessage {

  private String date;
  private String time;
  private String logType;

  public LogMessage(String date, String time, String logType) {
    this.date = date;
    this.time = time;
    this.logType = logType;
  }

  public String getDate() {
    return date;
  }

  public String getTime() {
    return time;
  }

  public String getLogType() {
    return logType;
  }
}