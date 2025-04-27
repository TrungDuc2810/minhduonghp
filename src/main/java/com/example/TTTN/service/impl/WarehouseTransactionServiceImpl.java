package com.example.TTTN.service.impl;

import com.example.TTTN.entity.*;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.WarehouseTransactionDto;
import com.example.TTTN.repository.*;
import com.example.TTTN.service.WarehouseTransactionService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseTransactionServiceImpl implements WarehouseTransactionService {
    private final WarehouseTransactionRepository warehouseTransactionRepository;
    private final ModelMapper modelMapper;
    private final StatusRepository statusRepository;
    private final OrderRepository orderRepository;
    private final WarehouseProductRepository warehouseProductRepository;
    private final ProductRepository productRepository;
    private final WarehouseTransactionTypeRepository warehouseTransactionTypeRepository;

    public WarehouseTransactionServiceImpl(WarehouseTransactionRepository warehouseTransactionRepository,
                                           ModelMapper modelMapper,
                                           StatusRepository statusRepository,
                                           OrderRepository orderRepository,
                                           WarehouseProductRepository warehouseProductRepository,
                                           ProductRepository productRepository, WarehouseTransactionTypeRepository warehouseTransactionTypeRepository) {
        this.warehouseTransactionRepository = warehouseTransactionRepository;
        this.modelMapper = modelMapper;
        this.statusRepository = statusRepository;
        this.orderRepository = orderRepository;
        this.warehouseProductRepository = warehouseProductRepository;
        this.productRepository = productRepository;
        this.warehouseTransactionTypeRepository = warehouseTransactionTypeRepository;
    }

    private WarehouseTransactionDto mapToDto(WarehouseTransaction warehouseTransaction) {
        return modelMapper.map(warehouseTransaction, WarehouseTransactionDto.class);
    }

    private WarehouseTransaction mapToEntity(WarehouseTransactionDto warehouseTransactionDto) {
        return modelMapper.map(warehouseTransactionDto, WarehouseTransaction.class);
    }

    @Override
    @Transactional
    public WarehouseTransactionDto createWarehouseTransaction(WarehouseTransactionDto dto) {
        WarehouseTransaction entity = mapToEntity(dto);

        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", String.valueOf(dto.getOrderId())));
        Status status = statusRepository.findById(dto.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Status", "id", String.valueOf(dto.getStatusId())));
        WarehouseTransactionType type = warehouseTransactionTypeRepository.findById(dto.getTransactionTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction Type", "id", String.valueOf(dto.getTransactionTypeId())));

        long warehouseId = dto.getWarehouseId();
        List<OrderDetail> orderDetails = order.getOrderDetails().stream().toList();

        boolean isImport = type.getName().equalsIgnoreCase("Nhập");
        boolean isExport = type.getName().equalsIgnoreCase("Xuất");
        String statusName = status.getName();

        for (OrderDetail detail : orderDetails) {
            long productId = detail.getProduct().getId();
            int quantity = detail.getQuantity();

            WarehouseProduct warehouseProduct = warehouseProductRepository.
                    findByWarehouseIdAndProductId(warehouseId, productId);
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", String.valueOf(productId)));

            if (isImport && statusName.equalsIgnoreCase("Đã hoàn thành")) {
                updateStock(warehouseId, productId, quantity);
            }
            if (isExport && (statusName.equalsIgnoreCase("Đã hoàn thành")
                    || statusName.equalsIgnoreCase("Đang xử lý"))) {
                updateStock(warehouseId, productId, -quantity);
            }
            if (isExport && statusName.equalsIgnoreCase("Không thành công")) {
                updateStock(warehouseId, productId, quantity);
            }
        }
        return mapToDto(warehouseTransactionRepository.save(entity));
    }

    public void updateStock(long warehouseId, long productId, int quantityChange) {
        WarehouseProduct wp = warehouseProductRepository.findByWarehouseIdAndProductId(warehouseId, productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", String.valueOf(productId)));

        wp.setQuantity(wp.getQuantity() + quantityChange);
        product.setQuantity(product.getQuantity() + quantityChange);

        warehouseProductRepository.save(wp);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public WarehouseTransactionDto updateWarehouseTransaction(long id, WarehouseTransactionDto dto) {
        WarehouseTransaction warehouseTransaction = warehouseTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse transaction", "id", String.valueOf(id)));

        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", String.valueOf(dto.getOrderId())));

        Status status = statusRepository.findById(dto.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Status", "id", String.valueOf(dto.getStatusId())));

        WarehouseTransactionType type = warehouseTransactionTypeRepository.findById(dto.getTransactionTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction Type", "id", String.valueOf(dto.getTransactionTypeId())));

        long warehouseId = dto.getWarehouseId();
        List<OrderDetail> orderDetails = order.getOrderDetails().stream().toList();

        boolean isImport = type.getName().equalsIgnoreCase("Nhập");
        boolean isExport = type.getName().equalsIgnoreCase("Xuất");
        String statusName = status.getName();

        for (OrderDetail detail : orderDetails) {
            long productId = detail.getProduct().getId();
            int quantity = detail.getQuantity();

            if (isImport && statusName.equalsIgnoreCase("Đã hoàn thành")) {
                updateStock(warehouseId, productId, quantity);
            }
            if (isExport && (statusName.equalsIgnoreCase("Đã hoàn thành") ||
                    statusName.equalsIgnoreCase("Đang xử lý"))) {
                updateStock(warehouseId, productId, -quantity);
            }
            if (isExport && statusName.equalsIgnoreCase("Không thành công")) {
                updateStock(warehouseId, productId, quantity);
            }
        }
        warehouseTransaction.setStatus(status);
        warehouseTransaction.setCreatedBy(dto.getCreatedBy());
        warehouseTransaction.setParticipant(dto.getParticipant());
        warehouseTransaction.setStorekeeper(dto.getStorekeeper());
        warehouseTransaction.setAccountant(dto.getAccountant());

        return mapToDto(warehouseTransactionRepository.save(warehouseTransaction));
    }

    @Override
    public ListResponse<WarehouseTransactionDto> getAllWarehouseTransactions(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<WarehouseTransaction> warehouseTransactions = warehouseTransactionRepository.findAll(pageRequest);

        List<WarehouseTransaction> listOfWarehouseTransactions = warehouseTransactions.getContent();

        List<WarehouseTransactionDto> content = listOfWarehouseTransactions.stream().map(this::mapToDto).toList();

        ListResponse<WarehouseTransactionDto> response = new ListResponse<>();
        response.setContent(content);
        response.setPageNo(warehouseTransactions.getNumber());
        response.setPageSize(warehouseTransactions.getSize());
        response.setTotalElements((int) warehouseTransactions.getTotalElements());
        response.setTotalPages(warehouseTransactions.getTotalPages());
        response.setLast(warehouseTransactions.isLast());

        return response;
    }

    @Override
    public WarehouseTransactionDto getWarehouseTransaction(long id) {
        WarehouseTransaction warehouseTransaction = warehouseTransactionRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Product", "id", String.valueOf(id)));

        return mapToDto(warehouseTransaction);
    }

    @Override
    public void deleteWarehouseTransaction(long id) {
        WarehouseTransaction warehouseTransaction = warehouseTransactionRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Product", "id", String.valueOf(id)));
        warehouseTransactionRepository.delete(warehouseTransaction);
    }
}
