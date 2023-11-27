package edu.chat.kirje.controller;

import org.springframework.boot.context.properties.source.ConfigurationPropertyName.Form;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class OutController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String testMapping(Model model) {
        return "/pages/index.html";
    }

    @RequestMapping(path = "/api/submit", method = RequestMethod.POST)
    public ResponseEntity<String> handleFormData(@RequestPart("fileInput") MultipartFile file,
            @RequestPart("textAreaInput") String textAreaInput) {
        // Process the file and text data as needed
        System.out.println("Received file: " + file.getOriginalFilename());
        System.out.println("Received text: " + textAreaInput);

        // Return a response
        return ResponseEntity.ok(textAreaInput);
    }
}
