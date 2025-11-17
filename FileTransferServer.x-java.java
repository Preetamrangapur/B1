
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class FileTransferServer {

    private static final int SERVER_PORT = 1238;

    private static final int BUFFER_SIZE = 2000;

    public static void main(String[] args) {
        System.out.println("Starting TCP File Transfer Server...");

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started and listening on port " + SERVER_PORT + "...");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("\nClient connected from: " + clientSocket.getInetAddress().getHostAddress());

                new Thread(() -> handleClient(clientSocket)).start();
            }

        } catch (IOException e) {
            System.err.println("Could not start server: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
            OutputStream socketOut = clientSocket.getOutputStream();
            BufferedReader socketIn = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8))
        ) {
            String fileName = socketIn.readLine();
            if (fileName == null || fileName.trim().isEmpty()) {
                System.out.println("Client disconnected before sending filename.");
                return;
            }

            fileName = fileName.trim();
            System.out.println("Requested file: " + fileName);

            File file = new File(fileName);

            if (!file.exists() || file.isDirectory()) {
                String errorMessage = "ERROR: File does not exist\n";
                System.out.println(errorMessage.trim());
                socketOut.write(errorMessage.getBytes(StandardCharsets.UTF_8));
                socketOut.flush();
                return;
            }

            System.out.println("File found. Sending contents...");

            try (FileInputStream fileIn = new FileInputStream(file)) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;

                while ((bytesRead = fileIn.read(buffer)) != -1) {
                    socketOut.write(buffer, 0, bytesRead);
                }

                socketOut.flush();
                System.out.println("File sent successfully.");
            }

        } catch (IOException e) {
            System.err.println("Error processing client request: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignore) {}
            System.out.println("Client connection closed.");
        }
    }
}
