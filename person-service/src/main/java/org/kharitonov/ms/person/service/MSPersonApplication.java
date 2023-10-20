package org.kharitonov.ms.person.service;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.kharitonov.ms.person.service.util.BindingResultMessageBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.BindingResult;

@SpringBootApplication
public class MSPersonApplication {
    public static void main(String[] args) {
        SpringApplication.run(MSPersonApplication.class, args);
    }


    @Bean
    public BindingResultMessageBuilder bindingResultMessageBuilder(){
        return new BindingResultMessageBuilder();
    }

    @Bean
    public Validator validatorBuilder(){
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }
}
