package net.example.chatappwebsockets.app;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args){
        try {
            Socket socket = new Socket("localhost", 9091);
            System.out.println("connected to server");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            String clientName = in.readLine();
            new Thread(()->{
                String serverMsg;
                try {
                    while ((serverMsg = in.readLine()) != null) {
                        System.out.println(serverMsg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            while (true){
                System.out.print(clientName+": ");
                String msg = scanner.nextLine();
                out.println(msg);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
