package io.springbatch.springbatch.chunk.itemWriter.db.jpa;


import io.springbatch.springbatch.chunk.ItemReader.db.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerEntityBackupRepository extends JpaRepository<CustomerEntityBackup, Integer> {
}
