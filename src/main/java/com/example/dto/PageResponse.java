package com.example.dto;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        boolean hasNext,
        boolean hasPrevious,
        long totalElements,
        int totalPages,
        int page,
        int size
) {
}
