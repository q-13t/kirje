package edu.chat.kirje.controller;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.chat.kirje.configuration.WEBSocketController;

@Controller
public class OutController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OutController.class);
    private static String SERVER_DATA = null;
    @Autowired
    WEBSocketController con;

    public OutController() throws SocketException {
        SERVER_DATA = WEBSocketController.getServerData();
    }

    public static InetAddress getInetAddresses() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (!networkInterface.isUp() || !networkInterface.getDisplayName().contains("Wireless LAN") || !networkInterface.getInetAddresses().nextElement().getHostAddress().contains(".")) {
                continue;
            } else {
                return networkInterface.getInetAddresses().nextElement();
            }
        }
        return null;
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String testMapping(Model model) {
        model.addAttribute("server_data", SERVER_DATA);
        LOGGER.info("Sending Server Data: " + SERVER_DATA);
        return "/pages/index.html";
    }

}
