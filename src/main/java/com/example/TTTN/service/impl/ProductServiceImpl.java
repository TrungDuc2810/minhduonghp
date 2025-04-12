package com.example.TTTN.service.impl;

import com.example.TTTN.entity.*;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.ProductDto;
import com.example.TTTN.repository.*;
import com.example.TTTN.service.ProductService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ProductTypeRepository productTypeRepository;
    private final ProductUnitRepository productUnitRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseProductRepository warehouseProductRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              ModelMapper modelMapper,
                              ProductTypeRepository productTypeRepository,
                              ProductUnitRepository productUnitRepository, WarehouseRepository warehouseRepository, WarehouseProductRepository warehouseProductRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.productTypeRepository = productTypeRepository;
        this.productUnitRepository = productUnitRepository;
        this.warehouseRepository = warehouseRepository;
        this.warehouseProductRepository = warehouseProductRepository;
    }

    private ProductDto mapToDto(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }

    private Product mapToEntity(ProductDto productDto) {
        return modelMapper.map(productDto, Product.class);
    }

    @Override
    @Transactional
    public ProductDto addProduct(ProductDto productDto) {
        Product product = mapToEntity(productDto);
        product = productRepository.save(product);

        List<Warehouse> warehouses = warehouseRepository.findAll();

        if (!warehouses.isEmpty()) {
            List<WarehouseProduct> warehouseProducts = new ArrayList<>();

            for (Warehouse warehouse : warehouses) {
                WarehouseProduct warehouseProduct = new WarehouseProduct();
                warehouseProduct.setWarehouse(warehouse);
                warehouseProduct.setProduct(product);
                warehouseProduct.setQuantity(0);
                warehouseProducts.add(warehouseProduct);
            }

            warehouseProductRepository.saveAll(warehouseProducts);
        }

        return mapToDto(product);
    }


    @Override
    public ListResponse<ProductDto> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> products = productRepository.findAll(pageRequest);

        List<Product> listOfProducts = products.getContent();

        List<ProductDto> content = listOfProducts.stream().map(this::mapToDto).toList();

        ListResponse<ProductDto> productResponse = new ListResponse<>();
        productResponse.setContent(content);
        productResponse.setPageNo(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements((int)products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }

    @Override
    public ProductDto getProductById(long id) {
        Product product = productRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Product", "id", String.valueOf(id)));

        return mapToDto(product);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto productDto, long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()
                -> new ResourceNotFoundException("Product", "id", String.valueOf(productId)));

        ProductType productType = productTypeRepository.findById(productDto.getProductTypeId()).orElseThrow(() ->
                new ResourceNotFoundException("Product type", "id", String.valueOf(productDto.getProductTypeId())));

        ProductUnit productUnit = productUnitRepository.findById(productDto.getProductUnitId()).orElseThrow(() ->
                new ResourceNotFoundException("Product unit", "id", String.valueOf(productDto.getProductUnitId())));

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setProductType(productType);
        product.setProductUnit(productUnit);

        Product updatedProduct = productRepository.save(product);

        return mapToDto(updatedProduct);
    }

    @Override
    public void deleteProductById(long id) {
        Product product = productRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Product", "id", String.valueOf(id)));
        productRepository.delete(product);
    }
}
