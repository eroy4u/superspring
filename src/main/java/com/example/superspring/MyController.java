package com.example.superspring;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/hello")
public class MyController {
    @GetMapping(value = "/{name}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String hello(@PathVariable("name") String name){
        return "hello world";
    }
    
}
