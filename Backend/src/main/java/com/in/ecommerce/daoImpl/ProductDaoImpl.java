package com.in.ecommerce.daoImpl;

import com.in.ecommerce.dto.User;
import com.in.ecommerce.enums.UserRole;
import com.in.ecommerce.wrapper.ProductWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@Slf4j
public class ProductDaoImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AdminCategoryDaoImpl adminCategoryDao;

    public Optional<ProductWrapper> findById(Long productId) {
        try {
            String sql = "select * from product where id=?";

            ProductWrapper productWrapper = this.jdbcTemplate.queryForObject(sql, new RowMapper<ProductWrapper>() {
                @Override
                public ProductWrapper mapRow(ResultSet rs, int rowNum) throws SQLException {
                    ProductWrapper productWrapper1=new ProductWrapper();
                    productWrapper1.setId(rs.getLong(1));
                    productWrapper1.setName(rs.getString(2));
                    productWrapper1.setDescription(rs.getString(3));
                    productWrapper1.setPrice(rs.getLong(4));
                    productWrapper1.setByteImage(rs.getBytes(5));
                    productWrapper1.setCategoryId(rs.getLong(6));
                    productWrapper1.setCategoryName(adminCategoryDao.getCategoryNameById(rs.getLong(6)));

                    return productWrapper1;
                }
            }, productId);

            return Optional.ofNullable(productWrapper);

        }catch (EmptyResultDataAccessException ex){
            log.info("No existing product with productId detected.");
        }

        return null;
    }
}
