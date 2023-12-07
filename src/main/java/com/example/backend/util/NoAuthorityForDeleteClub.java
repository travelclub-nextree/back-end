package com.example.backend.util;

import java.io.Serial;

public class NoAuthorityForDeleteClub extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -66326304208345573L;

    public NoAuthorityForDeleteClub(String message) {
        //
        super(message);
    }
}
