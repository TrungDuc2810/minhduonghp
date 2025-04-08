package com.example.TTTN.controller.common;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.service.common.GenericService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public abstract class GenericController<Dto> {

    protected final GenericService<Dto> service;

    protected GenericController(GenericService<Dto> service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Dto> create(@RequestBody Dto dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dto> update(@PathVariable long id, @RequestBody Dto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted successfully!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dto> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ListResponse<Dto> getAll(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir
    ) {
        return service.getAll(pageNo, pageSize, sortBy, sortDir);
    }
}
