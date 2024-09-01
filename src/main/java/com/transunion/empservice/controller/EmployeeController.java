package com.transunion.empservice.controller;

import com.transunion.empservice.constants.Constants;
import com.transunion.empservice.dto.EmployeeRequest;
import com.transunion.empservice.entity.Employee;
import com.transunion.empservice.service.EmployeeService;
import com.transunion.empservice.util.CommonUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Object> createEmployee(@RequestBody @Valid EmployeeRequest employeeRequest) {
        ResponseEntity<Object> responseEntity = null;

        try{
            Employee createdEmployee = employeeService.createEmployee(employeeRequest);
            responseEntity = commonUtil.generateResponseEntity("Successfully created employee record.", Constants.SUCCESS, createdEmployee, HttpStatus.CREATED);
        }catch (Exception e){
            responseEntity = commonUtil.generateResponseEntity("Error occurred while creating employee record.", Constants.FAILURE, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<Object> updateEmployee(@PathVariable Long employeeId, @RequestBody EmployeeRequest employeeRequest) {
        ResponseEntity<Object> responseEntity = null;

        try{
            Employee updatedEmployee = employeeService.updateEmployee(employeeId, employeeRequest);
            responseEntity = commonUtil.generateResponseEntity("Successfully created employee record.", Constants.SUCCESS, updatedEmployee, HttpStatus.OK);
        }catch (Exception e){
            responseEntity = commonUtil.generateResponseEntity("Error occurred while updating employee record.", Constants.FAILURE, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable Long employeeId) {
        ResponseEntity<Object> responseEntity = null;

        try{
            employeeService.deleteEmployee(employeeId);
            responseEntity = commonUtil.generateResponseEntity("Employee with ID " + employeeId + " has been deleted successfully.", Constants.SUCCESS, null, HttpStatus.OK);
        }catch (Exception e){
            responseEntity = commonUtil.generateResponseEntity("Error occurred while deleting employee record :", Constants.FAILURE, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping
    public ResponseEntity<Object> retrieveEmployees(
            @RequestParam(value = "age", required = false, defaultValue = "") String age,
            @RequestParam(value="title", required = false, defaultValue = "") String title,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size){
        ResponseEntity<Object> responseEntity = null;

        try{
            // Convert age from String to Integer if it's not empty
            Integer ageValue = (age.isEmpty() ? null : Integer.valueOf(age));
            List<Employee> employeeList = employeeService.getAllEmployees(page, size, ageValue, title);
            responseEntity = commonUtil.generateResponseEntity("Successfully retrieves employees", Constants.SUCCESS, employeeList, HttpStatus.OK);
        }catch (Exception e){
            responseEntity = commonUtil.generateResponseEntity("Error occurred while retrieving employee records :", Constants.FAILURE, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}
