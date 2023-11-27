// package edu.chat.kirje.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.messaging.simp.SimpMessagingTemplate;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.RestController;

// /**
// * InController
// */
// @RestController
// public class InController {
// private final SimpMessagingTemplate template;

// public InController(SimpMessagingTemplate template) {
// this.template = template;
// }

// @RequestMapping(path = "/send-message", method = RequestMethod.POST)
// private void sendMessage(@RequestBody String Message) {
// this.template.convertAndSend("/topic/greeting", Message);
// }
// }