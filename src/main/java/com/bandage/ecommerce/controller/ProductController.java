package com.bandage.ecommerce.controller;

import com.bandage.ecommerce.dto.ProductListResponse;
import com.bandage.ecommerce.dto.ProductResponse;
import com.bandage.ecommerce.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(
            ProductService productService
    ) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ProductListResponse>
    getProducts(

            @RequestParam(
                    required = false
            )
            Long category,

            @RequestParam(
                    required = false
            )
            String filter,

            @RequestParam(
                    required = false
            )
            String sort,

            @RequestParam(
                    defaultValue = "25"
            )
            int limit,

            @RequestParam(
                    defaultValue = "0"
            )
            int offset
    ) {

        return ResponseEntity.ok(
                productService.getProducts(
                        category,
                        filter,
                        sort,
                        limit,
                        offset
                )
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse>
    getProduct(
            @PathVariable Long productId
    ) {

        return ResponseEntity.ok(
                productService.getProductById(
                        productId
                )
        );
    }
}