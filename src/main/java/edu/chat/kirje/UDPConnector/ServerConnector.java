package edu.chat.kirje.UDPConnector;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.WebSocket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ServerConnector extends Thread {
    private static ExecutorService executor = Executors.newCachedThreadPool();
    private static Logger logger = LoggerFactory.getLogger(ServerConnector.class);
    private final int PORT;
    private ServerSocket serverSocket;
    private int MAX_CONNECTIONS = 10;
    private boolean serverAlive = true;

    ServerConnector() {
        PORT = new Random().nextInt(4_000, 10_000);
        try {
            serverSocket = new ServerSocket(PORT, MAX_CONNECTIONS, InetAddress.getByName("localhost"));
            logger.info("Server Started at IP: " + serverSocket.getInetAddress() + " PORT: " + serverSocket.getLocalPort());
            executor.submit(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Socket client;
            while (serverAlive) {
                if ((client = serverSocket.accept()) != null) {
                    logger.info("Client: " + client.getInetAddress() + " connected");
                    executor.submit(new ClientHandler(client));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getServerData() {
        if (!serverSocket.isClosed())
            return serverSocket.getInetAddress().getHostAddress() + serverSocket.getLocalPort();// + ":8080";
        else
            return null;
    }
}
