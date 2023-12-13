package edu.chat.kirje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import edu.chat.kirje.configuration.WEBSocketController;

/**
 * InController
 */
@Controller
public class InController {

    @Autowired
    private WEBSocketController socketController;

    @MessageMapping("/chat/notify")
    private void sendMessage(@RequestBody String Message) {
        socketController.sendMessage("Message");
    }
}