package com.teamdev.restservlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author vladislav.kovchug
 */
public class RestServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        System.out.println("I was initialized");
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        System.out.println("GET request happened");
    }
}
