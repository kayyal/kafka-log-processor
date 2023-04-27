import java.io.*;
//import java.io.FileReader;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;
import java.util.List;

public class RuleEvaluator {

  private List<Rule> rules;
  private LogAnalyzer logAnalyzer;
  private Connection connection;

  public RuleEvaluator(String rulesFilePath) throws IOException, SQLException {
    this.rules = readRulesFromFile(rulesFilePath);
    this.logAnalyzer = new LogAnalyzer();
    this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root",
        "@mir1234");
  }

  public void evaluate(List<LogMessage> logs) throws ParseException, SQLException {
    for (Rule rule : rules) {
      if (logAnalyzer.hasExceededThresholdWithinTimeWindow(logs, rule.getThreshold(),
          rule.getLogType(), rule.getTimeWindow())) {
        storeMessageInDatabase(rule.getLogType(), new Date(), rule.name);
      }
    }
  }

  private List<Rule> readRulesFromFile(String filePath) throws IOException {
    List<Rule> rules = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new FileReader(filePath));
    String line;
    while ((line = reader.readLine()) != null) {
      String[] parts = line.split(":");
      String[] ruleParts = parts[1].trim().split(",");
      String logType = ruleParts[0].trim().split("=")[1].trim();
      int threshold = Integer.parseInt(ruleParts[1].trim().split("=")[1].trim());
      int timeWindow = Integer.parseInt(
          ruleParts[2].trim().split("=")[1].trim().replace("min", ""));
      rules.add(new Rule(parts[0].trim(), logType, threshold, timeWindow));
    }
    reader.close();
    return rules;
  }

  private void storeMessageInDatabase(String logType, Date dateTime, String ruleName)
      throws SQLException {
    String query = "INSERT INTO messages (log_type, date_time , rule_name) VALUES (?, ? , ?)";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setString(1, logType);
    statement.setTimestamp(2, new Timestamp(dateTime.getTime()));
    statement.setString(3, ruleName);
    statement.executeUpdate();
  }

  private static class Rule {

    private String name;
    private String logType;
    private int threshold;
    private int timeWindow;

    public Rule(String name, String logType, int threshold, int timeWindow) {
      this.name = name;
      this.logType = logType;
      this.threshold = threshold;
      this.timeWindow = timeWindow;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getLogType() {
      return logType;
    }

    public void setLogType(String logType) {
      this.logType = logType;
    }

    public int getThreshold() {
      return threshold;
    }

    public void setThreshold(int threshold) {
      this.threshold = threshold;
    }

    public int getTimeWindow() {
      return timeWindow;
    }

    public void setTimeWindow(int timeWindow) {
      this.timeWindow = timeWindow;
    }
  }
}