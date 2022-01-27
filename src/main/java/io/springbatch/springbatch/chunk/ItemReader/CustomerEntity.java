package io.springbatch.springbatch.chunk.ItemReader;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class CustomerEntity {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private int age;
    private String year;

}
