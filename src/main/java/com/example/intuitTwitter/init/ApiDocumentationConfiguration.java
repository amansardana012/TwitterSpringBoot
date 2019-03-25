package com.example.intuitTwitter.init;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class ApiDocumentationConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(globalOperationParameters());
    }

    List<Parameter> globalOperationParameters(){
        final List<Parameter> parameterList = new ArrayList<>();
        final Parameter clientId = new ParameterBuilder()
                .name("clientId")
                .description("Who is calling")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(true)
                .build();

        final Parameter authToken = new ParameterBuilder()
                .name("Authorization")
                .description("Authorization token for given clientID")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(true)
                .build();
        parameterList.add(clientId);
        parameterList.add(authToken);

        return parameterList;
    }
}
