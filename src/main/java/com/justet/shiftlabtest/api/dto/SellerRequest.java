package com.justet.shiftlabtest.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String contactInfo;
}