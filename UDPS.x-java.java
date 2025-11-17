import java.net.*; 
 
public class UDPS { 
    public static void main(String[] args) { 
        DatagramSocket socket = null; 
 
        try { 
            socket = new DatagramSocket(6788); 
            System.out.println("Server started. Waiting for client messages..."); 
 
            byte[] buffer = new byte[1024]; 
 
            while (true) { 
                DatagramPacket request = new DatagramPacket(buffer, buffer.length); 
                socket.receive(request); 
 
                String message = new String(request.getData(), 0, request.getLength()); 
                System.out.println("Received from client: " + message); 
 
                String replyMessage = message + " (server processed)"; 
                byte[] sendMsg = replyMessage.getBytes(); 
 
                DatagramPacket reply = new DatagramPacket( 
                        sendMsg, 
                        sendMsg.length, 
                        request.getAddress(), 
                        request.getPort() 
                ); 
 
                socket.send(reply); 
                System.out.println("Reply sent to client."); 
            } 
 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            if (socket != null) 
                socket.close(); 
        } 
    } 
} 