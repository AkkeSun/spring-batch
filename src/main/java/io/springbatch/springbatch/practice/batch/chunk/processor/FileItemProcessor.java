package io.springbatch.springbatch.practice.batch.chunk.processor;

import io.springbatch.springbatch.practice.batch.domain.ProductVo;
import io.springbatch.springbatch.practice.batch.domain.Product;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

public class FileItemProcessor implements ItemProcessor<ProductVo, Product> {

    @Override
    public Product process(ProductVo productVo) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        Product product = modelMapper.map(productVo, Product.class);
        return product;
    }
}
