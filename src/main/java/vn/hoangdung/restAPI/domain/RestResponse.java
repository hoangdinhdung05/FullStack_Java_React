package vn.hoangdung.restAPI.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponse<T> {
    
    private int statusCode;
    private String error;
    //message có thể là string hoặc list
    private Object message;
    private T data;

}
