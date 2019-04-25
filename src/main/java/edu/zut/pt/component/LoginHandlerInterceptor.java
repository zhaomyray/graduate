package edu.zut.pt.component;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 登录拦截器
 */
public class LoginHandlerInterceptor implements HandlerInterceptor{

    /**
     * 登录检查
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //目标方法执行之前，做预检查
            Object stu = request.getSession().getAttribute("login");

                if(stu == null){
                    //未登录，返回登录页面
                    request.setAttribute("stuMsg","无权限，请先登录！");
                    request.getRequestDispatcher("/login").forward(request,response);
                    return false;
                }else{
                    //已登录，放行请求
                    return true;
                }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
