package vn.hoangdung.restAPI.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hoangdung.restAPI.service.error.IdInvalidException;

@RestController
public class HelloController {

    @SuppressWarnings("unused")
    @GetMapping("/")
    public String getHello() throws Exception {
        if(true) {
            throw new IdInvalidException("Error");
        }
        return "Hello";
    }

}
