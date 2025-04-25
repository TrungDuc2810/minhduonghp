package com.example.TTTN.service.impl;

import com.example.TTTN.entity.*;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.InternalOrderDto;
import com.example.TTTN.repository.*;
import com.example.TTTN.service.InternalOrderService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternalOrderServiceImpl implements InternalOrderService {
    private final InternalOrderRepository internalOrderRepository;
    private final StatusRepository statusRepository;
    private final WarehouseProductRepository warehouseProductRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public InternalOrderServiceImpl(InternalOrderRepository internalOrderRepository,
                                    StatusRepository statusRepository,
                                    WarehouseProductRepository warehouseProductRepository,
                                    WarehouseRepository warehouseRepository,
                                    ProductRepository productRepository,
                                    ModelMapper modelMapper) {
        this.internalOrderRepository = internalOrderRepository;
        this.statusRepository = statusRepository;
        this.warehouseProductRepository = warehouseProductRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    private InternalOrderDto mapToDto(InternalOrder internalOrder) {
        return modelMapper.map(internalOrder, InternalOrderDto.class);
    }

    private InternalOrder mapToEntity(InternalOrderDto internalOrderDto) {
        return modelMapper.map(internalOrderDto, InternalOrder.class);
    }

    @Override
    @Transactional
    public InternalOrderDto createInternalOrder(InternalOrderDto internalOrderDto) {
        InternalOrder internalOrder = mapToEntity(internalOrderDto);

        warehouseRepository.findById(internalOrderDto.getSourceWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", String.valueOf(internalOrderDto.getSourceWarehouseId())));
        warehouseRepository.findById(internalOrderDto.getDestinationWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", String.valueOf(internalOrderDto.getDestinationWarehouseId())));
        productRepository.findById(internalOrderDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", String.valueOf(internalOrderDto.getProductId())));

        Status status = statusRepository.findById(internalOrderDto.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Status", "id", String.valueOf(internalOrderDto.getStatusId())));

        adjustStockForInternalOrder(
                status.getName(), internalOrderDto.getSourceWarehouseId(),
                internalOrderDto.getDestinationWarehouseId(),
                internalOrderDto.getProductId(), internalOrderDto.getQuantity()
        );

        return mapToDto(internalOrderRepository.save(internalOrder));
    }

    public void adjustStockForInternalOrder(String statusName, long sourceWarehouseId,
                                            long destWarehouseId, long productId, int quantity) {
        WarehouseProduct source = warehouseProductRepository.findByWarehouseIdAndProductId(sourceWarehouseId, productId);
        WarehouseProduct dest = warehouseProductRepository.findByWarehouseIdAndProductId(destWarehouseId, productId);

        if (source == null) {
            throw new ResourceNotFoundException("Warehouse product", "source", String.valueOf(sourceWarehouseId));
        }
        if (statusName.equalsIgnoreCase("Đã hoàn thành")) {
            if (dest == null) {
                throw new ResourceNotFoundException("Warehouse product", "destination", String.valueOf(destWarehouseId));
            }
            if (source.getQuantity() < quantity) {
                throw new IllegalArgumentException("Không đủ hàng trong kho nguồn.");
            }
            source.setQuantity(source.getQuantity() - quantity);
            dest.setQuantity(dest.getQuantity() + quantity);
            warehouseProductRepository.save(source);
            warehouseProductRepository.save(dest);
        } else if (statusName.equalsIgnoreCase("Đang xử lý")) {
            if (source.getQuantity() < quantity) {
                throw new IllegalArgumentException("Không đủ hàng để lock.");
            }
            source.setQuantity(source.getQuantity() - quantity);
            warehouseProductRepository.save(source);
        } else if (statusName.equalsIgnoreCase("Không thành công")) {
            source.setQuantity(source.getQuantity() + quantity);
            warehouseProductRepository.save(source);
        }
    }

    @Override
    @Transactional
    public InternalOrderDto updateInternalOrder(long id, InternalOrderDto dto) {
        InternalOrder internalOrder = internalOrderRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Internal Order", "id", String.valueOf(id)));

        Warehouse sourceWarehouse = warehouseRepository.findById(dto.getSourceWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", String.valueOf(dto.getSourceWarehouseId())));
        Warehouse destWarehouse = warehouseRepository.findById(dto.getDestinationWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", String.valueOf(dto.getDestinationWarehouseId())));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", String.valueOf(dto.getProductId())));
        Status status = statusRepository.findById(dto.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Status", "id", String.valueOf(dto.getStatusId())));

        adjustStockForInternalOrder(status.getName(), dto.getSourceWarehouseId(),
                dto.getDestinationWarehouseId(), dto.getProductId(), dto.getQuantity()
        );

        internalOrder.setStatus(status);
        internalOrder.setProduct(product);
        internalOrder.setQuantity(dto.getQuantity());
        internalOrder.setSourceWarehouse(sourceWarehouse);
        internalOrder.setDestinationWarehouse(destWarehouse);

        return mapToDto(internalOrderRepository.save(internalOrder));
    }

    @Override
    public ListResponse<InternalOrderDto> getAllInternalOrders(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<InternalOrder> warehouseTransfers = internalOrderRepository.findAll(pageRequest);

        List<InternalOrder> listOfInternalOrders = warehouseTransfers.getContent();

        List<InternalOrderDto> content = listOfInternalOrders.stream().map(this::mapToDto).toList();

        ListResponse<InternalOrderDto> response = new ListResponse<>();
        response.setContent(content);
        response.setPageNo(warehouseTransfers.getNumber());
        response.setPageSize(warehouseTransfers.getSize());
        response.setTotalElements((int)warehouseTransfers.getTotalElements());
        response.setTotalPages(warehouseTransfers.getTotalPages());
        response.setLast(warehouseTransfers.isLast());

        return response;
    }

    @Override
    public InternalOrderDto getInternalOrderById(long id) {
        InternalOrder internalOrder = internalOrderRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse transfer", "id", String.valueOf(id)));

        return mapToDto(internalOrder);
    }

    @Override
    public void deleteInternalOrderById(long id) {
        InternalOrder internalOrder = internalOrderRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse transfer", "id", String.valueOf(id)));
        internalOrderRepository.deleteById(id);
    }
}
