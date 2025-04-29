package com.example.TTTN.controller;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.MonthlyRevenueDto;
import com.example.TTTN.payload.OrderDto;
import com.example.TTTN.payload.YearlyRevenueDto;
import com.example.TTTN.service.OrderService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // @PreAuthorize("hasRole('ADMIN_KD')")
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        return new ResponseEntity<>(orderService.createOrder(orderDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ListResponse<OrderDto> getAllOrders(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return orderService.getAllOrders(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/partners/{id}")
    public ListResponse<OrderDto> getOrdersByPartnerId(
            @PathVariable(name = "id") long id,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return orderService.getOrdersByPartnerId(id, pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable(name = "id") long id) {
        return new ResponseEntity<>(orderService.getOrderById(id), HttpStatus.OK);
    }

    // @PreAuthorize("hasRole('ADMIN_KD')")
    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable(name = "id") long orderId,
            @RequestBody OrderDto orderDto) {
        return new ResponseEntity<>(orderService.updateOrder(orderId, orderDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderById(@PathVariable(name = "id") long id) {
        orderService.deleteOrderById(id);
        return new ResponseEntity<>("Delete successfully!!!", HttpStatus.OK);
    }

    @GetMapping("/monthly-revenue")
    public ListResponse<MonthlyRevenueDto> getMonthlyRevenue(@RequestParam() int year) {
        return orderService.getMonthlyRevenue(year);
    }

    @GetMapping("/quarterly-revenue")
    public ListResponse<MonthlyRevenueDto> getQuarterlyRevenue(@RequestParam() int year) {
        return orderService.getQuarterlyRevenue(year);
    }

    @GetMapping("/year-revenue")
    public ListResponse<YearlyRevenueDto> getYearRevenue() {
        return orderService.getYearRevenue();
    }
}
