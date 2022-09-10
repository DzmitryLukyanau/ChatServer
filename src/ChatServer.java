import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatServer extends Thread {
    private final ArrayList<Client> clients = new ArrayList<>();
    private ServerSocket serverSocket;

    public ChatServer() {
        try {
            serverSocket = new ServerSocket(1234);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendMessage(String message) {
        for (Client client : clients) {
            client.reciveMessage(message);
        }
    }

    public void runServer() {
        while (true) {
            System.out.println("Waiting...");
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");
                // создаем клиента на своей стороне
                clients.add(new Client(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ChatServer chat = new ChatServer();
        chat.runServer();
    }

    class Client implements Runnable {
        private final Socket socket;
        private Scanner in;
        private PrintStream out;

        public Client(Socket socket) {
            this.socket = socket;
            new Thread(this).start();
        }

        void reciveMessage(String message) {
            out.println(message);
        }

        public void run() {
            try {
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                in = new Scanner(is);
                out = new PrintStream(os);
                out.println("Welcome to chat.");
                String input = in.nextLine();
                while (!input.equals("bye")) {
                    ChatServer.this.sendMessage(input);
                    input = in.nextLine();
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}




