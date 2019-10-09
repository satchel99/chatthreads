public class Message {
    String content;
    String sender;
    
    Message() {
        content = "";
        sender = "";
    }
    Message(String content,String sender) {
        this.content = content;
        this.sender = sender;
    }
    public String toString() {
        return sender + ": " + content;
    }
}