package com.example.backend.util;

import java.io.Serial;

public class PostingDuplicationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1265566794578703852L;

    public PostingDuplicationException(String message) {
        //
        super(message);
    }
}
