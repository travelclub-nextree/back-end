package com.example.backend.util;

import java.io.Serial;

public class NoAuthorityForModifyPost extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5072384252180558625L;

    public NoAuthorityForModifyPost(String message) {
        //
        super(message);
    }
}
