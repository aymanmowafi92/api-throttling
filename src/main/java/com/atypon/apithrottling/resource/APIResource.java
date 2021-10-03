package com.atypon.apithrottling.resource;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-throttling")
public class APIResource {

    @GetMapping("/service1")
    public ResponseEntity<String> service1 (@PathVariable("throttling") Optional<Long> id) {
        return new ResponseEntity<>("Service One", HttpStatus.OK);
    }

    @GetMapping("/service2")
    public ResponseEntity<String> service2 (@PathVariable("throttling") Optional<Long> id) {
        return new ResponseEntity<>("Service Two", HttpStatus.OK);
    }

    @GetMapping("/service3")
    public ResponseEntity<String> service3 (@PathVariable("throttling") Optional<Long> id) {
        return new ResponseEntity<>("Service Three", HttpStatus.OK);
    }
}
