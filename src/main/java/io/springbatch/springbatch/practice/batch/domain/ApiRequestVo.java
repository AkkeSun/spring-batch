package io.springbatch.springbatch.practice.batch.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiRequestVo {

    private long id;
    private ProductVo productVo;

}
