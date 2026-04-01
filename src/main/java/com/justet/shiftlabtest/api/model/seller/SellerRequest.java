package com.justet.shiftlabtest.api.model.seller;

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