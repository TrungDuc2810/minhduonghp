package com.example.TTTN.service.impl;

import com.example.TTTN.entity.*;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.PostDto;
import com.example.TTTN.payload.WarehouseTransferDto;
import com.example.TTTN.repository.*;
import com.example.TTTN.service.WarehouseTransferService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseTransferServiceImpl implements WarehouseTransferService {
    private final WarehouseTransferRepository warehouseTransferRepository;
    private final StatusRepository statusRepository;
    private final WarehouseProductRepository warehouseProductRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public WarehouseTransferServiceImpl(WarehouseTransferRepository warehouseTransferRepository,
                                        StatusRepository statusRepository,
                                        WarehouseProductRepository warehouseProductRepository,
                                        WarehouseRepository warehouseRepository,
                                        ProductRepository productRepository,
                                        ModelMapper modelMapper) {
        this.warehouseTransferRepository = warehouseTransferRepository;
        this.statusRepository = statusRepository;
        this.warehouseProductRepository = warehouseProductRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    private WarehouseTransferDto mapToDto(WarehouseTransfer warehouseTransfer) {
        return modelMapper.map(warehouseTransfer, WarehouseTransferDto.class);
    }

    private WarehouseTransfer mapToEntity(WarehouseTransferDto warehouseTransferDto) {
        return modelMapper.map(warehouseTransferDto, WarehouseTransfer.class);
    }

    @Override
    @Transactional
    public WarehouseTransferDto createWarehouseTransfer(WarehouseTransferDto warehouseTransferDto) {
        WarehouseTransfer warehouseTransfer = mapToEntity(warehouseTransferDto);

        Warehouse sourceWarehouse = warehouseRepository.findById(warehouseTransferDto.getSourceWarehouseId()).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse", "id", String.valueOf(warehouseTransferDto.getSourceWarehouseId())));

        Warehouse destWarehouse = warehouseRepository.findById(warehouseTransferDto.getDestinationWarehouseId()).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse", "id", String.valueOf(warehouseTransferDto.getDestinationWarehouseId())));

        Product product = productRepository.findById(warehouseTransferDto.getProductId()).orElseThrow(()
                -> new ResourceNotFoundException("Product", "id", String.valueOf(warehouseTransferDto.getProductId())));

        Status status = statusRepository.findByName("Đã hoàn thành");
        if (warehouseTransferDto.getStatusId() == status.getId()) {
            WarehouseProduct sourceProduct = warehouseProductRepository.findByWarehouseIdAndProductId(
                    warehouseTransferDto.getSourceWarehouseId(), warehouseTransferDto.getProductId());
            WarehouseProduct destProduct = warehouseProductRepository.findByWarehouseIdAndProductId(
                    warehouseTransferDto.getDestinationWarehouseId(), warehouseTransferDto.getProductId());

            if (sourceProduct == null || destProduct == null) {
                throw new ResourceNotFoundException("Warehouse product", "source or destination", "Not found");
            }

            if (sourceProduct.getQuantity() < warehouseTransferDto.getQuantity()) {
                throw new IllegalArgumentException("Source warehouse does not have enough quantity.");
            }

            sourceProduct.setQuantity(sourceProduct.getQuantity() - warehouseTransferDto.getQuantity());
            destProduct.setQuantity(destProduct.getQuantity() + warehouseTransferDto.getQuantity());

            warehouseProductRepository.save(sourceProduct);
            warehouseProductRepository.save(destProduct);
        }

        return mapToDto(warehouseTransferRepository.save(warehouseTransfer));
    }

    @Override
    @Transactional
    public WarehouseTransferDto updateWarehouseTransfer(long id, WarehouseTransferDto warehouseTransferDto) {
        WarehouseTransfer warehouseTransfer = warehouseTransferRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse transfer", "id", String.valueOf(id)));

        Warehouse sourceWarehouse = warehouseRepository.findById(warehouseTransferDto.getSourceWarehouseId()).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse", "id", String.valueOf(warehouseTransferDto.getSourceWarehouseId())));

        Warehouse destWarehouse = warehouseRepository.findById(warehouseTransferDto.getDestinationWarehouseId()).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse", "id", String.valueOf(warehouseTransferDto.getDestinationWarehouseId())));

        Product product = productRepository.findById(warehouseTransferDto.getProductId()).orElseThrow(()
                -> new ResourceNotFoundException("Product", "id", String.valueOf(warehouseTransferDto.getProductId())));

        Status status = statusRepository.findByName("Đã hoàn thành");
        if (warehouseTransferDto.getStatusId() == status.getId()) {
            WarehouseProduct sourceProduct = warehouseProductRepository.findByWarehouseIdAndProductId(
                    warehouseTransferDto.getSourceWarehouseId(), warehouseTransferDto.getProductId());
            WarehouseProduct destProduct = warehouseProductRepository.findByWarehouseIdAndProductId(
                    warehouseTransferDto.getDestinationWarehouseId(), warehouseTransferDto.getProductId());

            if (sourceProduct == null || destProduct == null) {
                throw new ResourceNotFoundException("Warehouse product", "source or destination", "Not found");
            }

            if (sourceProduct.getQuantity() < warehouseTransferDto.getQuantity()) {
                throw new IllegalArgumentException("Source warehouse does not have enough quantity.");
            }

            sourceProduct.setQuantity(sourceProduct.getQuantity() - warehouseTransferDto.getQuantity());
            destProduct.setQuantity(destProduct.getQuantity() + warehouseTransferDto.getQuantity());

            warehouseProductRepository.save(sourceProduct);
            warehouseProductRepository.save(destProduct);
        }

        warehouseTransfer.setStatus(status);
        warehouseTransfer.setProduct(product);
        warehouseTransfer.setQuantity(warehouseTransferDto.getQuantity());
        warehouseTransfer.setSourceWarehouse(sourceWarehouse);
        warehouseTransfer.setDestinationWarehouse(destWarehouse);

        return mapToDto(warehouseTransferRepository.save(warehouseTransfer));
    }

    @Override
    public ListResponse<WarehouseTransferDto> getAllWarehouseTransfers(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<WarehouseTransfer> warehouseTransfers = warehouseTransferRepository.findAll(pageRequest);

        List<WarehouseTransfer> listOfWarehouseTransfers = warehouseTransfers.getContent();

        List<WarehouseTransferDto> content = listOfWarehouseTransfers.stream().map(this::mapToDto).toList();

        ListResponse<WarehouseTransferDto> response = new ListResponse<>();
        response.setContent(content);
        response.setPageNo(warehouseTransfers.getNumber());
        response.setPageSize(warehouseTransfers.getSize());
        response.setTotalElements((int)warehouseTransfers.getTotalElements());
        response.setTotalPages(warehouseTransfers.getTotalPages());
        response.setLast(warehouseTransfers.isLast());

        return response;
    }

    @Override
    public WarehouseTransferDto getWarehouseTransferById(long id) {
        WarehouseTransfer warehouseTransfer = warehouseTransferRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse transfer", "id", String.valueOf(id)));

        return mapToDto(warehouseTransfer);
    }

    @Override
    public void deleteWarehouseTransferById(long id) {
        WarehouseTransfer warehouseTransfer = warehouseTransferRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse transfer", "id", String.valueOf(id)));
        warehouseTransferRepository.deleteById(id);
    }
}
