package edu.chat.kirje.controller;

import org.springframework.ui.Model;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OutController {

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

        // Return a response
        return ResponseEntity.ok("OK");
    }
}
