package edu.chat.kirje.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * InController
 */
@Controller
public class InController extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(InController.class);

    // This value set as true in my properties file. Just for test. actually you
    // don't need this.
    // @Value("${server.supportsPartialMessages'}")
    private boolean supportsPartialMessages = true;

    // If you need to handle partial message, should return true.
    @Override
    public boolean supportsPartialMessages() {
        return supportsPartialMessages;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        // I think this is easier to keep each message and each client.
        session.getAttributes().put("messageRoom", new StringBuilder(session.getTextMessageSizeLimit()));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        String Payload = message.getPayload();
        StringBuilder sbTemp = (StringBuilder) session.getAttributes().get("messageRoom");

        // isLast() will tell you this is last chunk or not.
        if (message.isLast() == false) {
            sbTemp.append(Payload);
        } else {
            if (sbTemp.length() != 0) {
                sbTemp.append(Payload);

                LOGGER.info(session.getRemoteAddress() + ":RECEIVE_TO[CLIENT][PARTIAL][" + sbTemp.length() + "]:" + sbTemp.toString());
                doYourWork(session, sbTemp.toString());
                // Release memory would be nice.
                sbTemp.setLength(0);
                sbTemp.trimToSize();

            } else {
                LOGGER.info(session.getRemoteAddress() + ":RECEIVE_TO[CLIENT][WHOLE]:" + Payload);
                doYourWork(session, Payload);
            }
        }
    }

    private void doYourWork(WebSocketSession session, String string) {
        this.stringMessage(new GenericMessage<String>(string));
    }

    @MessageMapping("/message")
    @SendTo("/chat/WEBNotify")
    public Message<String> stringMessage(Message<String> message) {
        LOGGER.info("Received Message Of Length: " + message.getPayload().length());
        return message;
    }
}