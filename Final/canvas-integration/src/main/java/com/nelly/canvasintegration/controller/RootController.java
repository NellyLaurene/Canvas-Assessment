package com.nelly.canvasintegration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RootController {

    @GetMapping
    public ResponseEntity<String> handleRootRequest() {
        return ResponseEntity.ok("Backend is running. Use /api endpoints.");
    }

}
