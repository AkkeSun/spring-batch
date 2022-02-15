package io.springbatch.springbatch.practice.batch.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponseVo {
    private int status;
    private String message;
}
