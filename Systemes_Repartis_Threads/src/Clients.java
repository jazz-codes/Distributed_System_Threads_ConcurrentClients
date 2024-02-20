import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Clients {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 45678;

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Thread clientThread = new Thread(new ClientThread(i));
            clientThread.start();
        }
    }

    private static class ClientThread implements Runnable {
        private int clientId;

        public ClientThread(int clientId) {
            this.clientId = clientId;
        }

        @Override
        public void run() {
            try {
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String message = "je suis le client " + clientId;
                out.println(message);
                System.out.println("Le client " + clientId + " a envoyé: " + message);

                String reversedMessage = in.readLine();
                System.out.println("le client " + clientId + " a reçu: " + reversedMessage);

                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                System.err.println();
            }
        }
    }
}
