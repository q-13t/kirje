package edu.chat.kirje.UDPConnector;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private Socket clientSocket = null;
    private boolean connected;
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    public ClientHandler(Socket client) {
        try {
            clientSocket = client;
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        dataInputStream.close();
        dataOutputStream.close();
    }

    @Override
    public void run() {
        connected = true;
        try {
            while (connected) {
                BufferedReader br = new BufferedReader(new InputStreamReader(dataInputStream));
                String line = "";
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                connected = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
