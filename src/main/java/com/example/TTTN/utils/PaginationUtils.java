package com.example.TTTN.utils;

import com.example.TTTN.payload.ListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.function.Function;


public class PaginationUtils {
    public static PageRequest createPageRequest(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(page, size, sort);
    }

    // E is Entity, D is DTO
    // mapper is this::mapToDto
    public static <E, D> ListResponse<D> toListResponse(Page<E> page, Function<E, D> mapper) {
        List<D> content = page.getContent().stream().map(mapper).toList();

        ListResponse<D> response = new ListResponse<>();
        response.setContent(content);
        response.setPageNo(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements((int) page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLast(page.isLast());

        return response;
    }
}
