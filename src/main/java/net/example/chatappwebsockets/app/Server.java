package net.example.chatappwebsockets.app;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(9092);
        Socket s = ss.accept();
        InputStream in = s.getInputStream();
        OutputStream out = s.getOutputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(out),true);
        Scanner sc = new Scanner(System.in);
        String msg;
        do {
            msg = br.readLine();
            System.out.println("Lui : "+msg);
            System.out.print("Moi : ");
            msg = sc.nextLine();
            pw.println(msg);
        }while ( !msg.equals("Bye"));
    }

}