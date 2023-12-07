package com.example.backend.util;

import java.io.Serial;

public class NoSuchPostingException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -2480013870728471672L;

    public NoSuchPostingException(String message) {
        //
        super(message);
    }
}
