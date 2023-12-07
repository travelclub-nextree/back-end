package com.example.backend.util;

import java.io.Serial;

public class NoSuchMemberException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -3676628524205329368L;

    public NoSuchMemberException(String message) {
        //
        super(message);
    }
}
