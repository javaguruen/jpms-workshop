package com.jpmsworkshop.students;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/*
import com.google.common.base.Predicates;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;
 */

@SpringBootApplication
//@EnableSwagger2
public class SpringBootJpmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJpmsApplication.class, args);
    }
/*
    @Bean
    public Docket studentsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("students-api")
                .apiInfo(apiInfo())
                .select()
                .paths(studentsPaths())
                .build()
                .ignoredParameterTypes(ApiIgnore.class)
                .enableUrlTemplating(true);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Student registration API")
                .description("Maintaines student information")
                .termsOfServiceUrl("http://my.university.com")
                .license("Apache License Version 2.0")
                //.licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
                .version("1.0")
                .build();
    }

    private Predicate<String> studentsPaths() {
        return Predicates.or(PathSelectors.regex("/students.*"), PathSelectors.regex("/students"));
    }
*/
}
