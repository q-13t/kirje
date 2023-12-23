package edu.chat.kirje.configuration;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

// @Component
// public class TextHandler extends TextWebSocketHandler {
//     private static final Logger LOGGER = LoggerFactory.getLogger(TextHandler.class.getSimpleName());

//     @Override
//     public boolean supportsPartialMessages() {
//         LOGGER.info("Server Support Partial Messaging: " + true);
//         return true;
//     }

//     @Override
//     public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//         super.afterConnectionEstablished(session);
//         session.getAttributes().put("messageRoom", new StringBuilder(session.getTextMessageSizeLimit()));
//     }

//     @Override
//     protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//         super.handleTextMessage(session, message);
//         StringBuilder sbTemp = (StringBuilder) session.getAttributes().get("messageRoom");
//         // isLast() will tell you this is last chunk or not.
//         String Payload = message.getPayload();
//         if (message.isLast() == false) {
//             sbTemp.append(Payload);

//         } else {
//             if (sbTemp.length() != 0) {
//                 sbTemp.append(Payload);

//                 LOGGER.info(session.getRemoteAddress() + ":RECEIVE_TO[CLIENT][PARTIAL][" + sbTemp.length() + "]:"
//                         + sbTemp.toString());
//                 // doYourWork(session, sbTemp.toString());
//                 // Release memory would be nice.
//                 sbTemp.setLength(0);
//                 sbTemp.trimToSize();

//             } else {
//                 LOGGER.info(session.getRemoteAddress() + ":RECEIVE_TO[CLIENT][WHOLE]:" + message.getPayload());
//                 // doYourWork(session, Payload);
//             }
//         }

//     }
// }
@Component
public class SocketHandler extends TextWebSocketHandler {

    List sessions = new CopyOnWriteArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {

        System.out.println(message.getPayload());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // the messages will be broadcasted to all users.
        sessions.add(session);
    }
}