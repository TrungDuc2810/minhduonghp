package com.example.TTTN.controller;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.PostDto;
import com.example.TTTN.payload.ProductDto;
import com.example.TTTN.service.ProductService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

//    @PreAuthorize("hasRole('ADMIN_K')")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ProductDto> createPost(@RequestPart("product") ProductDto productDto,
                                              @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail) {
        try {
            if (thumbnail != null && !thumbnail.isEmpty()) {
                String thumbnailPath = uploadThumbnail(thumbnail);
                productDto.setThumbnail(thumbnailPath);
            }
            return new ResponseEntity<>(productService.addProduct(productDto), HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String uploadThumbnail(MultipartFile file) throws IOException {
        String uploadDir = "D://OneDrive//frontend//public//";
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        String fileName = file.getOriginalFilename();
        String filePath = uploadDir + fileName;

        File destinationFile = new File(filePath);
        file.transferTo(destinationFile);

        return fileName;
    }

    @GetMapping
    public ListResponse<ProductDto> getAllProducts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return productService.getAllProducts(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable(name = "id") long id) {
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

//    @PreAuthorize("hasRole('ADMIN_K')")
    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable(name = "id") long productId,
            @RequestPart("product") ProductDto productDto,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail
    ) {
        try {
            if (thumbnail != null && !thumbnail.isEmpty()) {
                productDto.setThumbnail(uploadThumbnail(thumbnail));
            }
            ProductDto updatedProduct = productService.updateProduct(productId, productDto);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PreAuthorize("hasRole('ADMIN_K')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable(name = "id") long id) {
        productService.deleteProductById(id);
        return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
    }


}
