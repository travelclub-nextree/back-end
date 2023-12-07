package com.example.backend.util;

import java.io.Serial;

public class MemberDuplicationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8825177725194354114L;

    public MemberDuplicationException(String message) {
        //
        super(message);
    }
}
