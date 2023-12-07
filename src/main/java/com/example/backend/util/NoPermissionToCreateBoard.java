package com.example.backend.util;

import java.io.Serial;

public class NoPermissionToCreateBoard extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -2246400138208684714L;

    public NoPermissionToCreateBoard(String message) {
        //
        super(message);
    }
}
