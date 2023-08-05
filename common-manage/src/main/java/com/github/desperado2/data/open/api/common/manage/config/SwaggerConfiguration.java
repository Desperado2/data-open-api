/*
 * <<
 *  Davinci
 *  ==
 *  Copyright (C) 2016 - 2019 EDP
 *  ==
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  >>
 *
 */

package com.github.desperado2.data.open.api.common.manage.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.google.common.base.Predicates;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
@ConditionalOnProperty(value = "open.data.platform.base.open-swagger", havingValue = "true", matchIfMissing = false)
public class SwaggerConfiguration {
    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("open data platform api")
                .version("1.0")
                .build();
    }

    @Bean("平台接口模块")
    public Docket platform() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("平台接口模块")
                .select()
                .apis(Predicates.or(
                        RequestHandlerSelectors.basePackage("com.github.desperado2.open.data.platform.api.manage.controller"),
                        RequestHandlerSelectors.basePackage("com.github.desperado2.open.data.platform.authentication.manage.controller"),
                        RequestHandlerSelectors.basePackage("com.github.desperado2.open.data.platform.controller"),
                        RequestHandlerSelectors.basePackage("com.github.desperado2.open.data.platform.log.manage.controller"),
                        RequestHandlerSelectors.basePackage("com.github.desperado2.open.data.platform.datasource.manage.controller"),
                        RequestHandlerSelectors.basePackage("com.github.desperado2.open.data.platform.user.manage.controller"))
                )
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }


    @Bean("对外接口模块")
    public Docket open() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("对外接口模块")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.github.desperado2.open.data.platform.news.search.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

}
