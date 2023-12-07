package com.example.backend.util;

import java.io.Serial;

public class NoPermisiionToDeleteBoard extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4811141926985725525L;

    public NoPermisiionToDeleteBoard(String message) {
        //
        super(message);
    }
}
