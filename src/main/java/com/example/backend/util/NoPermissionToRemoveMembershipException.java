package com.example.backend.util;

import java.io.Serial;

public class NoPermissionToRemoveMembershipException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4441035242803501718L;

    public NoPermissionToRemoveMembershipException(String message) {
        //
        super(message);
    }
}
