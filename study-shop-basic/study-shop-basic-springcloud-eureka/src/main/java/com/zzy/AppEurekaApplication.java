package com.zzy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by Sheep on 2019/11/6.
 */

@SpringBootApplication
@EnableEurekaServer
public class AppEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppEurekaApplication.class,args);
    }

}
