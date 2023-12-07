package com.example.backend.util;

import java.io.Serial;

public class NoAuthorityForModifyClub extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 7759693756260392001L;

    public NoAuthorityForModifyClub(String message) {
        //
        super(message);
    }
}
