package com.teamdev.servlet;


import com.teamdev.chat.ContextConfiguration;
import com.teamdev.chat.service.UserAuthenticationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.AccessControlException;

public class AuthFilter implements Filter{

    private ApplicationContext applicationContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        applicationContext = new AnnotationConfigApplicationContext(ContextConfiguration.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        final UserAuthenticationService userAuthenticationService = applicationContext.getBean(UserAuthenticationService.class);

        final String token = servletRequest.getParameter("token");

        try{
            userAuthenticationService.checkUserLogged(token);
        } catch (AccessControlException e){
            response.sendError(403, "Forbidden");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
