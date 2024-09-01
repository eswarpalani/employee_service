package com.transunion.empservice.specification;

import org.springframework.data.jpa.domain.Specification;
import com.transunion.empservice.entity.Employee;

public class EmployeeSpecification {

    public static Specification<Employee> hasAge(Integer age) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("age"), age);
    }

    public static Specification<Employee> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("title"), title);
    }
}

