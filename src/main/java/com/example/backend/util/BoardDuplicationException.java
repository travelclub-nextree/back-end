package com.example.backend.util;

import java.io.Serial;

public class BoardDuplicationException extends RuntimeException {
    @Serial //Serialization 인터페이스를 구현한 클래스 내에서 직렬화 시스템과 관련된 멤버를 명시적으로 나타냄
    private static final long serialVersionUID = -5008148013281896704L;

    public BoardDuplicationException(String message) {
        //
        super(message);
    }
}
