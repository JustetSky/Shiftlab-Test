package com.justet.shiftlabtest.api.model.seller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerRequest {

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String contactInfo;
}