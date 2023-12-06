package edu.chat.kirje.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import edu.chat.kirje.UDPConnector.UDPOperator;

@Controller
public class OutController {

    @Autowired
    private UDPOperator operator;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String testMapping(Model model) {
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
        try {
            operator.sendMessage(textAreaInput);
            return ResponseEntity.ok("OK");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.ok("NOT OK");
        }
    }
}
