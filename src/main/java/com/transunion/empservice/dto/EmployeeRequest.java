package com.transunion.empservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.transunion.empservice.entity.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeRequest {
    private Long employeeId;
    @NotBlank(message = "Employee name is mandatory")
    private String employeeName;
    private Address address;
    @NotNull(message = "Date of birth is mandatory")
    @JsonFormat(pattern = "MM/dd/yyyy") // Specify date format
    private LocalDate dateOfBirth;
    @NotBlank(message = "Phone Number is mandatory")
    private String phoneNumber;
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "SIN is mandatory")
    private String sin;
}
