package com.transunion.empservice.service;

import com.transunion.empservice.dto.EmployeeRequest;
import com.transunion.empservice.entity.Employee;
import com.transunion.empservice.exception.BusinessException;
import com.transunion.empservice.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEmployee() {
        EmployeeRequest request = new EmployeeRequest();
        request.setEmployeeName("John Doe");
        request.setPhoneNumber("1234567890");
        request.setDateOfBirth(LocalDate.of(1990, 1, 1));
        request.setSin("123456789");

        Employee employee = new Employee();
        employee.setEmployeeName("John Doe");
        employee.setPhoneNumber("1234567890");
        employee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        employee.setSin("6789");

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = employeeService.createEmployee(request);

        assertNotNull(result);
        assertEquals("John Doe", result.getEmployeeName());
        assertEquals("6789", result.getSin());
    }

    @Test
    void testGetAllEmployees_NoFilters() {
        Employee employee = new Employee();
        employee.setEmployeeName("Jane Doe");

        Page<Employee> page = new PageImpl<>(Collections.singletonList(employee));
        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(page);

        List<Employee> employees = employeeService.getAllEmployees(1, 5, null, null);

        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("Jane Doe", employees.get(0).getEmployeeName());
    }

    @Test
    void testGetAllEmployees_WithFilters() {
        Employee employee = new Employee();
        employee.setEmployeeName("Jane Doe");

        Page<Employee> page = new PageImpl<>(Collections.singletonList(employee));
        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        List<Employee> employees = employeeService.getAllEmployees(1, 5, 30, "Manager");

        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("Jane Doe", employees.get(0).getEmployeeName());
    }

    @Test
    void testGetEmployeeById() {
        Employee employee = new Employee();
        employee.setEmployeeName("John Doe");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeById(1L);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getEmployeeName());
    }

    @Test
    void testUpdateEmployee_Success() {
        EmployeeRequest request = new EmployeeRequest();
        request.setEmployeeName("John Updated");
        request.setDateOfBirth(LocalDate.of(1985, 05, 30));
        request.setSin("4567890");

        Employee existingEmployee = new Employee();
        existingEmployee.setEmployeeName("John Doe");
        existingEmployee.setDateOfBirth(LocalDate.of(1985, 05, 30));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);

        Employee updatedEmployee = employeeService.updateEmployee(1L, request);

        assertNotNull(updatedEmployee);
        assertEquals("John Updated", updatedEmployee.getEmployeeName());
    }

    @Test
    void testUpdateEmployee_NotFound() {
        EmployeeRequest request = new EmployeeRequest();
        request.setEmployeeName("John Updated");

        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            employeeService.updateEmployee(1L, request);
        });

        assertEquals("Employee not found with id 1", exception.getMessage());
    }

    @Test
    void testDeleteEmployee_Success() {
        Employee employee = new Employee();
        employee.setId(1L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(any(Employee.class));

        assertDoesNotThrow(() -> employeeService.deleteEmployee(1L));
        verify(employeeRepository, times(1)).delete(employee);
    }

    @Test
    void testDeleteEmployee_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            employeeService.deleteEmployee(1L);
        });

        assertEquals("Employee with ID 1 not found", exception.getMessage());
    }

    @Test
    void testExtractLast4Digits_Valid() {
        String sin = "123456789";
        String last4Digits = employeeService.extractLast4Digits(sin);
        assertEquals("6789", last4Digits);
    }

    @Test
    void testExtractLast4Digits_Invalid() {
        String sin = "123";
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            employeeService.extractLast4Digits(sin);
        });

        assertEquals("Invalid SIN number: must be a numeric value with at least 4 digits", exception.getMessage());
    }

    @Test
    void testCalculateAge_Valid() {
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        int age = employeeService.calculateAge(dateOfBirth);
        assertEquals(Period.between(dateOfBirth, LocalDate.now()).getYears(), age);
    }

    @Test
    void testCalculateAge_NullDateOfBirth() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            employeeService.calculateAge(null);
        });

        assertEquals("Date of birth cannot be null", exception.getMessage());
    }
}