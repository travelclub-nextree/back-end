package com.example.backend.util;

import java.io.Serial;

public class InvalidEmailException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -3434652379390404587L;

    public InvalidEmailException(String message) {
        //
        super(message);
    }
}
