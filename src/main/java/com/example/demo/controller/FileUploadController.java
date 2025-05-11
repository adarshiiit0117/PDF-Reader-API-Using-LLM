package com.example.demo.controller;

import com.example.demo.service.PdfParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @Autowired
    private PdfParsingService pdfParsingService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPdf(@RequestParam("file") MultipartFile file,
                                       @RequestParam(required = false) String firstname,
                                       @RequestParam(required = false) String dob) throws IOException {
        // Parse the PDF with LLM and fetch the response
        Map<String, String> response = pdfParsingService.parsePdfWithLlm(file, firstname, dob);

        // Return the parsed details as a JSON response
        return ResponseEntity.ok(response);
    }
}
