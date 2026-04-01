package com.justet.shiftlabtest.api.model.seller;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerResponse {

    private Long id;
    private String name;
    private String contactInfo;
    private LocalDateTime registrationDate;
}