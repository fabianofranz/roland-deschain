package com.fabianofranz.deschain;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * Receives all events from Instagram Realtime API and proxies a search for the event details.
 *
 * @author Fabiano Franz
 */
@WebServlet(name = "DetailsServlet", urlPatterns = {"/details"})
public class DetailsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("application/json");
        res.setCharacterEncoding("utf-8");
        PrintWriter out = res.getWriter();
        
        String object = req.getParameter("object");
        
        HttpClient http = new HttpClient();
        GetMethod get = new GetMethod("https://api.instagram.com/v1/geographies/" + object + "/media/recent?client_id=490ee43313464481b03811d2753c7c48");
        http.executeMethod(get);
        String body = get.getResponseBodyAsString();
        get.releaseConnection();
        out.print(body);
        out.flush();
        out.close();
        
    }

}
