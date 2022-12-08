package com.dpm.winwin.api.common.response.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record GlobalPageResponseDto<T>(
    List<T> content,
    long totalElements,
    int totalPages,
    boolean hasNextPages
) {

    public static <T> GlobalPageResponseDto<T> of(Page<T> page) {
        return new GlobalPageResponseDto<T>(
            page.getContent(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.hasNext()
        );
    }
}