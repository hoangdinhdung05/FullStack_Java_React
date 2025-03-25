package vn.hoangdung.restAPI.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.hoangdung.restAPI.domain.RestResponse;

@RestControllerAdvice
public class GlobalException {
        
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponse<Object>> handleIdException(IdInvalidException ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("INVALID_ID");
        res.setMessage(ex.getMessage());  // Giữ nguyên thông báo lỗi chi tiết
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
