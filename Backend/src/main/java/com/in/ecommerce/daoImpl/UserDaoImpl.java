package com.in.ecommerce.daoImpl;

import com.in.ecommerce.dto.User;
import com.in.ecommerce.enums.UserRole;
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
public class UserDaoImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<User> findByUsername(String username) {
        try {
            String sql = "select * from users where email=?";

            User user = this.jdbcTemplate.queryForObject(sql, new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user=new User();
                     user.setId(rs.getLong(1));
                     user.setName(rs.getString(2));
                     user.setEmail(rs.getString(3));
                     user.setPassword(rs.getString(4));
                     user.setImage(rs.getBytes(5));
                     user.setRole(UserRole.valueOf(rs.getString(6)));

                    return user;
                }
            }, username);

            return Optional.ofNullable(user);

        }catch (EmptyResultDataAccessException ex){
            log.info("No existing user with username detected.");
        }

        return null;
    }

    public int createUser(User user) {
        String sql="insert into users(name,email,password,role) values(?,?,?,?)";
        log.info(user.getName());
        log.info(user.getEmail());
        log.info(user.getPassword());
        log.info(String.valueOf(user.getRole()));
        return this.jdbcTemplate.update(sql,user.getName(),user.getEmail(),user.getPassword(),String.valueOf(user.getRole()));
    }

    public Optional<User> findById(Long userId) {
        try {
            String sql = "select * from users where id=?";

            User user = this.jdbcTemplate.queryForObject(sql, new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user=new User();
                    user.setId(rs.getLong(1));
                    user.setName(rs.getString(2));
                    user.setEmail(rs.getString(3));
                    user.setPassword(rs.getString(4));
                    user.setImage(rs.getBytes(5));
                    user.setRole(UserRole.valueOf(rs.getString(6)));

                    return user;
                }
            }, userId);

            return Optional.ofNullable(user);

        }catch (EmptyResultDataAccessException ex){
            log.info("No existing user with given userId detected");
        }

        return null;
    }
}
