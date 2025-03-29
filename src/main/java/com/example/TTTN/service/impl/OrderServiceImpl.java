package com.example.TTTN.service.impl;

import com.example.TTTN.entity.Order;
import com.example.TTTN.entity.OrderStatus;
import com.example.TTTN.entity.OrderType;
import com.example.TTTN.entity.Partner;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.OrderDto;
import com.example.TTTN.repository.OrderRepository;
import com.example.TTTN.repository.OrderStatusRepository;
import com.example.TTTN.repository.OrderTypeRepository;
import com.example.TTTN.repository.PartnerRepository;
import com.example.TTTN.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final PartnerRepository partnerRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final OrderTypeRepository orderTypeRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper, PartnerRepository partnerRepository, OrderStatusRepository orderStatusRepository, OrderTypeRepository orderTypeRepository) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.partnerRepository = partnerRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.orderTypeRepository = orderTypeRepository;
    }

    private OrderDto mapToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }

    private Order mapToEntity(OrderDto orderDto) {
        return modelMapper.map(orderDto, Order.class);
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = mapToEntity(orderDto);
        return mapToDto(orderRepository.save(order));
    }

    @Override
    public ListResponse<OrderDto> getAllOrders(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<Order> orders = orderRepository.findAll(pageRequest);

        List<Order> listOfOrders = orders.getContent();

        List<OrderDto> content = listOfOrders.stream().map(this::mapToDto).toList();

        ListResponse<OrderDto> orderResponse = new ListResponse<>();
        orderResponse.setContent(content);
        orderResponse.setPageNo(orders.getNumber());
        orderResponse.setPageSize(orders.getSize());
        orderResponse.setTotalPages(orders.getTotalPages());
        orderResponse.setTotalElements((int)orders.getTotalElements());
        orderResponse.setLast(orders.isLast());

        return orderResponse;

    }

    @Override
    public OrderDto getOrderById(long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()
                -> new ResourceNotFoundException("Order", "id", String.valueOf(orderId)));
        return mapToDto(order);
    }

    @Override
    public OrderDto updateOrder(long orderId, OrderDto orderDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(()
                -> new ResourceNotFoundException("Order", "id", String.valueOf(orderId)));

        Partner partner = partnerRepository.findById(orderDto.getPartnerId()).orElseThrow(()
                -> new ResourceNotFoundException("Partner", "id", String.valueOf(orderDto.getPartnerId())));

        OrderStatus orderStatus = orderStatusRepository.findById(orderDto.getOrderStatusId()).orElseThrow(()
                -> new ResourceNotFoundException("Order", "id", String.valueOf(orderDto.getOrderStatusId())));

        OrderType orderType = orderTypeRepository.findById(orderDto.getOrderTypeId()).orElseThrow(()
                -> new ResourceNotFoundException("Partner", "id", String.valueOf(orderDto.getOrderTypeId())));

        order.setTotalMoney(orderDto.getTotalMoney());
        order.setPaidMoney(orderDto.getPaidMoney());
        order.setOrderStatus(orderStatus);
        order.setOrderType(orderType);
        order.setPartner(partner);

        orderRepository.save(order);

        return mapToDto(order);
    }

    @Override
    public void deleteOrderById(long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()
                -> new ResourceNotFoundException("Order", "id", String.valueOf(orderId)));

        orderRepository.delete(order);
    }
}
