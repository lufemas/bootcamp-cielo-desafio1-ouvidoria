package com.ouvidoria.bootcampcieloouvidoria.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    @CrossOrigin(origins = "*")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hellos %s!", name);
    }
}
