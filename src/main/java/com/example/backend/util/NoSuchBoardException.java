package com.example.backend.util;

import java.io.Serial;

public class NoSuchBoardException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -8119657705962891831L;

    public NoSuchBoardException(String message) {
        //
        super(message);
    }
}
