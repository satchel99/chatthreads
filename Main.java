import java.util.ArrayList;
import java.util.Scanner;
public class Main {
    Conversation conv;
    class DisplayThread implements Runnable 
    { 
        public void run() 
        { 
            try
            { 
                while(true) {
                  conv.loadConversation();
                  conv.display();
                  Thread.sleep(1500);  
                } 
            } 
            catch (Exception e) 
            { 
                System.err.println(e);
                System.out.println ("Exception is caught"); 
            } 
        } 
    }
    
    class WriteThread implements Runnable 
    { 
        public void run() 
        { 
            try
            { 
                while(true) {
                    System.out.println("Enter your message");
                    
                    Scanner scan = new Scanner(System.in);
                    String content = scan.nextLine();
                    Message m = new Message(content, currentUser.username);
                    conv.addMessage(m);
                    conv.recordConversation();
                    conv.convo.add(m);
            } }
            catch (Exception e) 
            { 
                System.out.println ("Exception is caught"); 
            } 
        } 
    }
 
    
    Chat chat;
    User currentUser;
    Main() {
        chat = new Chat();
        currentUser = null;
    }
    
    public void setup() {
        chat.loadUsers();
        chat.loadGroups();
    }
    
    public void newUser() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter your username");
        String name = scan.next();
        currentUser = new User(name);
        chat.users.add(currentUser);
        System.out.println("Registered!");
    }
    
    public boolean login() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter your username");
        String name = scan.next();
        currentUser = chat.findUser(name);
        if(currentUser == null) {
            return false;
        }
        else {
            return true;
        }
    }
    
    public void newGroup() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter your group name");
        String name = scan.next();
        Group g = new Group(name);
        chat.groups.add(g);
        System.out.println("New group added!");
    }
    
    public int displayMainMenu() {
        System.out.println("1 Login");
        System.out.println("2 Create a new account");
        System.out.println("3 Display all users");
        System.out.println("4 Quit");
        Scanner scan = new Scanner(System.in);
        return scan.nextInt();
        }
    
    
    public int displayLoggedIn() {
        System.out.println("Welcome " + currentUser.username);
        System.out.println("1 See your groups");
        System.out.println("2 Start a new group");
        System.out.println("3 See all groups");
        System.out.println("4 Enter a group chat");
        System.out.println("5 Quit");
        Scanner scan = new Scanner(System.in);
        return scan.nextInt();
    }
    
    public boolean inGroup(Group g) {
        for(User u : g.users) {
            if(u.username.equals(currentUser.username)) {
                return true;
            }
        }
        return false;
    }
    
    public void displayYourGroups() {
        for(Group g : chat.groups) {
            if(inGroup(g)) {
                System.out.println(g);
            } 
        }
    }
    
    public void converse(Group g) {
        System.out.println("Welcome to the convo!");
        System.out.println("Enter 'leave()' at any time to quit!");
        conv = new Conversation(g);
        DisplayThread dt = new DisplayThread();
        //WriteThread wt = new WriteThread();
        Thread object1 = new Thread(dt);
        //Thread object2 = new Thread(wt);
        object1.start();
        //object2.start();
        while(true) {
            System.out.println("Enter your message");

            Scanner scan = new Scanner(System.in);
            String content = scan.nextLine();
            Message m = new Message(content, currentUser.username);
            conv.addMessage(m);
            conv.recordConversation();
            conv.convo.add(m);
        }
    }
    
    public void enterGroup() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the group name");
        String name = scan.next();
        Group found = null;
        for(Group g : chat.groups) {
            if(g.groupID.equals(name)) {
                found = g;
                break;
            }
        }
        if(found == null) {
            System.out.println("that group does exist");
        }
        else if(!inGroup(found)) {
            System.out.println("You are not in that group");
        }
        else {
            converse(found);
        }
    }
    
    public void loggedIn() {
        boolean loggedIn = true;
        while(loggedIn) {
            int choice = displayLoggedIn();
            if(choice == 1) {
                displayYourGroups();
            }
            if(choice == 2) {
                newGroup();
            }
            if(choice == 3) {
                chat.displayGroups();
            }
            if(choice == 4) {
                enterGroup();
            }
            if(choice == 5) {
                System.out.println("Logging out!");
                loggedIn = false;
                break;
            }
        }
    }
    
    
    public void startChat() {
        setup();
        boolean mainMenu = true;
        while(mainMenu) {
            int choice = displayMainMenu();
            if(choice == 1) {
                boolean success = login();
                if(success) {
                    loggedIn();
                }
            }
            if(choice == 2) {
                newUser();
            }
            if(choice == 3) {
                chat.displayUsers();
            }
            if(choice == 4) {
                System.out.println("Goodbye!");
                mainMenu = false;
                break;
            }
        }
    }
    
    public static void main(String [] args) {
        Main chatApp = new Main();
        chatApp.startChat();
    }
    
}