package com.in.ecommerce.serviceImpl.admin;

import com.in.ecommerce.daoImpl.AdminCategoryDaoImpl;
import com.in.ecommerce.dto.CategoryDto;
import com.in.ecommerce.service.admin.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final AdminCategoryDaoImpl adminCategoryDaoImpl;
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        try {
            int insertedRowCount = adminCategoryDaoImpl.createCategory(categoryDto);
            log.info("Inserted New Category Row Count -> "+insertedRowCount);
            categoryDto.setId(adminCategoryDaoImpl.findCategoryIdByName(categoryDto.getName()));

            return categoryDto;
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        try {
            return adminCategoryDaoImpl.getAllCategories();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public String getCategoryNameById(Long id) {
        try{
            return adminCategoryDaoImpl.getCategoryNameById(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return "";
    }

}
