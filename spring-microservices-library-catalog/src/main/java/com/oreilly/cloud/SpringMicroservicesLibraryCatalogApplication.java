package com.oreilly.cloud;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import freemarker.core.ReturnInstruction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
@Slf4j

@RefreshScope
@EnableEurekaClient
//Monitoring and Health check
@EnableHystrix
@EnableHystrixDashboard

//Security
@EnableAuthorizationServer
@EnableResourceServer
//@EnableGlobalMethodSecurity(prePostEnabled=true)
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

        return this.restTemplate().getForObject("http://localhost:5005/realcatalog?size="+size,List.class);
    }
    @RequestMapping("/catalog/book")
    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity addBook(@RequestBody @Valid Book book){
        return ResponseEntity.ok().build();
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
