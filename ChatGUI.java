import javax.swing.JFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import javax.swing.*;
import java.util.*;
//lets grab a a JList.

public class ChatGUI extends JFrame {
    JButton login, newUser, send, joinGroup, newGroup;
    JComboBox<ImageIcon> shapesComboBox, pathComboBox;
    JTextField mainTitle, username;
    //ArrayList<Song> library;
    JList libraryGUI;
    JScrollPane menu;
    JTextArea groupsList;
    JTextArea messages;
    JList<String> messagesList;
    JScrollPane messagesScrollPane;
    
    
    Conversation conv;
    Chat chat;
    User currentUser;
    
    class DisplayThread implements Runnable 
    { 
        public void run() 
        { 
            try
            { 
                while(true) {
                  conv.loadConversation();
                  updateList();
                  Thread.sleep(1000);  
                } 
            } 
            catch (Exception e) 
            { 
                System.err.println(e);
                System.out.println ("Exception is caught"); 
            } 
        } 
    }

    public static void main(String[] args) {
        ChatGUI newFrame = new ChatGUI("Chat GUI");
		// ask it to become visible
		newFrame.setVisible(true);

    }
    
    public void updateList() {
        ListModel mod = messagesList.getModel();
        DefaultListModel model = new DefaultListModel();
        model.clear();
        for(Message m : conv.convo) {
            model.addElement(m.toString());
        }
        messagesList.setModel(model);
    }
    
    public void setup() {
        chat.loadUsers();
        chat.loadGroups();
    }
    
    public boolean inGroup(Group g) {
        for(User u : g.users) {
            if(u.username.equals(currentUser.username)) {
                return true;
            }
        }
        return false;
    }
    
    public void login() {
        String name = username.getText();
        currentUser = chat.findUser(name);
        if(currentUser == null) {
            JOptionPane.showMessageDialog(null, "User does not exist.");
        }
        else {
            welcome();
        }
    }
    
    public void newUser() {
        String name = username.getText();;
        currentUser = new User(name);
        chat.users.add(currentUser);
        chat.writeUsers();
        JOptionPane.showMessageDialog(null, "Registered.");
        welcome();
    }
    
    public void newgroup() {
        String name = JOptionPane.showInputDialog("Please enter the group id: ");
        Group g = new Group(name);
        chat.groups.add(g);
        chat.writeGroups();
        JOptionPane.showMessageDialog(null, "New group added.");
    }
    
    public void converse() {
        
        
        
        JFrame converse = new JFrame("Conversation");
        
        GridBagLayout grid = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
        converse.setLayout(grid);
        
        
        conv.loadConversation();
        
        
        String [] convoArr = new String[conv.convo.size()];
        for(int i = 0; i < convoArr.length; i++) {
            convoArr[i] = conv.convo.get(i).toString();;
        }
        
        messagesList = new JList<String>(convoArr);
        messagesScrollPane = new JScrollPane(messagesList);
        
        messagesScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
        public void adjustmentValueChanged(AdjustmentEvent e) {  
            e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
        }
        });

        
        JTextField sendM = new JTextField(20);
        
        JButton send = new JButton("send");
        send.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Message newm = new Message(sendM.getText(), currentUser.username);
                conv.addMessage(newm);
                conv.recordConversation();
            }
        });
        
        DisplayThread dt = new DisplayThread();
        Thread object1 = new Thread(dt);
        
        JButton leave = new JButton("leave");
        leave.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                object1.stop();
                converse.setVisible(false);
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        converse.add(messagesScrollPane, gbc);
        
        gbc.gridy = 1;
        converse.add(sendM, gbc);
        gbc.gridx = 1;
        converse.add(send, gbc);
        gbc.gridy = 2;
        converse.add(leave, gbc);
        
        
        converse.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        converse.setSize(800, 500);
		converse.setLayout(grid);
        
        converse.setVisible(true);
        
        
        object1.start();
    }
    
    public void welcome() {
        JFrame loggedinFrame = new JFrame("Loggedin");
        
        GridBagLayout grid = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
        loggedinFrame.setLayout(grid);
        JLabel welcomeField = new JLabel("Welcome " + currentUser.username + "!");
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        loggedinFrame.add(welcomeField, gbc);
        
        
        
        JButton newgroup = new JButton("new group");
        newgroup.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newgroup();
            }
        });
        
        
        JButton joingroup = new JButton("join group");
        joingroup.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setup();
                String groupID= JOptionPane.showInputDialog("Please enter the group id: ");
                Group foundGroup = chat.findGroup(groupID);
                if(foundGroup == null) {
                    JOptionPane.showMessageDialog(null, "Group does not exist.");
                }
                else {
                    foundGroup.users.add(currentUser);
                    String groups = "Your groups\n===\n";
                    for(Group g : chat.groups) {
                        if(inGroup(g)) {
                            groups = groups + g + "\n";
                        } 
                    }
                    groupsList.setText(groups);
                }
            }
        });
        
        
        
        JButton enterchat = new JButton("enter chat");
        enterchat.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String groupID= JOptionPane.showInputDialog("Please enter the group id: ");
                Group foundGroup = chat.findGroup(groupID);
                if(foundGroup == null) {
                    JOptionPane.showMessageDialog(null, "Group does not exist.");
                }
                else if(!inGroup(foundGroup)){
                    JOptionPane.showMessageDialog(null, "Group does not exist.");
                }
                else {
                    conv = new Conversation(foundGroup);
                    converse();
                }
            }
        });
        
        
        
        JButton logout = new JButton("logout");
        logout.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loggedinFrame.setVisible(false);
            }
        });
        
        String groups = "Your groups\n===\n";
        
        for(Group g : chat.groups) {
            if(inGroup(g)) {
                groups = groups + g + "\n";
            } 
        }
        
        groupsList = new JTextArea(groups);
        groupsList.setEditable(false);
        gbc.gridy = 1;
        loggedinFrame.add(groupsList, gbc);
        gbc.gridy = 2;
        gbc.gridx = 0;
        loggedinFrame.add(newgroup, gbc);
        gbc.gridx = 1;
        loggedinFrame.add(joingroup, gbc);
        gbc.gridx = 2;
        loggedinFrame.add(enterchat, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        loggedinFrame.add(logout, gbc);
        loggedinFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loggedinFrame.setSize(800, 500);
		loggedinFrame.setLayout(grid);
        
        loggedinFrame.setVisible(true);
    }

   
    public ChatGUI(String title) {
        super(title);
        chat = new Chat();
        currentUser = null;
        setup();
        
        GridBagLayout grid = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		setLayout(grid);
        mainTitle = new JTextField("username");
        mainTitle.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        username = new JTextField(10);
        username.setEditable(true);
        add(mainTitle, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(username, gbc);
        login = new JButton("login");
        login.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        newUser = new JButton("create user");
        newUser.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newUser();
            }
        });
        
        
        gbc.gridy = 2;
        add(login, gbc);
        gbc.gridy = 3;
        add(newUser, gbc);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
		setLayout(grid);
        
        
    }


}