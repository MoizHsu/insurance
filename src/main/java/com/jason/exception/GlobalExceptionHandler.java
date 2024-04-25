package com.jason.exception;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.jason.model.response.ErrorHttpResponseDTO;
import com.jason.model.response.ResponseCode;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.nio.charset.Charset;


/**
 * globally handle exception
 *
 * @author jason
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * self defined exception (known)
     */
    @ExceptionHandler(RestfulException.class)
    @ResponseBody
    public ResponseEntity<ErrorHttpResponseDTO> handleException(RestfulException e, HttpServletRequest request) {
        printFormattedError("RestfulException", e, request);
        ResponseCode resCode = e.getResponseCode();
        String badMsg
                = handleExceptionMessageWithParams(resCode.get(), e.getResponseCodeParams());

        ErrorHttpResponseDTO errorMsg = ErrorHttpResponseDTO.builder().status(resCode.get()).error(badMsg).build();
        var response = ResponseEntity.badRequest();
        return response.body(errorMsg);
    }

    /**
     * self defined exception (JPA)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<ErrorHttpResponseDTO> handleException(ConstraintViolationException e, HttpServletRequest request) {
        printFormattedError("ConstraintViolationException", e, request);
        
        String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());
        ResponseCode resCode = ResponseCode.DATA_INTEGRITY_ERROR;
        ErrorHttpResponseDTO errorMsg = ErrorHttpResponseDTO.builder().status(resCode.get()).error(message).build();
        var response = ResponseEntity.badRequest();
        return response.body(errorMsg);
    }

    /**
     * handle all other exceptions (unknown)
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorHttpResponseDTO> handleException(Exception e, HttpServletRequest request) {
        printFormattedError("Exception", e, request);

        ResponseCode resCode = ResponseCode.DATA_INTEGRITY_ERROR;
        ErrorHttpResponseDTO errorMsg = ErrorHttpResponseDTO.builder().status(resCode.get()).error(e.getMessage()).build();
        var response = ResponseEntity.badRequest();
        return response.body(errorMsg);
    }

    private void printFormattedError(String msg, Exception e, ServletRequest request) {
        StringBuilder requestInfo = new StringBuilder();
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
            requestInfo.append(",request[");
            requestInfo.append("url:").append(wrapper.getRequestURL());
            String queryString = wrapper.getQueryString();
            if (StringUtils.isNotBlank(queryString)) {
                requestInfo.append("?").append(queryString);
            }
            if (!"GET".equalsIgnoreCase(wrapper.getMethod())) {
                requestInfo.append(",method:").append(wrapper.getMethod());
            }
            byte[] contentAsByteArray = wrapper.getContentAsByteArray();
            if (ArrayUtils.isNotEmpty(contentAsByteArray)) {
                requestInfo.append(",body:").append(StringUtils.toEncodedString(contentAsByteArray, Charset.forName(wrapper.getCharacterEncoding())));
            }
            requestInfo.append("]");
        }
        log.error(msg + " msg[{}],trace{}[{}]{}", e.getMessage(), requestInfo);
    }

    @Data
    @AllArgsConstructor
    public static final class InputError {
        private String name;
        private String message;
    }

    /**
     * exception with messages param
     */
    public static String handleExceptionMessageWithParams(String message, String... params) {
        if (StringUtils.isBlank(message)) {
            return "";
        }
        if (ObjectUtils.isEmpty(params) || params.length == 0) {
            return message;
        }

        StringBuilder unFormattedMessage = new StringBuilder(message);
        Pattern stringPattern = Pattern.compile("\\%s");
        long count = stringPattern.matcher(unFormattedMessage.toString()).results().count();
        long needCount = params.length - count;
        for (int i = 0; i < needCount; i++) {
            unFormattedMessage.append(" %s");
        }

        return String.format(unFormattedMessage.toString(), (Object[]) params);
    }
}
