package com.example.backend.util;

import java.io.Serial;

public class BoardLimitOverException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1441095517389622124L;

    public BoardLimitOverException(String message) {
        //
        super(message);
    }
}
