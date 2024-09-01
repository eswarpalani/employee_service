package com.transunion.empservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String streetAddress;
    private String streetAddressLine2;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    @OneToOne(mappedBy = "address")  // Inverse side of the relationship
    @JsonBackReference
    private Employee employee;
}
