package io.springbatch.springbatch.chunk.itemWriter.db.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEntityBackup {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private int age;
    private String year;
}
