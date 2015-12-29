package com.teamdev.servlet;


import com.teamdev.chat.service.UserAuthenticationService;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.AccessControlException;

public class AuthFilter implements Filter{

    private ApplicationContextContainer applicationContextContainer = new ApplicationContextContainer();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        final UserAuthenticationService userAuthenticationService
                = applicationContextContainer.getApplicationContext().getBean(UserAuthenticationService.class);

        final String token = servletRequest.getParameter("token");
        long userId = -1;
        try {
            userId = Long.parseLong(servletRequest.getParameter("userid"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        try{
            userAuthenticationService.validateToken(userId, token);
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
