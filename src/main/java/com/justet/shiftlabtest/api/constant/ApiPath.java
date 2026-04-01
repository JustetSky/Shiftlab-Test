package com.justet.shiftlabtest.api.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiPath {

    public static final String SELLERS = "/sellers";
    public static final String SELLER_ID = "/{sellerId}";

}
