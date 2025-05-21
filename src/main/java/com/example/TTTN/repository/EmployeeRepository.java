package com.example.TTTN.repository;

import com.example.TTTN.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Boolean existsByPhoneNumber(String phonenumber);
}
