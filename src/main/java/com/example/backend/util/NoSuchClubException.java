package com.example.backend.util;

import java.io.Serial;

public class NoSuchClubException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 887166294700941684L;

    public NoSuchClubException(String message) {
        //
        super(message);
    }
}
