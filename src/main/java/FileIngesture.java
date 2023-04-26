import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class FileIngesture {

  private final WatchService watchService;
  private final Path logDirectory;
  private final Map<String, String> logContents;

  public FileIngesture(Path logDirectory) throws IOException {
    this.logDirectory = logDirectory;
    this.watchService = FileSystems.getDefault().newWatchService();
    this.logDirectory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
    this.logContents = new HashMap<>();
  }

  public static String extractComponentName(String logFilePath) {
    String[] parts = logFilePath.split("\\\\"); // split the file path by backslashes
    String fileName = parts[parts.length - 1]; // get the file name from the last part of the path
    String[] fileNameParts = fileName.split("-"); // split the file name by hyphens
    return fileNameParts[0];
  }

  public Map<String, String> getLogContents() {
    return logContents;
  }

  public void start() throws IOException {
    readExistingLogFiles();
    System.out.println("Monitoring directory for new log files...");
    while (true) {
      WatchKey key;
      try {
        key = watchService.take();
      } catch (InterruptedException e) {
        return;
      }
      for (WatchEvent<?> event : key.pollEvents()) {
        Path filePath = logDirectory.resolve((Path) event.context());
        String fileName = filePath.toString();
        if (fileName.endsWith(".log")) {
          System.out.println("New log file detected: " + fileName);
          String fileContent = readFileContent(filePath);
          logContents.put(fileName, fileContent);
          System.out.println("File contents saved to HashMap.");
//          System.out.println(fileName + fileName);
        }
      }
      boolean reset = key.reset();
      if (!reset) {
        System.err.println("WatchKey has been invalidated, exiting...");
        return;
      }
    }
  }

  private void readExistingLogFiles() throws IOException {
    DirectoryStream<Path> stream = Files.newDirectoryStream(logDirectory, "*.log");
    for (Path filePath : stream) {
      String fileName = filePath.toString();
      System.out.println("Reading existing log file: " + fileName);
      String fileContent = readFileContent(filePath);
      logContents.put(fileName, fileContent);
    }
    stream.close();
    System.out.println("Existing log files read and saved to HashMap.");
  }

  private String readFileContent(Path filePath) {
    StringBuilder contentBuilder = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        contentBuilder.append(line);
        contentBuilder.append("\n");
      }
    } catch (IOException e) {
      System.err.println("Error reading log file: " + filePath);
      e.printStackTrace();
    }
    return contentBuilder.toString();
  }

}
