package com.example.security.config;

import com.example.security.service.UserService.SecurityContextOperatorResolver;
import com.example.security.web.HandlerRequireOperatorMethodArgumentResolver;
import com.example.security.web.HandlerSnakeCaseGetMethodArgumentResolver;
import com.spring4all.swagger.EnableSwagger2Doc;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@EnableSwagger2Doc
@Slf4j
public class WebConfig extends WebMvcConfigurerAdapter {

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    super.addArgumentResolvers(argumentResolvers);
    argumentResolvers.add(new HandlerSnakeCaseGetMethodArgumentResolver());
    log.debug("注册参数处理器:{}", HandlerSnakeCaseGetMethodArgumentResolver.class.getName());

    HandlerRequireOperatorMethodArgumentResolver operatorMethodArgumentResolver = new HandlerRequireOperatorMethodArgumentResolver();
    operatorMethodArgumentResolver.setOperatorResolver(new SecurityContextOperatorResolver());
    argumentResolvers.add(operatorMethodArgumentResolver);
    log.debug("注册参数处理器:{}->{}", HandlerRequireOperatorMethodArgumentResolver.class.getName(),
        SecurityContextOperatorResolver.class.getName());
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");

    registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");

    registry.addResourceHandler("/static/**")
        .addResourceLocations("classpath:/static/");
    log.debug("注册静态资源解析器:{}", "/static/**");
  }
}
