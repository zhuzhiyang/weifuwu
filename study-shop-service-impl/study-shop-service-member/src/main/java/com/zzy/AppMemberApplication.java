package com.zzy;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created by Sheep on 2019/11/6.
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableSwagger2Doc
//@EnableApolloConfig
@MapperScan(basePackages = "com.zzy.mapper")
public class AppMemberApplication {

    public static void main(String[] args) {

        SpringApplication.run(AppMemberApplication.class,args);
    }


}
