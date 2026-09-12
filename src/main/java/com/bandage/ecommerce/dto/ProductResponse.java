package com.bandage.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stock;

    @JsonProperty("store_id")
    private Long storeId;

    @JsonProperty("category_id")
    private Long categoryId;

    private Double rating;

    @JsonProperty("sell_count")
    private Integer sellCount;

    private List<ProductImageResponse> images;
}