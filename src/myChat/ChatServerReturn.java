package myChat;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatServerReturn implements Runnable {

    Socket socket;
    private Scanner input;
    private PrintWriter out;
    String message = "";

    public ChatServerReturn(Socket chatRunnable) {
        this.socket = chatRunnable;
    }

    public void checkConnection() throws Exception {
        if (!socket.isConnected()) {
            for (int i = 1; i < ChatServer.connectionArray.size(); i++) {
                if (ChatServer.connectionArray.get(i) == socket) {
                    ChatServer.connectionArray.remove(i);
                }
            }
            for (Socket currentConnection : ChatServer.connectionArray) {
                PrintWriter tmpOut = new PrintWriter(currentConnection.getOutputStream());
                tmpOut.println(currentConnection.getLocalAddress().getHostName() + "disconnected!");
                tmpOut.flush();
                System.out.println(currentConnection.getLocalAddress().getHostName() + "disconnected!");
            }
        }
    }

    @Override
    public void run() {

        try {
            try {
                input = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream());

                while (true) {
                    checkConnection();
                    if (!input.hasNext()) {
                        return;
                    }

                    message = input.nextLine();
                    System.out.println("Client said:" + message);

                    for (Socket currentConnection : ChatServer.connectionArray) {
                        PrintWriter tmpOut = new PrintWriter(new OutputStreamWriter(currentConnection.getOutputStream()));
                        tmpOut.println(message);
                        tmpOut.flush();
                        System.out.println("Send to:" + currentConnection.getLocalAddress().getHostName());
                    }
                }
            } finally {
                socket.close();
            }
        } catch (Exception chat) {
            System.out.println(chat);
        }

    }
}

