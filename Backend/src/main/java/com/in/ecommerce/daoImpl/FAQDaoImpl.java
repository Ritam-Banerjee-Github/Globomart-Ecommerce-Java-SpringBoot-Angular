package com.in.ecommerce.daoImpl;

import com.in.ecommerce.dto.CategoryDto;
import com.in.ecommerce.dto.FAQDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class FAQDaoImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<FAQDto> findAllByProductId(Long productId) {
        List<FAQDto> list=new ArrayList<>();
        String sql="select * from FAQ where productid=?";

        List<Map<String,Object>> rows=this.jdbcTemplate.queryForList(sql,productId);
        for(Map<String,Object> row:rows){
            FAQDto faqDto=new FAQDto();
            faqDto.setId((Long) row.get("id"));
            faqDto.setQuestion((String) row.get("question"));
            faqDto.setAnswer((String) row.get("answer"));
            faqDto.setProductId(productId);

            list.add(faqDto);
        }
        return list;
    }
}
