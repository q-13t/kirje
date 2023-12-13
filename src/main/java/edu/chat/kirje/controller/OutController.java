package edu.chat.kirje.controller;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class OutController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OutController.class);
    private static String SERVER_DATA = null;

    public OutController() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (!networkInterface.isUp() || !networkInterface.getDisplayName().contains("Wireless LAN")
                    || !networkInterface.getInetAddresses().nextElement().getHostAddress().contains(".")) {
                continue;
            } else {
                SERVER_DATA = networkInterface.getInetAddresses().nextElement().getHostAddress()
                        + ":8080";
            }
        }
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String testMapping(Model model) {
        model.addAttribute("server_data", SERVER_DATA);
        LOGGER.info("Sending Server Data: " + SERVER_DATA);
        return "/pages/index.html";
    }

    @RequestMapping(path = "/api/submit", method = RequestMethod.POST)
    public ResponseEntity<String> handleFormData(
            @RequestPart(name = "fileInput", required = false) List<MultipartFile> files,
            @RequestPart(name = "textAreaInput", required = false) String textAreaInput) {
        // Process the file and text data as needed
        for (int index = 0; index < files.size(); index++) {
            System.out.println("Received file: " + files.get(index).getOriginalFilename());
        }
        System.out.println("Received text: " + textAreaInput);
        // ###################################################
        // new Thread(() -> {
        // try {
        // Thread.sleep(5000);
        // // socketOperator.sendMessage("SECRETS");
        // socketOperator.sendFiles();
        // System.out.println("HERE");
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // }).start();
        // ###################################################
        return ResponseEntity.ok("KO");
    }

}
