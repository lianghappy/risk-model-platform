package com.cmcc.hy.model_platform;

import com.cmcc.hy.model_platform.jpmml.ModelCalc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class ModelPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModelPlatformApplication.class, args);

    }
}
