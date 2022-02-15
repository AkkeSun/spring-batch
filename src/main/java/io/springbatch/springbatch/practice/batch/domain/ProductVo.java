package io.springbatch.springbatch.practice.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor
@NoArgsConstructor
public class ProductVo {

    private Long id;
    private String name;
    private int price;
    private String type;

}
