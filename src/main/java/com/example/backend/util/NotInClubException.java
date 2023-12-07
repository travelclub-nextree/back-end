package com.example.backend.util;

import java.io.Serial;

public class NotInClubException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -6070428144160605873L;

    public NotInClubException(String message) {
        //
        super(message);
    }
}
