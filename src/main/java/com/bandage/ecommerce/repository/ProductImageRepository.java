package com.bandage.ecommerce.repository;

import com.bandage.ecommerce.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository
        extends JpaRepository<ProductImage, Long> {
}