package com.wetie.imcapplication.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
