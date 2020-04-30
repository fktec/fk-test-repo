package br.com.devops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"br.com.devops", "br.com.globosat.integration"})
public class DevOpsSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevOpsSyncApplication.class, args);
    }

}
