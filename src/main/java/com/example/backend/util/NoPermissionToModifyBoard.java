package com.example.backend.util;

import java.io.Serial;

public class NoPermissionToModifyBoard extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4217345557973037024L;

    public NoPermissionToModifyBoard(String message) {
        //
        super(message);
    }
}
