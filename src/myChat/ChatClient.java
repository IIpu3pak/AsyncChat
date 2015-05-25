package myChat;

import javax.swing.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class ChatClient implements Runnable {

    Socket socket;
    private Scanner input;
    private PrintWriter out;
    Scanner send = new Scanner(System.in);

    public ChatClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            try {
                input = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream());
                out.flush();
                checkStream();
            } finally {
                socket.close();
            }
        } catch (Exception chat) {
            System.out.println(chat);
        }
    }

    public void disconnect() throws Exception {
        out.println(ChatClientGUI.username + " has disconnected.");
        out.flush();
        socket.close();
        JOptionPane.showMessageDialog(null, "You disconnected!");
        System.exit(0);
    }

    public void checkStream() {
        while (true) {
            receive();
        }
    }

    public void receive() {
        if (input.hasNext()) {
            String message = input.nextLine();
            if (message.contains("#?!")) {
                String temp1 = message.substring(3);
                temp1 = temp1.replace("[", "");
                temp1 = temp1.replace("]", "");
                String[] currentUsers = temp1.split(", ");
                ChatClientGUI.onlineList.setListData(currentUsers);
            } else {
                ChatClientGUI.conversationTextArea.append(message + "\n");
            }
        }
    }

    public void send(String x) {
        out.println(ChatClientGUI.username + ": " + x);
        out.flush();
        ChatClientGUI.messageTextField.setText("");
    }
}
