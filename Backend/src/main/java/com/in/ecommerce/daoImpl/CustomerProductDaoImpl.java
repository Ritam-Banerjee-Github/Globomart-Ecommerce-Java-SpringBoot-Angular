package com.in.ecommerce.daoImpl;

import com.in.ecommerce.service.admin.CategoryService;
import com.in.ecommerce.wrapper.ProductWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class CustomerProductDaoImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CategoryService categoryService;


    public List<ProductWrapper> getAllProducts() {
        List<ProductWrapper> list=new ArrayList<>();
        String sql="select * from product";

        List<Map<String,Object>> rows=this.jdbcTemplate.queryForList(sql);
        for(Map<String,Object> row:rows){
            ProductWrapper productWrapper=new ProductWrapper();
            productWrapper.setId((Long) row.get("id"));
            productWrapper.setName((String) row.get("name"));
            productWrapper.setDescription((String) row.get("description"));
            productWrapper.setPrice((Long) row.get("price"));
            productWrapper.setByteImage((byte[]) row.get("image"));
            productWrapper.setCategoryId((Long) row.get("category_id"));
            String categoryName=categoryService.getCategoryNameById((Long) row.get("category_id"));
            productWrapper.setCategoryName(categoryName);

            list.add(productWrapper);
        }
        return list;
    }

    public List<ProductWrapper> getAllProductsByName(String name) {
        List<ProductWrapper> list=new ArrayList<>();
        String sql="select * from product where name ilike ?";
        String param="%"+name+"%";

        List<Map<String,Object>> rows=this.jdbcTemplate.queryForList(sql,param);
        for(Map<String,Object> row:rows){
            ProductWrapper productWrapper=new ProductWrapper();
            productWrapper.setId((Long) row.get("id"));
            productWrapper.setName((String) row.get("name"));
            productWrapper.setDescription((String) row.get("description"));
            productWrapper.setPrice((Long) row.get("price"));
            productWrapper.setByteImage((byte[]) row.get("image"));
            productWrapper.setCategoryId((Long) row.get("category_id"));
            String categoryName=categoryService.getCategoryNameById((Long) row.get("category_id"));
            productWrapper.setCategoryName(categoryName);

            list.add(productWrapper);
        }
        return list;
    }
}
