package edu.chat.kirje.configuration;

import java.net.SocketException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import java.nio.ByteBuffer;

import java.util.Random;
import java.util.Collections;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.handshake.ClientHandshake;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import edu.chat.kirje.controller.OutController;

@Component
public class WEBSocketController extends WebSocketServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(WEBSocketController.class);
    private static WEBSocketController WEBSC;

    public static String getServerData() {
        return WEBSC.getAddress().getAddress().getHostAddress() + ":" + WEBSC.getPort();
    }

    public WEBSocketController() throws UnknownHostException, SocketException {
        WEBSC = new WEBSocketController(new InetSocketAddress(OutController.getInetAddresses(), new Random().nextInt(4_000, 10_000)));
        WEBSC.start();
        LOGGER.info("ChatServer started on port: " + WEBSC.getAddress().getAddress().getHostAddress() + ":" + WEBSC.getPort());
    }

    public WEBSocketController(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public WEBSocketController(InetSocketAddress address) {
        super(address);
    }

    public WEBSocketController(int port, Draft_6455 draft) {
        super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("Connected to server: " + getServerData()); // This method sends a message to the new client
        WEBSC.getConnections().forEach(connection -> {
            if (!connection.equals(conn)) {
                connection.send("new connection: " + conn.getRemoteSocketAddress());
            }
        });
        LOGGER.info(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        broadcast(conn + " has left the room!");
        if (WEBSC.getConnections().size() == 1) {
            WEBSC.getConnections().forEach(x -> {
                x.send("Last Connection");
            });
        }
        LOGGER.info(conn.getRemoteSocketAddress() + " has left the room!");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        WEBSC.getConnections().forEach(connection -> {
            if (!connection.equals(conn)) {
                connection.send(message);
            }
        });
        LOGGER.info("Client: " + conn + " send message.");
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        WEBSC.getConnections().forEach(connection -> {
            if (!connection.equals(conn)) {
                connection.send(message.array());
            }
        });
        LOGGER.info("Client: " + conn + " send message.");
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        LOGGER.error("Error with WebSocket", ex);
        if (conn != null) {
        }
    }

    @Override
    public void onStart() {
        LOGGER.info("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

}