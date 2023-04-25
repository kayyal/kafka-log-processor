public class Message {
  private String component_name;
  private String date;
  private String type;
  private String time;
  private String messageContent;

  public Message(String component_name, String date, String type, String time, String messageContent) {
    this.component_name = component_name;
    this.date = date;
    this.type = type;
    this.time = time;
    this.messageContent = messageContent;
  }

  public String getComponent_name() {
    return component_name;
  }

  public void setComponent_name(String component_name) {
    this.component_name = component_name;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getMessageContent() {
    return messageContent;
  }

  public void setMessageContent(String messageContent) {
    this.messageContent = messageContent;
  }
}
