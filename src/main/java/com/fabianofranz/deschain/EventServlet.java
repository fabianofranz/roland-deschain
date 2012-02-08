package com.fabianofranz.wherestagram;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

/**
 * Comet servlet to handle conversation with the web browser.
 *
 * @author Fabiano Franz
 */
@WebServlet(name = "EventServlet", value = "/event")
public class EventServlet extends HttpServlet {

    static private final Logger log = Logger.getLogger(EventServlet.class.getName());
    static private final String HUB_MODE_SUBSCRIPTION = "subscribe";

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = res.getWriter();

        String mode = req.getParameter("hub.mode");
        String challenge = req.getParameter("hub.challenge");
        String verifyToken = req.getParameter("hub.verify_token");

        if (HUB_MODE_SUBSCRIPTION.equals(mode)) {
            log.log(Level.INFO, "Hub mode subscription received: challenge {0} and verification token {1}.", new Object[]{challenge, verifyToken});
            out.print(challenge);
        } else {
            log.log(Level.INFO, "Unsupported hub mode received: {0}.", new Object[]{mode});
            out.print("Hub mode unsupported.");
        }

        out.flush();
        out.close();


    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();

        String message = IOUtils.toString(req.getInputStream());

        Events.instance().add(message);

        out.close();
    }
    
}
