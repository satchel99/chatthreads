import java.util.ArrayList;
public class Conversation {
    Group group;
    ArrayList<Message> convo;
    Conversation () {
        group = new Group();
        convo = new ArrayList<Message>();
    }
    Conversation (Group group) {
        this.group = group;
        convo = new ArrayList<Message>();
    }
    
    public void addMessage(Message m) {
        convo.add(m);
    }
    public void recordConversation() {
        ArrayList<String> lines = new ArrayList<String>();
        for(Message m : convo) {
            lines.add(m.sender + "," + m.content);
        }
        FileManager.write(lines, "chathistory/" + group.groupID);
    }
    public void loadConversation() {
        System.out.println("reading");
        convo = new ArrayList<Message>();
        ArrayList<String> lines = FileManager.readFile("chathistory/" + group.groupID);
        for(String s : lines){
            String [] arr = s.split(",");
            Message m = new Message(arr[1], arr[0]);
            convo.add(m);
        }
    }
    public void display() {
        for(Message m : convo) {
            System.out.println(m);
        }
    }
}