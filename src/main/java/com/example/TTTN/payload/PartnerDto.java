package com.example.TTTN.payload;

import com.example.TTTN.entity.PartnerType;
import jakarta.persistence.*;
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
    private double debt;
    private long partnerTypeId;
}
