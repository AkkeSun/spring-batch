package io.springbatch.springbatch.practice.batch.job.api;

import io.springbatch.springbatch.practice.batch.domain.ProductVo;
import io.springbatch.springbatch.practice.batch.rowMapper.ProductRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryGenerator {

    public static List<ProductVo> getProductList(DataSource dataSource) {

        // -------- JdbcTemplate : 스프링부트에서 제공하는 JDBC 보조 클래스 -----------
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String query = "select type from product group by type";
        ProductRowMapper productRowMapper = new ProductRowMapper();

        List<ProductVo> productList = jdbcTemplate.query(query, productRowMapper);

        return productList;
    }

    // JdbcPagingItemReader.setParameterValues() 의 파라미터 타입으로 리턴하는함수
    public static Map<String, Object> getParameterForQuery(String parameter, String value){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(parameter, value);
        return parameters;
    }

}
