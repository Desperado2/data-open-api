package com.github.desperado2.data.open.api.common.manage.controller;


import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * swagger控制器
 * @author tu nan
 * @date 2023/4/12
 **/
@Profile("!open.data.platform.base.open-swagger")
@RestController
public class DisableSwaggerUiController {

    @RequestMapping(value = "swagger-ui.html", method = RequestMethod.GET)
    public void getSwaggerUi(HttpServletResponse httpResponse) throws IOException {
        httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
    }


    @RequestMapping(value = "doc.html", method = RequestMethod.GET)
    public void getSwaggerDoc(HttpServletResponse httpResponse) throws IOException {
        httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
    }
}
