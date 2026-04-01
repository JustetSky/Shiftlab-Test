package com.justet.shiftlabtest.api.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiPath {

    public static final String SELLERS = "/sellers";
    public static final String SELLER_ID = "/{sellerId}";
    public static final String TRANSACTIONS = "/transactions";
    public static final String TRANSACTION_ID = "/{transactionId}";
    public static final String SELLER_TRANSACTIONS = "/seller/{sellerId}";
    public static final String ANALYTICS = "/analytics";
    public static final String MOST_PRODUCTIVE = "/most-productive/{period}";;

}
