package io.springbatch.springbatch.multiThread.partition;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * gridSize 만큼 Map<String, ExecutionContext> 를 리턴한다
 * executionContext Map 의 value 에 돌어가있는 ExecutionContext 는
 * slaveStep 에서 참조가능하다.
 * 보통 ExecutionContext 값을 where 조건으로 하여 쿼리를 가져온다.
 * 아래의 예제처럼 총 갯수를 나눠 min, max 값을 조건으로 쿼리를 가져오기도하고,
 * DB 컬럼에 구분자 (ex. gubun)를 줘서 쿼리를 가져오기도 한다.
 */
public class ColumnRangePartitioner implements Partitioner {

    private JdbcOperations jdbcTemplate;

    private String table;

    private String column;

    public void setTable(String table) {
        this.table = table;
    }
    public void setColumn(String column) {
        this.column = column;
    }
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        int min = jdbcTemplate.queryForObject("SELECT MIN(" + column + ") from " + table, Integer.class);
        int max = jdbcTemplate.queryForObject("SELECT MAX(" + column + ") from " + table, Integer.class);
        int targetSize = (max - min) / gridSize + 1;

        Map<String, ExecutionContext> result = new HashMap<>();
        int number = 0;
        int start = min;
        int end = start + targetSize - 1;

        System.err.println(">> ColumnRangePartitioner");
        System.err.println(">> MIN : " + min + " || MAX : " + max + " || targetSize : " + targetSize);

        while (start <= max) {
            ExecutionContext value = new ExecutionContext();

            if (end >= max) {
                end = max;
            }

            // executionContext 에 담으면 바로 DB에 저장된다
            value.putInt("minValue", start);
            value.putInt("maxValue", end);

            result.put("partition" + number, value);

            start += targetSize;
            end += targetSize;
            number++;
        }

        return result;
    }}