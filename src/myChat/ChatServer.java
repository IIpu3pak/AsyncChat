package myChat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatServer {

    public static ArrayList<Socket> connectionArray = new ArrayList<Socket>();
    public static ArrayList<String> currentUsers = new ArrayList<String>();

    public static void main(String[] args) throws IOException {

        try {
            final int PORT = 30001;
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Waiting for clients...");

            while (true) {
                Socket socket = server.accept();
                connectionArray.add(socket);
                System.out.println("Client connected from: " + socket.getLocalAddress().getHostName());

                addUserName(socket);
                ChatServerReturn chat = new ChatServerReturn(socket);
                Thread chatRunner = new Thread(chat);
                chatRunner.start();
            }
        } catch (Exception chatRunner) {
            System.out.println(chatRunner);
        }
    }

    public static void addUserName(Socket chatRunner) throws Exception {

        Scanner input = new Scanner(chatRunner.getInputStream());
        String userName = input.nextLine();
        currentUsers.add(userName);
        for (Socket currentConnection : ChatServer.connectionArray) {
            PrintWriter out = new PrintWriter(currentConnection.getOutputStream());
            out.println("#?!" + currentUsers);
            out.flush();
        }
    }
}
