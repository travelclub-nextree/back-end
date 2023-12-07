package com.example.backend.util;

import java.io.Serial;

public class PasswordNotMatchException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -8881459221385339866L;

    public PasswordNotMatchException(String message) {
        //
        super(message);
    }
}
