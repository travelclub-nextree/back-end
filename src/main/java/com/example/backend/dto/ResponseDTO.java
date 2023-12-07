package com.example.backend.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class ResponseDTO<T> {
    private List<T> items;
    private Page<T> pageItems;

    private T item;
    private String errorMessage;
    private int statusCode;

    private boolean lastPage;
    private PaginationInfo paginationInfo;

    @Data
    public static class PaginationInfo {
        private int totalPages;
        private int currentPage;
        private long totalElements;
    }
}
