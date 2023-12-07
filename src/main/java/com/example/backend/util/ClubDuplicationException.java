package com.example.backend.util;

import java.io.Serial;

public class ClubDuplicationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5695176680159640556L;

    public ClubDuplicationException(String message) {
        //
        super(message);
    }
}
