# Kafka Log Processor

This is a simple Java program that reads log messages from a file, sends them to Kafka, and checks if certain kinds of log events (such as errors) happen in a window of time more than a threshold. If this condition is met, the program saves a message to a SQL database.

Dependencies
The program depends on the following libraries:

Kafka client (version 2.7.0 or higher)
Apache Commons CLI (version 1.4 or higher)
Apache Commons Lang (version 3.12 or higher)
HikariCP (version 4.0.3 or higher)
JDBC driver for your SQL database (e.g., MySQL Connector/J)
You can add these dependencies to your project using your favorite build tool (e.g., Maven or Gradle), or manually download the JAR files and add them to your classpath.

## Usage
To use the program, you need to have a running Kafka cluster and a SQL database (e.g., MySQL) with a table to store the messages.

## Configuration
You can configure the program using command-line arguments or a properties file. Here are the available options:

bootstrap.servers (required): A comma-separated list of Kafka broker addresses.
topic (required): The name of the Kafka topic to send the log messages to.
file (required): The path of the file to read the log messages from.
threshold (optional, default: 10): The threshold for the number of log events in the time window to trigger a message to the database.
db.url (required): The JDBC URL of the SQL database.
db.user (optional): The username to connect to the database.
db.password (optional): The password to connect to the database.
db.table (required): The name of the table to store the messages in.
You can either pass these options as command-line arguments (using the --option=value syntax), or create a config.properties file in the current directory with the options specified as option=value lines.
