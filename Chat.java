import java.util.*;
public class Chat {
  ArrayList<User> users;
  ArrayList<Group> groups;

  Chat() {
      users = new ArrayList<User>();
      groups = new ArrayList<Group>();
  }
    
  public void loadUsers() {
      users = new ArrayList<User>();
      ArrayList<String> lines = FileManager.readFile("users.txt");
      for(String s : lines) {
          User u = new User(s);
          users.add(u);
      }
  }
    
  public User findUser(String username) {
      for(User u : users) {
          if(u.username.equals(username)) {
              return u;
          }
      }
      return null;
  }
    
    public Group findGroup(String groupID) {
      for(Group g : groups) {
          if(g.groupID.equals(groupID)) {
              return g;
          }
      }
      return null;
  }
    
  public void loadGroups() {
      groups = new ArrayList<Group>();
      ArrayList<String> lines = FileManager.readFile("groups.txt");
      for(String s : lines) {
          String [] arr = s.split(",");
          Group g = new Group(arr[0]);
          for(int i = 1; i < arr.length; i++) {
              User found = findUser(arr[i]);
              if(found == null) {
                  found = new User(arr[0]);
              }
              g.users.add(found);
          }
          groups.add(g);
      }
  }
    
  public void writeUsers() {
      ArrayList<String> lines = new ArrayList<String>();
      for(User u : users) {
          lines.add(u.username);
      }
      FileManager.write(lines, "users.txt");
  }
    
public void writeGroups() {
      ArrayList<String> lines = new ArrayList<String>();
      for(Group g : groups) {
          String rs = "" + g.groupID + ",";
          for(User u : g.users) {
              rs = rs + u.username + ",";
          }
          lines.add(rs);
      }
      FileManager.write(lines, "groups.txt");
  }
    
  public void displayUsers() {
      for(User u : users) {
          System.out.println(u);
      }
  }
    
  public void displayGroups() {
      for(Group g : groups) {
          System.out.println(g);
      }
   } 
}