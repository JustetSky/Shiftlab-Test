package com.justet.shiftlabtest.api.error;

public record ErrorMessage(
        ErrorCode code,
        String message
) {}