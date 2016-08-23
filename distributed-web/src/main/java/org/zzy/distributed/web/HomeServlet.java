package org.zzy.distributed.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * Created by zhaoyu on 16-8-23.
 */
@WebServlet(name = "HomeServlet", urlPatterns = "/init", loadOnStartup = -1)
public class HomeServlet extends HttpServlet {

    public HomeServlet() {
        System.out.println("servlet init!!!");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("Hello World!!!");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getSession();

        response.getWriter().println("Hello World!!!");
        PrintWriter writer = response.getWriter();
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request);

        Enumeration<String> headers = requestWrapper.getHeaderNames();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            writer.println(header + " \t\t" + requestWrapper.getHeader(header));
        }

        writer.println("authtype" + "\t\t" + requestWrapper.getAuthType());
        writer.println("getContextPath" + "\t\t" + requestWrapper.getContextPath());
        writer.println("getPathInfo" + "\t\t" + requestWrapper.getPathInfo());
        writer.println("getMethod" + "\t\t" + requestWrapper.getMethod());

        request.getSession().invalidate();
        writer.close();
    }
}
