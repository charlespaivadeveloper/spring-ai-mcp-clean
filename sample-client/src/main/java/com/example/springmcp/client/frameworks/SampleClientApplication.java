package com.example.springmcp.client.frameworks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.springmcp.client")
public class SampleClientApplication {

    public static void main(final String[] args) {
        SpringApplication.run(SampleClientApplication.class, args);
    }
}
