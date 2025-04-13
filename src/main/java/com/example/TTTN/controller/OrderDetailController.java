package com.example.TTTN.controller;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.OrderDetailDto;
import com.example.TTTN.service.OrderDetailService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-details")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

//    @PreAuthorize("hasRole('ADMIN_KD')")
    @PostMapping
    public ResponseEntity<List<OrderDetailDto>> createOrderDetails(@RequestBody List<OrderDetailDto> orderDetailDtos) {
        return new ResponseEntity<>(orderDetailService.createOrderDetails(orderDetailDtos), HttpStatus.CREATED);
    }

    @GetMapping
    public ListResponse<OrderDetailDto> getAllOrderDetails(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return orderDetailService.getAllOrderDetails(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailDto> getOrderDetailById(@PathVariable("id") long id) {
        return new ResponseEntity<>(orderDetailService.getOrderDetailById(id), HttpStatus.OK);
    }

//    @PreAuthorize("hasRole('ADMIN_KD')")
    @PutMapping("/orders/{orderId}")
    public ResponseEntity<List<OrderDetailDto>> updateOrderDetails(
            @PathVariable long orderId,
            @RequestBody List<OrderDetailDto> orderDetailDtos) {

        List<OrderDetailDto> updatedDetails = orderDetailService.updateOrderDetails(orderId, orderDetailDtos);
        return ResponseEntity.ok(updatedDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderDetailById(@PathVariable("id") long id) {
        orderDetailService.deleteOrderDetailById(id);
        return new ResponseEntity<>("Delete successfully!!!", HttpStatus.OK);
    }
}
