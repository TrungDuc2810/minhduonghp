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

        Warehouse sourceWarehouse = warehouseRepository.findById(internalOrderDto.getSourceWarehouseId()).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse", "id", String.valueOf(internalOrderDto.getSourceWarehouseId())));

        Warehouse destWarehouse = warehouseRepository.findById(internalOrderDto.getDestinationWarehouseId()).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse", "id", String.valueOf(internalOrderDto.getDestinationWarehouseId())));

        Product product = productRepository.findById(internalOrderDto.getProductId()).orElseThrow(()
                -> new ResourceNotFoundException("Product", "id", String.valueOf(internalOrderDto.getProductId())));

        WarehouseProduct sourceProduct = warehouseProductRepository.findByWarehouseIdAndProductId(
                    internalOrderDto.getSourceWarehouseId(), internalOrderDto.getProductId());
        WarehouseProduct destProduct = warehouseProductRepository.findByWarehouseIdAndProductId(
                    internalOrderDto.getDestinationWarehouseId(), internalOrderDto.getProductId());

        Status status = statusRepository.findByName("Đã hoàn thành");
        if (internalOrderDto.getStatusId() == status.getId()) {
            if (sourceProduct == null || destProduct == null) {
                throw new ResourceNotFoundException("Warehouse product", "source or destination", "Not found");
            }

            if (sourceProduct.getQuantity() < internalOrderDto.getQuantity()) {
                throw new IllegalArgumentException("Kho nguồn không có đủ số lượng.");
            }

            sourceProduct.setQuantity(sourceProduct.getQuantity() - internalOrderDto.getQuantity());
            destProduct.setQuantity(destProduct.getQuantity() + internalOrderDto.getQuantity());

            warehouseProductRepository.save(sourceProduct);
            warehouseProductRepository.save(destProduct);
        } else {
            if (sourceProduct == null || destProduct == null) {
                throw new ResourceNotFoundException("Warehouse product", "source or destination", "Not found");
            }

            if (sourceProduct.getQuantity() < internalOrderDto.getQuantity()) {
                throw new IllegalArgumentException("Kho nguồn không có đủ số lượng.");
            }
        }

        return mapToDto(internalOrderRepository.save(internalOrder));
    }

    @Override
    @Transactional
    public InternalOrderDto updateInternalOrder(long id, InternalOrderDto internalOrderDto) {
        InternalOrder internalOrder = internalOrderRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse transfer", "id", String.valueOf(id)));

        Warehouse sourceWarehouse = warehouseRepository.findById(internalOrderDto.getSourceWarehouseId()).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse", "id", String.valueOf(internalOrderDto.getSourceWarehouseId())));

        Warehouse destWarehouse = warehouseRepository.findById(internalOrderDto.getDestinationWarehouseId()).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse", "id", String.valueOf(internalOrderDto.getDestinationWarehouseId())));

        Product product = productRepository.findById(internalOrderDto.getProductId()).orElseThrow(()
                -> new ResourceNotFoundException("Product", "id", String.valueOf(internalOrderDto.getProductId())));

        Status status = statusRepository.findByName("Đã hoàn thành");
        if (internalOrderDto.getStatusId() == status.getId()) {
            WarehouseProduct sourceProduct = warehouseProductRepository.findByWarehouseIdAndProductId(
                    internalOrderDto.getSourceWarehouseId(), internalOrderDto.getProductId());
            WarehouseProduct destProduct = warehouseProductRepository.findByWarehouseIdAndProductId(
                    internalOrderDto.getDestinationWarehouseId(), internalOrderDto.getProductId());

            if (sourceProduct == null || destProduct == null) {
                throw new ResourceNotFoundException("Warehouse product", "source or destination", "Not found");
            }

            if (sourceProduct.getQuantity() < internalOrderDto.getQuantity()) {
                throw new IllegalArgumentException("Kho nguồn không đủ số lượng.");
            }

            sourceProduct.setQuantity(sourceProduct.getQuantity() - internalOrderDto.getQuantity());
            destProduct.setQuantity(destProduct.getQuantity() + internalOrderDto.getQuantity());

            warehouseProductRepository.save(sourceProduct);
            warehouseProductRepository.save(destProduct);
        }

        internalOrder.setStatus(status);
        internalOrder.setProduct(product);
        internalOrder.setQuantity(internalOrderDto.getQuantity());
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
