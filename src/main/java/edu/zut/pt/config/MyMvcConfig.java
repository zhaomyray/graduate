package edu.zut.pt.config;

import edu.zut.pt.component.LoginHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Configuration
public class MyMvcConfig extends WebMvcConfigurationSupport {

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        //浏览器发送/practiceTraining请求来到login
        registry.addViewController("/practiceTraining").setViewName("login");
        registry.addViewController("/stuMain.html").setViewName("stu_homePage");
        registry.addViewController("/stu_Main.html").setViewName("stu_homePage2");
        registry.addViewController("/teaSchoolMain.html").setViewName("tea_school_homePage");
        registry.addViewController("/teaCompanyMain.html").setViewName("tea_company_homePage");
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        super.addResourceHandlers(registry);
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");

    }


        @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        super.addInterceptors(registry);
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
        .excludePathPatterns("/practiceTraining","/login","/student/login","/teacher/login");

    }


}
