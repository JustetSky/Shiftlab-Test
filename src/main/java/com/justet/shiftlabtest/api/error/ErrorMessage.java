package com.justet.shiftlabtest.api.error;

import com.justet.shiftlabtest.core.exception.ErrorCode;

public record ErrorMessage(
        ErrorCode code,
        String message
) {}