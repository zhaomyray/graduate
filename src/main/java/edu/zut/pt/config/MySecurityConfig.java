//package edu.zut.pt.config;
//
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
///**
// * 1、在pom中引入SpringSecurity模块
// *
// * 2、编写SpringSecurity的配置类
// *
// * 3、控制请求的访问权限
// *
// */
//@EnableWebSecurity
//public class MySecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
////        super.configure(http);
//        //定制请求的授权规则
//        http.authorizeRequests().antMatchers("/").permitAll()
//        .antMatchers("/stu/**").hasRole("stu")
//        .antMatchers("/tea_school/**").hasRole("tea_school")
//        .antMatchers("/tea_company/**").hasRole("tea_company");
//
//        //开启自动配置的登录功能   效果：如果没有登录，没有登录权限就会来到登录页面
//        http.formLogin().usernameParameter("stu_username").passwordParameter("stu_psd").loginPage("/login");
//        //1、/login来到登录页
//        //2、重定向到/login?error表示登录失败
//        //3、更多详细规则
//        //4、默认post形式的/login代表处理登录
//        //5、一旦定制loginPage，那么loginPage的post请求就是登录
//
//        //开启自动配置的注销功能
//        http.logout();
//        //1、访问/logout()表示用户注销，清空session
//
//        //开启记住我功能
//        http.rememberMe();
//        //登录成功以后，将cookie发给浏览器保存，以后访问页面带上这个cookie，只要通过检查就可以登录
//        //如果点击注销，也会删除这个cookie
//    }
//
//    //定义认证规则
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////        super.configure(auth);
//        auth.inMemoryAuthentication().withUser("201560240201").password("123456").roles("stu");
////        .and()
////        .withUser("2015601").password("2015601").roles("tea_school")
////        .and()
////        .withUser("22341").password("22341").roles("tea_company");
//
//    }
//}
