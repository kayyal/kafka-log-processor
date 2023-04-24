import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

public class FileIngesture {

//  public static void main(String[] args) throws IOException {
//    fileCheckerReader("C:\\Users\\amirk\\IdeaProjects\\kafka-log-processor\\logs\\logs");
//  }

  public static Map<String , String > fileCheckerReader(String path ) throws IOException {
    Path logDirectory = Path.of(path);
    WatchService watchService = FileSystems.getDefault().newWatchService();
    logDirectory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
    Map<String, String> logContents = new HashMap<>();

    System.out.println("Monitoring directory for new log files...");
//    while (true) {
      WatchKey key;
      try {
        key = watchService.take();
      } catch (InterruptedException e) {
        return null;
      }

      for (WatchEvent<?> event : key.pollEvents()) {
        Path filePath = logDirectory.resolve((Path) event.context());
        String fileName =extractComponentName(filePath.toString());
        System.out.println("fileName = " + fileName);
        if (fileName.endsWith(".log")) {
          System.out.println("New log file detected: " + fileName);
          StringBuilder contentBuilder = new StringBuilder();
          try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
              contentBuilder.append(line);
              contentBuilder.append("\n");
            }
          } catch (IOException e) {
            System.err.println("Error reading log file: " + fileName);
            e.printStackTrace();
          }
          String fileContent = contentBuilder.toString();
          logContents.put(fileName, fileContent);
          System.out.println("File contents saved to HashMap.");
        }
      }

      boolean reset = key.reset();
      if (!reset) {
        System.err.println("WatchKey has been invalidated, exiting...");

      }
      return logContents;

    }

    public static String extractComponentName(String logFilePath) {
      String[] parts = logFilePath.split("\\\\"); // split the file path by backslashes
      String fileName = parts[parts.length - 1]; // get the file name from the last part of the path
      String[] fileNameParts = fileName.split("-"); // split the file name by hyphens
      return fileNameParts[0];
    }

  }





