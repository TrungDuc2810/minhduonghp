package com.example.TTTN.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ListResponse<T> {
    private List<T> content;
    private int pageNo;
    private int pageSize;
    private int totalElements;
    private int totalPages;
    private boolean last;
}
