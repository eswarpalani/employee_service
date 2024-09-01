package com.transunion.empservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transunion.empservice.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CommonUtil {

    @Autowired
    ObjectMapper objectMapper;

    public ResponseEntity<Object> generateResponseEntity(String message, String status, Object data, HttpStatus httpStatus){
        ResponseEntity<Object> responseEntity = null;
        Map<String, Object> responseMap = new LinkedHashMap<>();

        try{
            responseMap.put("status", status);
            responseMap.put("message", message);
            responseMap.put("data", data);
            responseEntity = new ResponseEntity<>(objectMapper.writeValueAsString(responseMap), httpStatus);

        }catch (Exception e){
            e.printStackTrace();
        }
        return responseEntity;
    }
}
