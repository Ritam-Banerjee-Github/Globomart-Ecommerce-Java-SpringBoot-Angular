package com.in.ecommerce.service.admin;

import com.in.ecommerce.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    List<CategoryDto> getAllCategories();

    String getCategoryNameById(Long id);
}
