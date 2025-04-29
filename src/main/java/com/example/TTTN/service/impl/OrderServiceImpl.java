package com.example.TTTN.service.impl;

import com.example.TTTN.entity.*;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.MonthlyRevenueDto;
import com.example.TTTN.payload.OrderDto;
import com.example.TTTN.payload.YearlyRevenueDto;
import com.example.TTTN.repository.OrderRepository;
import com.example.TTTN.repository.OrderStatusRepository;
import com.example.TTTN.repository.OrderTypeRepository;
import com.example.TTTN.repository.PartnerRepository;
import com.example.TTTN.service.OrderService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
        private final OrderRepository orderRepository;
        private final ModelMapper modelMapper;
        private final PartnerRepository partnerRepository;
        private final OrderStatusRepository orderStatusRepository;
        private final OrderTypeRepository orderTypeRepository;

        public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper,
                        PartnerRepository partnerRepository, OrderStatusRepository orderStatusRepository,
                        OrderTypeRepository orderTypeRepository) {
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
        @Transactional
        public OrderDto createOrder(OrderDto orderDto) {
                OrderStatus orderStatus = orderStatusRepository.findByName("Chưa thanh toán");
                orderDto.setOrderStatusId(orderStatus.getId());
                orderDto.setPaidMoney(0);

                Partner partner = partnerRepository.findById(orderDto.getPartnerId()).orElseThrow(
                                () -> new ResourceNotFoundException("Partner", "id",
                                                String.valueOf(orderDto.getPartnerId())));
                partner.setDebt(partner.getDebt() + orderDto.getTotalMoney());
                partnerRepository.save(partner);

                Order order = mapToEntity(orderDto);
                return mapToDto(orderRepository.save(order));
        }

        @Override
        public ListResponse<OrderDto> getAllOrders(int pageNo, int pageSize, String sortBy, String sortDir) {
                Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                                ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();

                PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

                Page<Order> orders = orderRepository.findAll(pageRequest);

                List<Order> listOfOrders = orders.getContent();

                List<OrderDto> content = listOfOrders.stream().map(this::mapToDto).toList();

                ListResponse<OrderDto> orderResponse = new ListResponse<>();
                orderResponse.setContent(content);
                orderResponse.setPageNo(orders.getNumber());
                orderResponse.setPageSize(orders.getSize());
                orderResponse.setTotalPages(orders.getTotalPages());
                orderResponse.setTotalElements((int) orders.getTotalElements());
                orderResponse.setLast(orders.isLast());

                return orderResponse;

        }

        @Override
        public ListResponse<OrderDto> getOrdersByPartnerId(long id, int pageNo, int pageSize, String sortBy,
                        String sortDir) {
                partnerRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", String.valueOf(id)));

                Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                                ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();

                PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

                Page<Order> orders = orderRepository.findByPartnerId(id, pageRequest);

                List<Order> listOfOrders = orders.getContent();

                List<OrderDto> content = listOfOrders.stream().map(this::mapToDto).toList();

                ListResponse<OrderDto> orderResponse = new ListResponse<>();
                orderResponse.setContent(content);
                orderResponse.setPageNo(orders.getNumber());
                orderResponse.setPageSize(orders.getSize());
                orderResponse.setTotalPages(orders.getTotalPages());
                orderResponse.setTotalElements((int) orders.getTotalElements());
                orderResponse.setLast(orders.isLast());

                return orderResponse;
        }

        @Override
        public OrderDto getOrderById(long orderId) {
                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new ResourceNotFoundException("Order", "id",
                                                String.valueOf(orderId)));
                return mapToDto(order);
        }

        @Override
        @Transactional
        public OrderDto updateOrder(long orderId, OrderDto orderDto) {
                if (orderDto.getTotalMoney() < orderDto.getPaidMoney()) {
                        throw new IllegalArgumentException("Tổng giá trị đơn hàng không thể nhỏ hơn tổng tiền đã trả.");
                }

                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new ResourceNotFoundException("Order", "id",
                                                String.valueOf(orderId)));

                Partner partner = partnerRepository.findById(orderDto.getPartnerId())
                                .orElseThrow(
                                                () -> new ResourceNotFoundException("Partner", "id",
                                                                String.valueOf(orderDto.getPartnerId())));

                OrderType orderType = orderTypeRepository.findById(orderDto.getOrderTypeId())
                                .orElseThrow(() -> new ResourceNotFoundException("Order type", "id",
                                                String.valueOf(orderDto.getOrderTypeId())));

                String statusName = orderDto.getPaidMoney() == 0.0 ? "Chưa thanh toán"
                                : orderDto.getPaidMoney() < orderDto.getTotalMoney() ? "Thanh toán một phần"
                                                : "Đã thanh toán";

                OrderStatus orderStatus = orderStatusRepository.findByName(statusName);
                if (orderStatus == null) {
                        throw new ResourceNotFoundException("Order status", "name", statusName);
                }

                double oldDebt = order.getTotalMoney() - order.getPaidMoney();
                double newDebt = orderDto.getTotalMoney() - orderDto.getPaidMoney();
                partner.setDebt(partner.getDebt() - oldDebt + newDebt);
                partnerRepository.save(partner);

                order.setTotalMoney(orderDto.getTotalMoney());
                order.setPaidMoney(orderDto.getPaidMoney());
                order.setOrderStatus(orderStatus);
                order.setOrderType(orderType);
                order.setPartner(partner);

                orderRepository.save(order);
                return mapToDto(order);
        }

        @Override
        @Transactional
        public void deleteOrderById(long orderId) {
                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new ResourceNotFoundException("Order", "id",
                                                String.valueOf(orderId)));

                Partner partner = order.getPartner();
                partner.setDebt(partner.getDebt() + order.getPaidMoney() - order.getTotalMoney());

                orderRepository.delete(order);
        }

        @Override
        public ListResponse<MonthlyRevenueDto> getMonthlyRevenue(int year) {
                // Lấy dữ liệu từ repository
                List<Object[]> revenueData = orderRepository.findRevenueByMonth(year);

                // Chuyển đổi sang MonthlyRevenueDto
                List<MonthlyRevenueDto> revenueList = revenueData.stream()
                                .map(data -> {
                                        int resultYear = ((Number) data[0]).intValue();
                                        int quarter = ((Number) data[1]).intValue();
                                        BigDecimal revenue = new BigDecimal(data[2].toString());
                                        return new MonthlyRevenueDto(resultYear, quarter, revenue); // truyền BigDecimal
                                })
                                .toList();

                // Tạo response
                ListResponse<MonthlyRevenueDto> response = new ListResponse<>();
                response.setContent(revenueList);
                response.setPageNo(0);
                response.setPageSize(revenueList.size());
                response.setTotalPages(1);
                response.setTotalElements(revenueList.size());
                response.setLast(true);

                return response;
        }

        @Override
        public ListResponse<MonthlyRevenueDto> getQuarterlyRevenue(int year) {
                // Lấy dữ liệu từ repository
                List<Object[]> revenueData = orderRepository.findRevenueByQuarter(year);

                // Chuyển đổi sang DTO
                List<MonthlyRevenueDto> revenueList = revenueData.stream()
                                .map(data -> {
                                        int resultYear = ((Number) data[0]).intValue();
                                        int quarter = ((Number) data[1]).intValue();
                                        BigDecimal revenue = new BigDecimal(data[2].toString());
                                        return new MonthlyRevenueDto(resultYear, quarter, revenue); // truyền BigDecimal
                                })
                                .toList();

                // Tạo response
                ListResponse<MonthlyRevenueDto> response = new ListResponse<>();
                response.setContent(revenueList);
                response.setPageNo(0);
                response.setPageSize(revenueList.size());
                response.setTotalPages(1);
                response.setTotalElements(revenueList.size());
                response.setLast(true);

                return response;
        }

        @Override
        public ListResponse<YearlyRevenueDto> getYearRevenue() {
                List<Object[]> revenueData = orderRepository.findRevenueByYear();

                List<YearlyRevenueDto> revenueList = revenueData.stream()
                                .map(data -> {
                                        int year = ((Number) data[0]).intValue();
                                        BigDecimal revenue = new BigDecimal(data[1].toString());
                                        return new YearlyRevenueDto(year, revenue.toPlainString());
                                })
                                .toList();

                ListResponse<YearlyRevenueDto> response = new ListResponse<>();
                response.setContent(revenueList);
                response.setPageNo(0);
                response.setPageSize(revenueList.size());
                response.setTotalPages(1);
                response.setTotalElements(revenueList.size());
                response.setLast(true);

                return response;
        }

}
