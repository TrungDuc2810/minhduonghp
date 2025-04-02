package com.example.TTTN.service.impl;

import com.example.TTTN.entity.*;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.OrderDetailDto;
import com.example.TTTN.repository.*;
import com.example.TTTN.service.OrderDetailService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PartnerRepository partnerRepository;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository,
                                  ModelMapper modelMapper,
                                  OrderRepository orderRepository,
                                  ProductRepository productRepository,
                                  PartnerRepository partnerRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.partnerRepository = partnerRepository;
    }

    private OrderDetailDto mapToDto(OrderDetail orderDetail) {
        return modelMapper.map(orderDetail, OrderDetailDto.class);
    }

    private OrderDetail mapToEntity(OrderDetailDto orderDetailDto) {
        return modelMapper.map(orderDetailDto, OrderDetail.class);
    }

    @Override
    @Transactional
    public List<OrderDetailDto> createOrderDetails(List<OrderDetailDto> orderDetailDtos) {
        List<OrderDetail> orderDetails = orderDetailDtos.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());

        return orderDetailRepository.saveAll(orderDetails)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
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
    @Transactional
    public List<OrderDetailDto> updateOrderDetails(long orderId, List<OrderDetailDto> orderDetailDtos) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", String.valueOf(orderId)));

        List<OrderDetail> currentOrderDetails = orderDetailRepository.findByOrderId(orderId);

        Map<Long, OrderDetail> existingDetailsMap = currentOrderDetails.stream()
                .collect(Collectors.toMap(od -> od.getProduct().getId(), od -> od));

        List<OrderDetail> updatedOrderDetails = new ArrayList<>();

        for (OrderDetailDto dto : orderDetailDtos) {
            OrderDetail orderDetail = existingDetailsMap.get(dto.getProductId());

            if (orderDetail != null) {
                orderDetail.setQuantity(dto.getQuantity());
            } else {
                orderDetail = mapToEntity(dto);
                orderDetail.setOrder(order);
            }

            updatedOrderDetails.add(orderDetail);
        }

        // Tìm các order detail bị xóa
        List<OrderDetail> removedOrderDetails = currentOrderDetails.stream()
                .filter(od -> !orderDetailDtos.stream()
                        .map(OrderDetailDto::getProductId)
                        .collect(Collectors.toSet())
                        .contains(od.getProduct().getId()))
                .collect(Collectors.toList());

        orderDetailRepository.deleteAll(removedOrderDetails);
        List<OrderDetail> savedOrderDetails = orderDetailRepository.saveAll(updatedOrderDetails);

        double oldTotalMoney = order.getTotalMoney(); // 200000
        double newTotalMoney = savedOrderDetails.stream() // 300000
                .mapToDouble(OrderDetail::getTotalMoney)
                .sum();
        double restDebt = Math.abs(oldTotalMoney - newTotalMoney);

        Partner partner = partnerRepository.findById(order.getPartner().getId()).orElseThrow(()
                -> new ResourceNotFoundException("Partner", "id", String.valueOf(order.getPartner().getId())));

        if (oldTotalMoney > newTotalMoney) {
            partner.setDebt(partner.getDebt() - restDebt);
        } else {
            partner.setDebt(partner.getDebt() + restDebt);
        }
        partnerRepository.save(partner);

        order.setTotalMoney(newTotalMoney);
        orderRepository.save(order);

        return savedOrderDetails.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteOrderDetailById(long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId).orElseThrow(()
                -> new ResourceNotFoundException("Order", "id", String.valueOf(orderDetailId)));

        orderDetailRepository.delete(orderDetail);
    }
}
