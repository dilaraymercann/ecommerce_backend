package com.bandage.ecommerce.service;

import com.bandage.ecommerce.dto.CategoryResponse;
import com.bandage.ecommerce.entity.Category;
import com.bandage.ecommerce.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(
            CategoryRepository categoryRepository
    ) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> getAllCategories() {

        return categoryRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CategoryResponse mapToResponse(
            Category category
    ) {

        return CategoryResponse
                .builder()
                .id(category.getId())
                .title(category.getTitle())
                .gender(category.getGender())
                .imgUrl(category.getImgUrl())
                .rating(category.getRating())
                .build();
    }
}