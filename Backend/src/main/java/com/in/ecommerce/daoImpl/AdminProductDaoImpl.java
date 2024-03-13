package com.in.ecommerce.daoImpl;

import com.in.ecommerce.dto.FAQDto;
import com.in.ecommerce.dto.ProductDto;
import com.in.ecommerce.service.admin.CategoryService;
import com.in.ecommerce.wrapper.ProductWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;
import java.util.Map;

@Repository
@Slf4j

public class AdminProductDaoImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CategoryService categoryService;

    public ProductDto addProduct(ProductDto productDto) throws IOException {
        String sql="insert into product(name,description,price,image,category_id) values(?,?,?,?,?)";
        int insertedRowCount=this.jdbcTemplate.update(sql,productDto.getName(),productDto.getDescription(),
                productDto.getPrice(),productDto.getImage().getBytes(),productDto.getCategoryId());
        log.info("Number of new Product inserted -> "+insertedRowCount);
        Long id=this.findProductIdByName(productDto.getName());
        log.info("New Product Id is -> "+id);
        productDto.setId(id);
        return productDto;
    }

    public Long findProductIdByName(String name) {
        String sql="select id from product where name=?";
        return this.jdbcTemplate.queryForObject(sql,Long.class,name);
    }

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

    public int deleteProductById(Long productId) {
        String sql="delete from product where id=?";
        return this.jdbcTemplate.update(sql,productId);
    }

    public void postFAQ(FAQDto faqDto) {
        String sql="insert into FAQ(question,answer,productid) values(?,?,?)";
        int insertedRowCOunt=this.jdbcTemplate.update(sql,faqDto.getQuestion(),faqDto.getAnswer(),faqDto.getProductId());
    }

    public Long findIdByQuestionandAnswerandProductId(FAQDto faqDto) {
        String sql="select id from FAQ where question=? and answer=? and productid=?";
        return this.jdbcTemplate.queryForObject(sql,Long.class,faqDto.getQuestion(),faqDto.getAnswer(),faqDto.getProductId());
    }

    public void updateProductById(Long productId, ProductDto productDtoNew) throws IOException {
        if(productDtoNew.getImage()!=null) {
            String sql = "update product set name=?,description=?,price=?,image=?,category_id=? where id=?";
            this.jdbcTemplate.update(sql, productDtoNew.getName(), productDtoNew.getDescription(), productDtoNew.getPrice(),
                    productDtoNew.getImage().getBytes(), productDtoNew.getCategoryId(), productId);
        }else{
            String sql = "update product set name=?,description=?,price=?,category_id=? where id=?";
            this.jdbcTemplate.update(sql, productDtoNew.getName(), productDtoNew.getDescription(), productDtoNew.getPrice(),
                    productDtoNew.getCategoryId(), productId);
        }
    }
}
