package vn.hoangdung.restAPI.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoangdung.restAPI.util.error.IdInvalidException;


@RestController
public class HelloController {

    @GetMapping("/")
    // @CrossOrigin
    public String getHello() throws IdInvalidException {
        return "Hello";
    }

}
