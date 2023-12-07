package com.example.backend.util;

import java.io.Serial;

public class NoSuchMembershipException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4417793138799563659L;

    public NoSuchMembershipException(String message) {
        //
        super(message);
    }
}
