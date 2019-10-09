import java.util.ArrayList;
public class Group {
    ArrayList<User> users;
    String groupID;
    
    Group() {
        groupID = "";
        users = new ArrayList<User>();
    }
    Group(String groupID) {
        this.groupID = groupID;
        users = new ArrayList<User>();
    }
    public String toString() {
        return groupID;
    }
}