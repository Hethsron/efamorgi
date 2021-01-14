package fr.uha.ensisa.aia.servlets.platform;

import fr.uha.ensisa.aia.res.Parameter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LogOut", urlPatterns = "/logout")
public class LogOut extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Check if the object bounds with the session is not null
        if (req.getSession().getAttribute(Parameter.FIRSTNAME.getName()) != null && req.getSession().getAttribute(Parameter.LASTNAME.getName())!= null) {
            // Bind an object to this session
            req.getSession().setAttribute(Parameter.LOGOUT.getName(), Parameter.LOGOUT.getName());

            // Send a temporary redirect response to the client
            resp.sendRedirect("/");
        }
        else {
            // Display 403 page
            this.getServletContext().getRequestDispatcher("/WEB-INF/403.jsp").forward(req, resp);
        }
    }
}
