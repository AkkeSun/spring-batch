package io.springbatch.springbatch.practice.batch.rowMapper;

import io.springbatch.springbatch.practice.batch.domain.ProductVo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ProductRowMapper implements RowMapper<ProductVo> {

    @Override
    public ProductVo mapRow(ResultSet resultSet, int i) throws SQLException {
        return ProductVo.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .price(resultSet.getInt("price"))
                .type(resultSet.getString("type"))
                .build();
    }
}
