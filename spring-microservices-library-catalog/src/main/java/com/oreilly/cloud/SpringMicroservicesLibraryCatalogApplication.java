package com.oreilly.cloud;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
@EnableEurekaClient
@EnableHystrix
@EnableHystrixDashboard
public class SpringMicroservicesLibraryCatalogApplication {

    @Value("${catalog.size}")
    private int size;

    @Bean
    public RestTemplate restTemplate(){
        return  new RestTemplate();
    }

    @RequestMapping("/catalog")
    @CrossOrigin
    @HystrixCommand(fallbackMethod = "failover")
    public List<Book> getCatalog()  {
        return this.restTemplate().getForObject("http://localhost:5005/realcatalog",List.class);
    }


    public List<Book> failover() {
        List<Book> base = new ArrayList<>();
        base.add(new Book(2000, "Clean Code", "Robert C.", "Martin", 150, "Tech"));
        return base;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringMicroservicesLibraryCatalogApplication.class, args);
    }
}
