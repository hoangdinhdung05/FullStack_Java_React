package vn.hoangdung.restAPI.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import org.springframework.lang.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import vn.hoangdung.restAPI.domain.RestResponse;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            @Nullable Object body, // Sử dụng đúng @Nullable của Spring
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        if (!(response instanceof ServletServerHttpResponse)) {
            return body;
        }

        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        if (body instanceof RestResponse) {
            return body;
        }

        if (body instanceof String) {
            return body;
        }

        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(status);

        if (status >= 400) {
            return body;
        } else {
            res.setData(body);
            res.setMessage("CALL API SUCCESS");
            return res;
        }
    }
}
