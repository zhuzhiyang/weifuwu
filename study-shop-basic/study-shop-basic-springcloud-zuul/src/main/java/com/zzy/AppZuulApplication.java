package com.zzy;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/**
 * Created by Sheep on 2019/11/6.
 */

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
@EnableSwagger2Doc
@EnableApolloConfig
public class AppZuulApplication {

    @ApolloConfig
    private Config appConfig;

    public static void main(String[] args) {
        SpringApplication.run(AppZuulApplication.class, args);

    }

    @Component
    @Primary
    class DocumentationConfig implements SwaggerResourcesProvider {
        public List<SwaggerResource> get() {
            // 开启监听，配置文件发生改变需要更改
            appConfig.addChangeListener(new ConfigChangeListener() {

                @Override
                public void onChange(ConfigChangeEvent changeEvent) {
                    get();
                }
            });
            return resources();
        }

        private List<SwaggerResource> resources() {

            List resources = new ArrayList<>();
            // app-itmayiedu-order
            // 网关使用服务别名获取远程服务的SwaggerApi
            String swaggerDocJson = swaggerDocument();
            JSONArray jsonArray = JSONArray.parseArray(swaggerDocJson);
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;
                String name = jsonObject.getString("name");
                String location = jsonObject.getString("location");
                String version = jsonObject.getString("version");
                resources.add(swaggerResource(name, location, version));
            }
            return resources;
        }

        /**
         * 获取swaggerDocument配置
         *
         * @return
         */
        private String swaggerDocument() {
            String property = appConfig.getProperty("zzy.zuul.swaggerDocument", "");
            return property;
        }

        private SwaggerResource swaggerResource(String name, String location, String version) {
            SwaggerResource swaggerResource = new SwaggerResource();
            swaggerResource.setName(name);
            swaggerResource.setLocation(location);
            swaggerResource.setSwaggerVersion(version);
            return swaggerResource;
        }
    }



}
