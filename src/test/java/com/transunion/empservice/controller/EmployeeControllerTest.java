package com.transunion.empservice.controller;

import com.transunion.empservice.constants.Constants;
import com.transunion.empservice.dto.EmployeeRequest;
import com.transunion.empservice.entity.Employee;
import com.transunion.empservice.service.EmployeeService;
import com.transunion.empservice.util.CommonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private CommonUtil commonUtil;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    void testCreateEmployee_Success() throws Exception {
        EmployeeRequest employeeRequest = new EmployeeRequest();
        Employee employee = new Employee();
        ResponseEntity<Object> successResponse = new ResponseEntity<>("Successfully created employee record.", HttpStatus.CREATED);

        when(employeeService.createEmployee(any(EmployeeRequest.class))).thenReturn(employee);
        when(commonUtil.generateResponseEntity(any(), eq(Constants.SUCCESS), eq(employee), eq(HttpStatus.CREATED)))
                .thenReturn(successResponse);

        mockMvc.perform(post("/api/employees")
                        .contentType("application/json")
                        .content("{}")) // JSON content can be added as needed
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateEmployee_Success() throws Exception {
        EmployeeRequest employeeRequest = new EmployeeRequest();
        Employee employee = new Employee();
        ResponseEntity<Object> successResponse = new ResponseEntity<>("Successfully updated employee record.", HttpStatus.OK);

        when(employeeService.updateEmployee(anyLong(), any(EmployeeRequest.class))).thenReturn(employee);
        when(commonUtil.generateResponseEntity(any(), eq(Constants.SUCCESS), eq(employee), eq(HttpStatus.OK)))
                .thenReturn(successResponse);

        mockMvc.perform(put("/api/employees/1")
                        .contentType("application/json")
                        .content("{}")) // JSON content can be added as needed
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteEmployee_Success() throws Exception {
        doNothing().when(employeeService).deleteEmployee(anyLong());
        ResponseEntity<Object> successResponse = new ResponseEntity<>("Employee with ID 1 has been deleted successfully.", HttpStatus.OK);

        when(commonUtil.generateResponseEntity(any(), eq(Constants.SUCCESS), eq(null), eq(HttpStatus.OK)))
                .thenReturn(successResponse);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testRetrieveEmployees_Success() throws Exception {
        List<Employee> employeeList = Collections.emptyList();
        ResponseEntity<Object> successResponse = new ResponseEntity<>("Successfully retrieves employees", HttpStatus.OK);

        when(employeeService.getAllEmployees(anyInt(), anyInt(), any(), any())).thenReturn(employeeList);
        when(commonUtil.generateResponseEntity(any(), eq(Constants.SUCCESS), eq(employeeList), eq(HttpStatus.OK)))
                .thenReturn(successResponse);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk());
    }
}