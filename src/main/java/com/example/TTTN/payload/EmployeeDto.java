package com.example.TTTN.payload;

import lombok.Data;

@Data
public class EmployeeDto {
    private long id;
    private String fullname;
    private String phoneNumber;
    private String address;
}
