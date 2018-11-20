package com.oreilly.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
@EnableEurekaClient
public class RealCatalogApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealCatalogApplication.class, args);
    }

    @RequestMapping("/realcatalog")
    @CrossOrigin
    public List<Book> getCatalog(@RequestParam(name = "size", required = false, defaultValue = "2") int size) {
        return Book.getBooks().subList(0, size);
    }
}
