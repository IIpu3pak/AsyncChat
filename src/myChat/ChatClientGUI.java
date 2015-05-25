package myChat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClientGUI {

    private static ChatClient chatClient;
    public static String username = "Anonymous";

    // GUI - Main Window
    public static JFrame MainWindow = new JFrame();
    private static JButton connect = new JButton();
    private static JButton disconnect = new JButton();
    private static JButton send = new JButton();

    private static JLabel messageLabel = new JLabel("Message: ");
    public static JTextField messageTextField = new JTextField(20);

    private static JLabel conversationLabel = new JLabel();
    public static JTextArea conversationTextArea = new JTextArea();
    private static JScrollPane conversationScrollPane = new JScrollPane();

    private static JLabel onlineLabel = new JLabel();
    public static JList onlineList = new JList();
    private static JScrollPane onlineScrollPane = new JScrollPane();

    private static JLabel loggedInAsLabel = new JLabel();
    private static JLabel loggedInAsBoxLabel = new JLabel();

    // GUI - LogIn Window
    public static JFrame LogInWindow = new JFrame();
    public static JTextField userNameBoxTextField = new JTextField(20);
    private static JButton enter = new JButton("ENTER");
    private static JLabel enterUserName = new JLabel("Enter username: ");
    private static JPanel P_Login = new JPanel();

    public static void main(String[] args) {
        buildMainWindow();
        initialize();
    }

    public static void buildMainWindow() {
        MainWindow.setTitle(username + "'s chat box");
        MainWindow.setSize(450, 500);
        MainWindow.setLocation(220, 180);
        MainWindow.setResizable(false);
        configureMainWindow();
        setMainWindowAction();
        MainWindow.setVisible(true);
    }

    public static void configureMainWindow() {

        MainWindow.setBackground(new Color(255, 255, 255));
        MainWindow.setSize(500, 320);
        MainWindow.getContentPane().setLayout(null);

        // for sent button
        send.setBackground(new Color(0, 0, 255));
        send.setForeground(new Color(255, 255, 255));
        send.setText("SEND");
        MainWindow.getContentPane().add(send);
        send.setBounds(250, 40, 81, 25);

        // for disconnect button
        disconnect.setBackground(new Color(0, 0, 255));
        disconnect.setForeground(new Color(255, 255, 255));
        disconnect.setText("DISCONNECT");
        MainWindow.getContentPane().add(disconnect);
        disconnect.setBounds(10, 40, 110, 25);

        // for connect button
        connect.setBackground(new Color(0, 0, 255));
        connect.setForeground(new Color(255, 255, 255));
        connect.setText("CONNECT");
        connect.setToolTipText("");
        MainWindow.getContentPane().add(connect);
        connect.setBounds(130, 40, 110, 25);

        // for message
        messageLabel.setText("Message:");
        MainWindow.getContentPane().add(messageLabel);
        messageLabel.setBounds(10, 10, 60, 20);

        messageTextField.setForeground(new Color(0, 0, 255));
        messageTextField.requestFocus();
        MainWindow.getContentPane().add(messageTextField);
        messageTextField.setBounds(70, 4, 260, 30);

        // for conversation
        conversationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        conversationLabel.setText("Conversation:");
        MainWindow.getContentPane().add(conversationLabel);
        conversationLabel.setBounds(100, 70, 140, 16);

        conversationTextArea.setColumns(20);
        conversationTextArea.setFont(new Font("Tahoma", 0, 12));
        conversationTextArea.setForeground(new Color(0, 0, 255));
        conversationTextArea.setLineWrap(true);
        conversationTextArea.setRows(5);
        conversationTextArea.setEditable(false);

        conversationScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        conversationScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        conversationScrollPane.setViewportView(conversationTextArea);
        MainWindow.getContentPane().add(conversationScrollPane);
        conversationScrollPane.setBounds(10, 90, 330, 180);

        // for online label
        onlineLabel.setHorizontalAlignment(SwingConstants.CENTER);
        onlineLabel.setText("Currently online");
        onlineLabel.setToolTipText("");
        MainWindow.getContentPane().add(onlineLabel);
        onlineLabel.setBounds(350, 70, 130, 16);

        // for online lists
        onlineList.setForeground(new Color(0, 0, 255));

        // for online ScrollPane
        onlineScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        onlineScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        onlineScrollPane.setViewportView(onlineList);
        MainWindow.getContentPane().add(onlineScrollPane);
        onlineScrollPane.setBounds(350, 90, 130, 180);

        // for logged in as
        loggedInAsLabel.setFont(new Font("Tahoma", 0, 12));
        loggedInAsLabel.setText("Currently logged in as");
        MainWindow.getContentPane().add(loggedInAsLabel);
        loggedInAsLabel.setBounds(348, 0, 140, 15);

        // for logged in as box
        loggedInAsBoxLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loggedInAsBoxLabel.setFont(new Font("Tahoma", 0, 12));
        loggedInAsBoxLabel.setForeground(new Color(255, 0, 0));
        loggedInAsBoxLabel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        MainWindow.getContentPane().add(loggedInAsBoxLabel);
        loggedInAsBoxLabel.setBounds(340, 17, 150, 25);

    }

    public static void connect() {
        try {
            final int PORT = 30001;
            final String HOST = "127.0.0.1";
            Socket socket = new Socket(HOST, PORT);
            System.out.println("You connected to: " + HOST);

            chatClient = new ChatClient(socket);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println(username);
            out.flush();


            Thread client = new Thread(chatClient);
            client.start();

        } catch (Exception client) {
            System.out.println(client);
            JOptionPane.showMessageDialog(null, "Server not responding. ");
            System.exit(0);
        }
    }

    public static void initialize() {
        send.setEnabled(false);
        disconnect.setEnabled(false);
        connect.setEnabled(true);
    }

    public static void login_Action() {
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionEnter();
            }
        });
    }

    public static void actionEnter() {
        if (!userNameBoxTextField.getText().equals("")) {
            username = userNameBoxTextField.getText().trim();
            loggedInAsBoxLabel.setText(username);
            ChatServer.currentUsers.add(username);
            MainWindow.setTitle(username + "'s Chat Box");
            LogInWindow.setVisible(false);
            send.setEnabled(true);
            disconnect.setEnabled(true);
            connect.setEnabled(false);
            connect();
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a name!");
        }
    }

    public static void buildLogInWindow() {
        LogInWindow.setTitle("What's your name?");
        LogInWindow.setSize(400, 100);
        LogInWindow.setLocation(250, 200);
        LogInWindow.setResizable(false);
        P_Login = new JPanel();
        P_Login.add(enterUserName);
        P_Login.add(userNameBoxTextField);
        P_Login.add(enter);
        LogInWindow.add(P_Login);

        login_Action();
        LogInWindow.setVisible(true);

    }

    public static void setMainWindowAction() {
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionSend();
            }
        });
        disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionDisconnect();
            }
        });

        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buildLogInWindow();
            }
        });
    }

    public static void actionSend() {
        if (!messageTextField.getText().equals("")) {
            System.out.println("SendMessage = "+messageTextField.getText());
            chatClient.send(messageTextField.getText());
            System.out.println("SendMessage = "+messageTextField.getText());
            messageTextField.requestFocus();

        }
    }

    public static void actionDisconnect() {
        try {
            chatClient.disconnect();
        } catch (Exception Y) {
            Y.printStackTrace();
        }
    }
}
