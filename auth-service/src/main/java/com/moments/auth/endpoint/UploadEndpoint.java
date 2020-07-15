package com.moments.auth.endpoint;


import com.moments.auth.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/upload")
public class UploadEndpoint {
    @Autowired
    private StorageService storageService;

    @PostMapping(value = "/file")
    public ResponseEntity handleUploads(@RequestParam("file") MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String[] split = originalFilename.split("\\.");
            if (!"jpg".equals(split[1])) {
                return ResponseEntity.badRequest().body("格式不对！");
            }
            storageService.saveFile(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.ok("upload succeed!");
    }
}