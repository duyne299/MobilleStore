package com.duy.do_an_be.controller;

import com.duy.do_an_be.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {

    private final FileService fileService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, String>> uploadFile(@RequestPart("file") MultipartFile file) {
        String fileUrl = fileService.saveFile(file);
        Map<String, String> response = new HashMap<>();
        response.put("location", fileUrl);
        return ResponseEntity.ok(response);
    }
}
