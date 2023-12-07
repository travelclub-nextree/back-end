package com.example.backend.util;

import java.io.Serial;

public class NoAuthorityForDeletePost extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1118006600962097133L;

    public NoAuthorityForDeletePost(String message) {
        //
        super(message);
    }
}
