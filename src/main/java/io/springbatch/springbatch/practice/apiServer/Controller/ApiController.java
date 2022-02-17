package io.springbatch.springbatch.practice.apiServer.Controller;

import io.springbatch.springbatch.practice.batch.domain.ApiInfo;
import io.springbatch.springbatch.practice.batch.domain.ProductVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
public class ApiController {

    @PostMapping("/1")
    public String product1Server(@RequestBody ApiInfo apiInfo) {
        List<ProductVo> collect = apiInfo.getApiRequestList().stream().map(item -> item.getProductVo()).collect(Collectors.toList());
        System.out.println(collect);

        return "product1 processing success";
    }

    @PostMapping("/2")
    public String product2Server(@RequestBody ApiInfo apiInfo) {
        List<ProductVo> collect = apiInfo.getApiRequestList().stream().map(item -> item.getProductVo()).collect(Collectors.toList());
        System.out.println(collect);

        return "product2 processing success";
    }

    @PostMapping("/3")
    public String product3Server(@RequestBody ApiInfo apiInfo) {
        List<ProductVo> collect = apiInfo.getApiRequestList().stream().map(item -> item.getProductVo()).collect(Collectors.toList());
        System.out.println(collect);

        return "product3 processing success";
    }

    @PostMapping("/4")
    public String product4Server(@RequestBody ApiInfo apiInfo) {
        List<ProductVo> collect = apiInfo.getApiRequestList().stream().map(item -> item.getProductVo()).collect(Collectors.toList());
        System.out.println(collect);

        return "product4 processing success";
    }

    @PostMapping("/5")
    public String product5Server(@RequestBody ApiInfo apiInfo) {
        List<ProductVo> collect = apiInfo.getApiRequestList().stream().map(item -> item.getProductVo()).collect(Collectors.toList());
        System.out.println(collect);

        return "product5 processing success";
    }
}
