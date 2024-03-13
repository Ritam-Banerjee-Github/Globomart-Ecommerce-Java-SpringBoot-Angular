package com.in.ecommerce.daoImpl;

import com.in.ecommerce.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.*;

@Repository
@Service
@RequiredArgsConstructor
public class AdminCategoryDaoImpl {
    private final JdbcTemplate jdbcTemplate;


    public int createCategory(CategoryDto categoryDto) {
            String sql = "insert into category(name,description) values(?,?)";
            String description = categoryDto.getDescription();
            return this.jdbcTemplate.update(sql, categoryDto.getName(), description);

    }

    public Long findCategoryIdByName(String name) {
            String sql="select id from category where name=?";
            return this.jdbcTemplate.queryForObject(sql,Long.class,name);
    }

    public List<CategoryDto> getAllCategories() {
        List<CategoryDto> list=new ArrayList<>();
        String sql="select * from category";

        List<Map<String,Object>> rows=this.jdbcTemplate.queryForList(sql);
        for(Map<String,Object> row:rows){
            CategoryDto categoryDto=new CategoryDto();
            categoryDto.setId((Long) row.get("id"));
            categoryDto.setName((String) row.get("name"));
            categoryDto.setDescription((String) row.get("description"));

            list.add(categoryDto);
        }
       return list;
    }

    public String getCategoryNameById(Long id) {
        String sql="select name from category where id=?";
        return this.jdbcTemplate.queryForObject(sql,String.class,id);
    }
}
