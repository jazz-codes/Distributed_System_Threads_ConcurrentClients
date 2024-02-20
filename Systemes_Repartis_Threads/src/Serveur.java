import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Serveur {
    private static final int PORT = 45678;
    private static final int MAX_CLIENTS = 10;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        //pool de threads (ExecutorService) pour gérer les demandes des clients entrants
        // MAX_CLIENTS (10) threads peuvent être exécutés simultanément
        ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Serveur en écoute sur le port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouveau client connecté sur le port: " + clientSocket.getPort());

                TraitementClient traitementClient = new TraitementClient(clientSocket);
                executor.execute(traitementClient);
            }
        } catch (IOException e) {
            System.err.println();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.err.println();
                }
            }
            executor.shutdown();
        }
    }

    private static class TraitementClient implements Runnable {
        private Socket clientSocket;

        public TraitementClient(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    System.out.println("Reçu du client: " + inputLine);
                    String reversedString = reverseString(inputLine);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        System.err.println();
                    }
                    writer.println(reversedString);
                }

                reader.close();
                writer.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println();
            }
        }

        private String reverseString(String str) {
            return new StringBuilder(str).reverse().toString();
        }
    }
}
