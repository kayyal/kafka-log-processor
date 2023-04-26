import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogMessage {

  private final String date;
  private final String time;
  private final String logType;

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

  public Date getDateTime() throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return dateFormat.parse(date + " " + time);
  }
}