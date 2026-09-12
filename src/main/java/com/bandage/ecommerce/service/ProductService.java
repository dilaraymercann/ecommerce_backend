package com.bandage.ecommerce.service;

import com.bandage.ecommerce.dto.ProductImageResponse;
import com.bandage.ecommerce.dto.ProductListResponse;
import com.bandage.ecommerce.dto.ProductResponse;
import com.bandage.ecommerce.entity.Product;
import com.bandage.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(
            ProductRepository productRepository
    ) {
        this.productRepository = productRepository;
    }

    public ProductListResponse getProducts(
            Long category,
            String filter,
            String sort,
            int limit,
            int offset
    ) {

        Specification<Product> specification =
                (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        /*
         * CATEGORY FILTER
         */
        if (category != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("category").get("id"),
                                    category
                            )
            );
        }

        /*
         * TEXT FILTER
         */
        if (filter != null && !filter.isBlank()) {

            String searchText =
                    "%" + filter.toLowerCase() + "%";

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.or(
                                    criteriaBuilder.like(
                                            criteriaBuilder.lower(
                                                    root.get("name")
                                            ),
                                            searchText
                                    ),
                                    criteriaBuilder.like(
                                            criteriaBuilder.lower(
                                                    root.get("description")
                                            ),
                                            searchText
                                    )
                            )
            );
        }

        /*
         * SORT
         */
        Sort sorting = createSort(sort);

        /*
         * OFFSET / LIMIT
         *
         * Frontend:
         * offset=0, limit=25   -> page 0
         * offset=25, limit=25  -> page 1
         * offset=50, limit=25  -> page 2
         */
        int pageNumber = offset / limit;

        Pageable pageable =
                PageRequest.of(
                        pageNumber,
                        limit,
                        sorting
                );

        Page<Product> page =
                productRepository.findAll(
                        specification,
                        pageable
                );

        List<ProductResponse> products =
                page.getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        return ProductListResponse
                .builder()
                .total(page.getTotalElements())
                .products(products)
                .build();
    }

    public ProductResponse getProductById(
            Long productId
    ) {

        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "Product not found with id: "
                                                        + productId
                                        )
                        );

        return mapToResponse(product);
    }

    private Sort createSort(String sort) {

        if (sort == null || sort.isBlank()) {
            return Sort.unsorted();
        }

        return switch (sort) {

            case "price:asc" ->
                    Sort.by(
                            Sort.Direction.ASC,
                            "price"
                    );

            case "price:desc" ->
                    Sort.by(
                            Sort.Direction.DESC,
                            "price"
                    );

            case "rating:asc" ->
                    Sort.by(
                            Sort.Direction.ASC,
                            "rating"
                    );

            case "rating:desc" ->
                    Sort.by(
                            Sort.Direction.DESC,
                            "rating"
                    );

            default ->
                    Sort.unsorted();
        };
    }

    private ProductResponse mapToResponse(
            Product product
    ) {

        List<ProductImageResponse> images =
                product
                        .getImages()
                        .stream()
                        .map(image ->
                                ProductImageResponse
                                        .builder()
                                        .url(image.getUrl())
                                        .index(image.getIndex())
                                        .build()
                        )
                        .toList();

        return ProductResponse
                .builder()
                .id(product.getId())
                .name(product.getName())
                .description(
                        product.getDescription()
                )
                .price(product.getPrice())
                .stock(product.getStock())
                .storeId(product.getStoreId())
                .categoryId(
                        product
                                .getCategory()
                                .getId()
                )
                .rating(product.getRating())
                .sellCount(
                        product.getSellCount()
                )
                .images(images)
                .build();
    }
}