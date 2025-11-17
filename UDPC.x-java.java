import java.io.*;
import java.net.*;

public class UDPC {
    public static void main(String[] args) { 
        DatagramSocket socket = null; 
 
        try { 
            socket = new DatagramSocket(); 
            InetAddress host = InetAddress.getByName("127.0.0.1"); 
            int serverPort = 6788; 
 
            String msg = "Hello Server"; 
            byte[] sendData = msg.getBytes(); 
 
            DatagramPacket request = new DatagramPacket(sendData, sendData.length, host, serverPort); 
            socket.send(request); 
            System.out.println("Message sent to server: " + msg); 
 
            byte[] buffer = new byte[1024]; 
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length); 
            socket.receive(reply); 
 
            String received = new String(reply.getData(), 0, reply.getLength()); 
            System.out.println("Client received: " + received); 
 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            if (socket != null) 
                socket.close(); 
        } 
    }
}