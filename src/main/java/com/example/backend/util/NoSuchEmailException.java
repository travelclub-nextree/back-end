package com.example.backend.util;

import java.io.Serial;

public class NoSuchEmailException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -9185936946283149511L;

    public NoSuchEmailException(String message) {
        //
        super(message);
    }
}
