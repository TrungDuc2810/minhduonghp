package com.example.TTTN.service.impl;

import com.example.TTTN.entity.Product;
import com.example.TTTN.entity.ProductType;
import com.example.TTTN.entity.ProductUnit;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.ProductDto;
import com.example.TTTN.repository.ProductRepository;
import com.example.TTTN.repository.ProductTypeRepository;
import com.example.TTTN.repository.ProductUnitRepository;
import com.example.TTTN.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ProductUnitRepository productUnitRepository;
    private final ModelMapper mapper;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper mapper, ProductTypeRepository productTypeRepository, ProductUnitRepository productUnitRepository) {
        this.productRepository = productRepository;
        this.mapper = mapper;
        this.productTypeRepository = productTypeRepository;
        this.productUnitRepository = productUnitRepository;
    }

    private ProductDto mapToDto(Product product) {
        return mapper.map(product, ProductDto.class);
    }

    private Product mapToProduct(ProductDto productDto) {
        return mapper.map(productDto, Product.class);
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        ProductType productType = productTypeRepository.findById(productDto.getProductTypeId()).orElseThrow(() ->
                new ResourceNotFoundException("Product type", "id", String.valueOf(productDto.getProductTypeId())));

        ProductUnit productUnit = productUnitRepository.findById(productDto.getProductUnitId()).orElseThrow(() ->
                new ResourceNotFoundException("Product unit", "id", String.valueOf(productDto.getProductUnitId())));

        Product product = mapToProduct(productDto);
        product.setProductType(productType);
        product.setProductUnit(productUnit);

        Product newProduct = productRepository.save(product);

        return mapToDto(newProduct);
    }

    @Override
    public ListResponse<ProductDto> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductDto> content = products.getContent().stream().map(this::mapToDto).collect(Collectors.toList());

        ListResponse<ProductDto> productResponse = new ListResponse<>();
        productResponse.setContent(content);
        productResponse.setPageNo(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements((int) products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Product", "id", String.valueOf(id)));
        return mapToDto(product);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, Long id) {
        Product product = productRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Product", "id", String.valueOf(id)));

        ProductType productType = productTypeRepository.findById(productDto.getProductTypeId()).orElseThrow(() ->
                new ResourceNotFoundException("Product type", "id", String.valueOf(productDto.getProductTypeId())));

        ProductUnit productUnit = productUnitRepository.findById(productDto.getProductUnitId()).orElseThrow(() ->
                new ResourceNotFoundException("Product unit", "id", String.valueOf(productDto.getProductUnitId())));

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setProductType(productType);
        product.setProductUnit(productUnit);

        Product updatedProduct = productRepository.save(product);

        return mapToDto(updatedProduct);
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()
            -> new ResourceNotFoundException("Product", "id", String.valueOf(id)));
        productRepository.delete(product);
    }
}
