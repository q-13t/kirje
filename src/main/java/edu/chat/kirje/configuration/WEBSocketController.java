package edu.chat.kirje.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WEBSocketController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebConfiguration.class);
    private final SimpMessagingTemplate template;

    public WEBSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendMessage(String message) {
        this.template.convertAndSend("/topic/greetings", message);
    }

    public void sendFiles() {
        try {
            byte[] img = Files.readAllBytes(Paths.get("C:\\Users\\vova2\\Desktop\\ln'jkh.png"));
            String base64Image = Base64.getEncoder().encodeToString(img);
            this.template.convertAndSend("/topic/greetings", base64Image);
        } catch (IOException e) {
            LOGGER.error("Error Sending data to WEB client:", e);
        }
    }
}
