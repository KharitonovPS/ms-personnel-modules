package org.kharitonov.ms.person.module;

import org.kharitonov.ms.person.module.util.BindingResultMessageBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MSPersonApplication {
    public static void main(String[] args) {
        SpringApplication.run(MSPersonApplication.class, args);
    }


    @Bean
    public BindingResultMessageBuilder bindingResultMessageBuilder(){
        return new BindingResultMessageBuilder();
    }
}
