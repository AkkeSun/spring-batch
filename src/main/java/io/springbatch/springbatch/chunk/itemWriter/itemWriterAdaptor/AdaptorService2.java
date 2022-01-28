package io.springbatch.springbatch.chunk.itemWriter.itemWriterAdaptor;

import io.springbatch.springbatch.chunk.ItemReader.db.CustomerEntity;
import io.springbatch.springbatch.chunk.ItemReader.db.CustomerRepository;
import io.springbatch.springbatch.chunk.itemWriter.db.jpa.CustomerEntityBackup;
import io.springbatch.springbatch.chunk.itemWriter.db.jpa.CustomerEntityBackupRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdaptorService2 <T> {

    @Autowired
    private CustomerEntityBackupRepository repository;

    private ModelMapper mapper  = new ModelMapper();
    
    // 하나씩 아이템이 넘어온다
    public void saveMethod(CustomerEntity item){
        CustomerEntityBackup backupData = mapper.map(item, CustomerEntityBackup.class);
        CustomerEntityBackup save = repository.save(backupData);
        System.out.println(">>>> Backup Data : " + save);
    };

}
