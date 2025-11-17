import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class FileTransferClient {

    private static final int SERVER_PORT = 1238;

    private static final int BUFFER_SIZE = 2000;

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Usage: java FileTransferClient <server_name> <file_name>");
            return;
        }

        String serverName = args[0]; 
        String fileName = args[1];   

        try (
            Socket socket = new Socket(serverName, SERVER_PORT);

            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
                    true);

            InputStream in = socket.getInputStream();

            DataOutputStream stdOut = new DataOutputStream(
                    new BufferedOutputStream(System.out))
        ) {
            out.println(fileName);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                stdOut.write(buffer, 0, bytesRead);
            }

            stdOut.flush();

        } catch (UnknownHostException e) {
            System.err.println("Error: Unknown host " + serverName);
        } catch (IOException e) {
            System.err.println("Error connecting to server or reading/writing data: " + e.getMessage());
        }
    }
}
