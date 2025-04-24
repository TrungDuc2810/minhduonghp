package com.example.TTTN.payload;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PartnerDto {
    private long id;
    @NotEmpty
    private String name;
    private String address;
    private String phone;
    private String email;
    private String taxCode;
    private double debt;
    private String organization;
    private long partnerTypeId;
}
