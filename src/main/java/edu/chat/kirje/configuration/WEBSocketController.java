package edu.chat.kirje.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WEBSocketController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebConfiguration.class);
    @Autowired
    private final SimpMessagingTemplate template;

    @Autowired
    public WEBSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendMessage(@NonNull String message) {
        this.template.convertAndSend("/chat/WEBNotify", message);
    }

    // public void notifyWEB() {
    // template.convertAndSend("/chat/WEBNotify", "Notify");
    // }

    // TODO: Create dynamic file addition
    public void sendFiles() {
        try {
            JSONObject payload = new JSONObject();
            JSONObject files = new JSONObject();
            JSONArray array = new JSONArray();
            String encoded = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get("C:\\Users\\vova2\\Desktop\\PArtyCat.jpg")));
            files.put("img", encoded);
            array.put(files);
            // LOGGER.info("Byte Array:\n" + files.getString("img"));
            payload.put("Files", array);
            // payload.put("Text", "Some TEXT");
            LOGGER.info("PAYLOAD[0-100]: " + payload.toString().substring(0, 101));
            this.template.convertAndSend("/chat/APP", payload.toString());
        } catch (IOException e) {
            LOGGER.error("Error Sending data to WEB client:", e);
        }
    }
}
