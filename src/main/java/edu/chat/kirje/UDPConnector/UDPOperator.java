package edu.chat.kirje.UDPConnector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class UDPOperator extends Thread {
    private DatagramChannel datagram;
    private SocketAddress originalClient;
    private SocketAddress client;
    private final int PORT;
    private boolean serverActive = true;

    public static boolean startServer() {
        try {
            new UDPOperator();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public SocketAddress gSocketAddress() {
        try {
            return datagram.getLocalAddress();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    UDPOperator() throws IOException {
        PORT = new Random().nextInt(4000, 10001);
        InetSocketAddress address = new InetSocketAddress("localhost", 8085);
        datagram = DatagramChannel.open();
        datagram.bind(address);
        System.out.println("UDP Datagram Started");
        System.out.println("UPD Is Located At: " + address);
        listenForClient();
    }

    private void listenForClient() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            originalClient = datagram.receive(buffer);
            buffer.flip();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            String msg = new String(bytes);
            System.out.println("Original Client: " + originalClient + " | Message: " + msg);
            System.out.println("Connection Established Starting Communication Channel.");
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean sendMessage(String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        assert client != null;
        datagram.send(buffer, client);
        return true;
    }

    @Override
    public void run() {
        try {
            while (serverActive) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                client = datagram.receive(buffer);
                assert client == originalClient;
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                String msg = new String(bytes);
                System.out.println("Client: " + client + " | Message: " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
