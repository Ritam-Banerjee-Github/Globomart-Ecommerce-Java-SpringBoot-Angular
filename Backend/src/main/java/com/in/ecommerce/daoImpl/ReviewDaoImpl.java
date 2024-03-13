package com.in.ecommerce.daoImpl;

import com.in.ecommerce.dto.CategoryDto;
import com.in.ecommerce.dto.ReviewDto;
import com.in.ecommerce.wrapper.ReviewWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class ReviewDaoImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserDaoImpl userDaoImpl;


    public int giveReview(ReviewDto reviewDto) throws IOException {
        String sql="insert into review(rating,description,image,userid,productid) values(?,?,?,?,?)";
        return this.jdbcTemplate.update(sql,reviewDto.getRating(),reviewDto.getDescription(),reviewDto.getImg().getBytes(),
                reviewDto.getUserId(),reviewDto.getProductId());
    }

    public ReviewWrapper findByUserIdandProductId(ReviewDto reviewDto) {

        String sql="select * from review where userid=? and productid=? and rating=?";

        return this.jdbcTemplate.queryForObject(sql, new RowMapper<ReviewWrapper>() {
            @Override
            public ReviewWrapper mapRow(ResultSet rs, int rowNum) throws SQLException {
                ReviewWrapper reviewWrapper1=new ReviewWrapper();

                reviewWrapper1.setId(rs.getLong(1));
                reviewWrapper1.setRating(rs.getLong(2));
                reviewWrapper1.setDescription(rs.getString(3));
                reviewWrapper1.setReturnedImg(rs.getBytes(4));
                reviewWrapper1.setUserId(rs.getLong(5));
                reviewWrapper1.setProductId(rs.getLong(6));

                return  reviewWrapper1;
            }
        },reviewDto.getUserId(),reviewDto.getProductId(),reviewDto.getRating());
    }

    public List<ReviewWrapper> findAllByProductId(Long productId) {
        List<ReviewWrapper> list=new ArrayList<>();
        String sql="select * from review where productid=?";

        List<Map<String,Object>> rows=this.jdbcTemplate.queryForList(sql,productId);
        for(Map<String,Object> row:rows){
            ReviewWrapper reviewWrapper=new ReviewWrapper();
            reviewWrapper.setId((Long) row.get("id"));
            reviewWrapper.setRating((Long) row.get("rating"));
            reviewWrapper.setDescription((String) row.get("description"));
            reviewWrapper.setReturnedImg((byte[]) row.get("image"));
            reviewWrapper.setUserId((Long) row.get("userid"));
            reviewWrapper.setProductId(productId);

            reviewWrapper.setUserName(this.userDaoImpl.findById(reviewWrapper.getUserId()).get().getName());

            list.add(reviewWrapper);
        }
        return list;
    }
}
