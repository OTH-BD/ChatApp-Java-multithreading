package net.example.chatappwebsockets.comunicationcilentserver.server;



import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {

    private static final int PORT = 5555;
    private static final Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();
    private static final Set<String> userNames = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {
        System.out.println("Serveur démarré sur le port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleClient(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            // Lire pseudo
            String userName = in.readLine();

            // Vérifier si pseudo dispo
            if (userName == null || userName.isEmpty() || userNames.contains(userName)) {
                out.println("ERROR:Pseudo déjà utilisé ou invalide");
                socket.close();
                return;
            }

            out.println("OK");
            userNames.add(userName);

            ClientHandler client = new ClientHandler(socket, userName, in, out);
            clients.add(client);

            broadcast(">> " + userName + " a rejoint le chat. (" + clients.size() + " en ligne)");

            System.out.println(userName + " connecté");

            client.listen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void broadcast(String message) {
        System.out.println(message);
        for (ClientHandler c : clients) {
            c.sendMessage(message);
        }
    }

    static void removeClient(ClientHandler client) {
        clients.remove(client);
        userNames.remove(client.getUserName());
        broadcast("<< " + client.getUserName() + " a quitté le chat. (" + clients.size() + " en ligne)");
        System.out.println(client.getUserName() + " déconnecté");
    }

    static class ClientHandler {
        private Socket socket;
        private String userName;
        private BufferedReader in;
        private PrintWriter out;

        ClientHandler(Socket socket, String userName, BufferedReader in, PrintWriter out) {
            this.socket = socket;
            this.userName = userName;
            this.in = in;
            this.out = out;
        }

        String getUserName() {
            return userName;
        }

        void sendMessage(String msg) {
            out.println(msg);
        }

        void listen() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    broadcast("[" + userName + "] : " + message);
                }
            } catch (IOException e) {
                // Client déconnecté
            } finally {
                try { socket.close(); } catch (IOException ignored) {}
                removeClient(this);
            }
        }
    }
}
