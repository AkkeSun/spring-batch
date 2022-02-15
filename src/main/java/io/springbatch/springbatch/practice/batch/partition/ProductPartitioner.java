package io.springbatch.springbatch.practice.batch.partition;

import io.springbatch.springbatch.practice.batch.domain.ProductVo;
import io.springbatch.springbatch.practice.batch.job.api.QueryGenerator;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import javax.sql.DataSource;
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
        List<ProductVo> productVoList= QueryGenerator.getProductList(dataSource);
        Map<String, ExecutionContext> result = new HashMap<>();

        int number = 0;

        for(int i=0; i<productVoList.size(); i++) {
            ExecutionContext value = new ExecutionContext();
            value.put("productVo", productVoList.get(i));

            result.put("partition" + number, value);
            number ++;
        }

        return result;
    }
}
