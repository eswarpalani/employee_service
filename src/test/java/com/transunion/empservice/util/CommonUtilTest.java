package com.transunion.empservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CommonUtilTest {

    private CommonUtil commonUtil;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = mock(ObjectMapper.class);
        commonUtil = new CommonUtil();
        commonUtil.objectMapper = objectMapper;
    }

    @Test
    void testGenerateResponseEntity() throws JsonProcessingException {
        // Arrange
        String message = "Successfully created employee record.";
        String status = "SUCCESS";
        Object data = "Some data";
        HttpStatus httpStatus = HttpStatus.CREATED;

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("status", status);
        responseMap.put("message", message);
        responseMap.put("data", data);

        String expectedJson = "{\"status\":\"SUCCESS\",\"message\":\"Successfully created employee record.\",\"data\":\"Some data\"}";
        when(objectMapper.writeValueAsString(responseMap)).thenReturn(expectedJson);

        // Act
        ResponseEntity<Object> responseEntity = commonUtil.generateResponseEntity(message, status, data, httpStatus);

        // Assert
        assertEquals(httpStatus, responseEntity.getStatusCode());
        assertEquals(expectedJson, responseEntity.getBody());
    }

    @Test
    void testGenerateResponseEntity_WhenExceptionOccurs() throws JsonProcessingException {
        // Arrange
        String message = "Error occurred";
        String status = "FAILURE";
        Object data = "Error data";
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Test exception") {});

        // Act
        ResponseEntity<Object> responseEntity = commonUtil.generateResponseEntity(message, status, data, httpStatus);

        // Assert
        assertEquals(null, responseEntity); // Expecting null due to exception
    }
}
