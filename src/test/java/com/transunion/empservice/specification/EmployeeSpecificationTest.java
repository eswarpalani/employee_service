package com.transunion.empservice.specification;

import com.transunion.empservice.entity.Employee;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class EmployeeSpecificationTest {
    private Root<Employee> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder criteriaBuilder;

    @BeforeEach
    public void setUp() {
        root = Mockito.mock(Root.class);
        query = Mockito.mock(CriteriaQuery.class);
        criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
    }

    @Test
    public void testHasAge() {
        Integer age = 30;
        Predicate predicate = Mockito.mock(Predicate.class);
        when(criteriaBuilder.equal(root.get("age"), age)).thenReturn(predicate);
        Specification<Employee> spec = EmployeeSpecification.hasAge(age);
        Predicate resultPredicate = spec.toPredicate(root, query, criteriaBuilder);
        assertNotNull(resultPredicate, "Predicate should not be null");
        Mockito.verify(criteriaBuilder).equal(root.get("age"), age);
    }

    @Test
    public void testHasTitle() {
        String title = "Engineer";
        Predicate predicate = Mockito.mock(Predicate.class);
        when(criteriaBuilder.equal(root.get("title"), title)).thenReturn(predicate);
        Specification<Employee> spec = EmployeeSpecification.hasTitle(title);
        Predicate resultPredicate = spec.toPredicate(root, query, criteriaBuilder);
        assertNotNull(resultPredicate, "Predicate should not be null");
        Mockito.verify(criteriaBuilder).equal(root.get("title"), title);
    }
}