package io.springbatch.springbatch.practice.batch.partition;

import io.springbatch.springbatch.practice.batch.domain.ProductVo;
import io.springbatch.springbatch.practice.batch.job.api.QueryGenerator;
import io.springbatch.springbatch.practice.batch.rowMapper.ProductRowMapper;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * gridSize 만큼  Map<String, ExecutionContext> 리턴
 * ExecutionContext 는 slaveStep() 에서 값을 참조하여 사용가능하다 (쿼리 조건문에 사용)
 */
public class ProductPartitioner implements Partitioner {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {

        // DB 데이터를 type 으로 묶어서 가져온다 -> type 을 기준으로 파티셔닝하기 위함
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String query = "select * from product group by type";
        ProductRowMapper productRowMapper = new ProductRowMapper();
        List<ProductVo> productVoList = jdbcTemplate.query(query, productRowMapper);
        Map<String, ExecutionContext> result = new HashMap<>();

        List<String> types = new ArrayList<>();

        int number = 0;

        for(int i=0; i<productVoList.size(); i++) {
            ExecutionContext value = new ExecutionContext();
            value.put("productVo", productVoList.get(i));
            result.put("partition" + number, value);

            types.add(productVoList.get(i).getType());

            number ++;
        }

        System.err.println("[Partitioner] size = " + productVoList.size() + " || type = " + types);

        return result;
    }






}
