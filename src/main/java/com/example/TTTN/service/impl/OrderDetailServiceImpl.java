package com.example.TTTN.service.impl;

import com.example.TTTN.entity.*;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.OrderDetailDto;
import com.example.TTTN.repository.*;
import com.example.TTTN.service.OrderDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository,
                                  ModelMapper modelMapper,
                                  OrderRepository orderRepository,
                                  ProductRepository productRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    private OrderDetailDto mapToDto(OrderDetail orderDetail) {
        return modelMapper.map(orderDetail, OrderDetailDto.class);
    }

    private OrderDetail mapToEntity(OrderDetailDto orderDetailDto) {
        return modelMapper.map(orderDetailDto, OrderDetail.class);
    }

    @Override
    public OrderDetailDto createOrderDetail(OrderDetailDto orderDetailDto) {
        OrderDetail orderDetail = mapToEntity(orderDetailDto);
        return mapToDto(orderDetailRepository.save(orderDetail));
    }

    @Override
    public ListResponse<OrderDetailDto> getAllOrderDetails(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<OrderDetail> orderDetails = orderDetailRepository.findAll(pageRequest);

        List<OrderDetail> listOfOrderDetails = orderDetails.getContent();

        List<OrderDetailDto> content = listOfOrderDetails.stream().map(this::mapToDto).toList();

        ListResponse<OrderDetailDto> orderDetailResponse = new ListResponse<>();
        orderDetailResponse.setContent(content);
        orderDetailResponse.setPageNo(orderDetails.getNumber());
        orderDetailResponse.setPageSize(orderDetails.getSize());
        orderDetailResponse.setTotalPages(orderDetails.getTotalPages());
        orderDetailResponse.setTotalElements((int)orderDetails.getTotalElements());
        orderDetailResponse.setLast(orderDetails.isLast());

        return orderDetailResponse;

    }

    @Override
    public ListResponse<OrderDetailDto> getOrderDetailsByOrderId(long orderId, int pageNo, int pageSize, String sortBy, String sortDir) {
        orderRepository.findById(orderId).orElseThrow(()
                -> new ResourceNotFoundException("Order", "id", String.valueOf(orderId)));

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId, pageRequest);

        List<OrderDetail> listOfOrderDetails = orderDetails.getContent();

        List<OrderDetailDto> content = listOfOrderDetails.stream().map(this::mapToDto).toList();

        ListResponse<OrderDetailDto> orderDetailResponse = new ListResponse<>();
        orderDetailResponse.setContent(content);
        orderDetailResponse.setPageNo(orderDetails.getNumber());
        orderDetailResponse.setPageSize(orderDetails.getSize());
        orderDetailResponse.setTotalPages(orderDetails.getTotalPages());
        orderDetailResponse.setTotalElements((int)orderDetails.getTotalElements());
        orderDetailResponse.setLast(orderDetails.isLast());

        return orderDetailResponse;
    }

    @Override
    public OrderDetailDto getOrderDetailById(long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId).orElseThrow(()
                -> new ResourceNotFoundException("Order detail", "id", String.valueOf(orderDetailId)));
        return mapToDto(orderDetail);
    }

    @Override
    public OrderDetailDto updateOrderDetail(long orderDetailId, OrderDetailDto orderDetailDto) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId).orElseThrow(()
                -> new ResourceNotFoundException("Order detail", "id", String.valueOf(orderDetailId)));

        Order order = orderRepository.findById(orderDetailDto.getOrderId()).orElseThrow(()
                -> new ResourceNotFoundException("Order", "id", String.valueOf(orderDetailDto.getOrderId())));

        Product product = productRepository.findById(orderDetailDto.getProductId()).orElseThrow(()
                -> new ResourceNotFoundException("Product", "id", String.valueOf(orderDetailDto.getProductId())));

        orderDetail.setQuantity(orderDetailDto.getQuantity());
        orderDetail.setPrice(orderDetailDto.getPrice());
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);

        orderDetailRepository.save(orderDetail);

        return mapToDto(orderDetail);
    }

    @Override
    public void deleteOrderDetailById(long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId).orElseThrow(()
                -> new ResourceNotFoundException("Order", "id", String.valueOf(orderDetailId)));

        orderDetailRepository.delete(orderDetail);
    }
}
