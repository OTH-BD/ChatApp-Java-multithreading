package net.example.chatappwebsockets.app;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerMultiThread {
    private static final int PORT = 9093;
    public static List<SocketThread> clients = new ArrayList<>();
    public static void main(String[] args){
        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Server is running on port "+PORT);
            while (true){
                Socket clientSocket = serverSocket.accept();
                SocketThread clientThread = new SocketThread(clientSocket,clients,"");
                clients.add(clientThread);
                clientThread.start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }
    public static void broadcastAll(String message) {
        for (SocketThread client : clients) {
            client.sendSystemMessage(message);
        }
    }

}