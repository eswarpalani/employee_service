package com.transunion.empservice.service;

import com.transunion.empservice.dto.EmployeeRequest;
import com.transunion.empservice.entity.Employee;
import com.transunion.empservice.exception.BusinessException;
import com.transunion.empservice.repository.EmployeeRepository;
import com.transunion.empservice.specification.EmployeeSpecification;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee createEmployee(EmployeeRequest employeeRequest)  {
        Employee employee = new Employee();
        extractEmployee(employeeRequest, employee);
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees(int page, int size, Integer age, String title) {
        List<Employee> employeeList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Employee> employeesPage = null;
        Specification<Employee> spec = Specification.where(null);

        // Check if both parameters are empty or null
        if (age == null && (title == null || title.isEmpty())) {
            // Return all employees without any filtering
            employeesPage = employeeRepository.findAll(pageable);
        }else{
            if (age != null) {
                spec = spec.and(EmployeeSpecification.hasAge(age));
            }
            if (StringUtils.isNotBlank(title)) {
                spec = spec.and(EmployeeSpecification.hasTitle(title));
            }
            employeesPage = employeeRepository.findAll(spec, pageable);
        }

        if(employeesPage != null){
            employeeList = employeesPage.getContent();
        }
        return employeeList;
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee updateEmployee(Long id, EmployeeRequest employeeRequest) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            extractEmployee(employeeRequest, employee);
            return employeeRepository.save(employee);
        } else {
            throw new BusinessException("Employee not found with id " + id, HttpStatus.NO_CONTENT.value());
        }
    }

    public void deleteEmployee(Long id) {
        // Check if the employee exists in the database
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Employee with ID " + id + " not found", HttpStatus.NOT_FOUND.value()));

        // Delete the employee
        employeeRepository.delete(employee);
    }

    private void extractEmployee(EmployeeRequest employeeRequest, Employee employee){
        employee.setPhoneNumber(employeeRequest.getPhoneNumber());
        employee.setEmployeeName(employeeRequest.getEmployeeName());
        employee.setAddress(employeeRequest.getAddress());
        employee.setDateOfBirth(employeeRequest.getDateOfBirth());
        employee.setTitle(employeeRequest.getTitle());
        employee.setAge(calculateAge(employeeRequest.getDateOfBirth()));
        employee.setSin(extractLast4Digits(employeeRequest.getSin()));
    }

    // Extract the last 4 digits of the SIN
    public String extractLast4Digits(String sin) {
        if (StringUtils.isNotBlank(sin) && StringUtils.isNumeric(sin) && sin.length() >= 4) {
            return sin.substring(sin.length() - 4);
        } else {
            throw new BusinessException("Invalid SIN number: must be a numeric value with at least 4 digits", HttpStatus.BAD_REQUEST.value());
        }
    }

    // Calculate the age from the date of birth
    public int calculateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new BusinessException("Date of birth cannot be null", HttpStatus.BAD_REQUEST.value());
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}
