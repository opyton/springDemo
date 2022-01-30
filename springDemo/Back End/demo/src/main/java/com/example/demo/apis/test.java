package com.example.demo.apis;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class test {
    @GetMapping("/test")
    public ResponseEntity<String> test(){
        JSONObject obj = new JSONObject();
        obj.put("Message","Hello World!");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(obj.toString());
    }
}
