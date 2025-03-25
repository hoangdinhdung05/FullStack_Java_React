package vn.hoangdung.restAPI.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hoangdung.restAPI.service.error.IdInvalidException;

@RestController
public class HelloController {
    
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<String> handleIdException(IdInvalidException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @SuppressWarnings("unused")
    @GetMapping("/")
    public String getHello() throws Exception {
        if(true) {
            throw new IdInvalidException("Error");
        }
        return "Hello";
    }

}
