import java.text.ParseException;
import java.util.*;

public class LogAnalyzer {

  public static boolean hasThresholdLogs(List<LogMessage> logs, int threshold)
      throws ParseException {
    Collections.sort(logs, (a, b) -> {
      try {
        return a.getDateTime().compareTo(b.getDateTime());
      } catch (ParseException e) {
        e.printStackTrace();
        return 0;
      }
    });
    int start = 0;
    int count = 0;
    for (int end = 0; end < logs.size(); end++) {
      while (logs.get(end).getDateTime().getTime() - logs.get(start).getDateTime().getTime()
          > 5 * 60 * 1000) {
        count--;
        start++;
      }
      count++;
      if (count >= threshold) {
        return true;
      }
    }
    return false;
  }

  public static boolean hasExceededThresholdWithinTimeWindow(List<LogMessage> logs, int threshold,
      String logType, int timeWindow) throws ParseException {
    Collections.sort(logs, (a, b) -> {
      try {
        return a.getDateTime().compareTo(b.getDateTime());
      } catch (ParseException e) {
        e.printStackTrace();
        return 0;
      }
    });
    int start = 0;
    int count = 0;
    for (int end = 0; end < logs.size(); end++) {
      while (logs.get(end).getDateTime().getTime() - logs.get(start).getDateTime().getTime()
          > timeWindow * 60 * 1000) {
        if (logs.get(start).getLogType().equals(logType)) {
          count--;
        }
        start++;
      }
      if (logs.get(end).getLogType().equals(logType)) {
        count++;
      }
      if (count >= threshold) {
        System.err.println("Error: " + count + " " + logType + " logs detected within " + timeWindow
            + " minutes, threshold exceeded.");
        return true;
      }
    }
    return false;
  }
}