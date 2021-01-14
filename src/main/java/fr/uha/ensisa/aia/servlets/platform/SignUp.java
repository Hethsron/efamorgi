/**
 * Copyright © 2020  	Hethsron Jedaël BOUEYA
 * 						Omar CHICHAOUI
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package fr.uha.ensisa.aia.servlets.platform;
/**
 *		@file            	SignOut.java
 *      @details
 *
 *      @author          	Hethsron Jedaël BOUEYA (hethsron-jedael.boueya@uha.fr)
 *      					Omar CHICHAOUI (omar.chichaoui@uha.fr)
 *
 *      @version         	0.0.1
 *      @date            	December, 9th 2020
 *
 *      @Copyright       	GPLv3+ : GNU GPL version 3 or later
 *                       	Licencied Material - Property of Us®
 *                       	© 2020 ENSISA (UHA) - All rights reserved.
 */
import fr.uha.ensisa.aia.factory.UserFactory;
import fr.uha.ensisa.aia.model.User;
import fr.uha.ensisa.aia.res.Parameter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SignUp", urlPatterns = "/signup")
public class SignUp extends HttpServlet {

    private UserFactory factory = new UserFactory();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Gets values of request parameter as a String
        String email = req.getParameter(Parameter.EMAIL.getName());
        String firstname = req.getParameter(Parameter.FIRSTNAME.getName());
        String lastname = req.getParameter(Parameter.LASTNAME.getName());
        String password = req.getParameter(Parameter.PASSWORD.getName());

        // Check if parameters have not null references
        if (email != null && firstname != null && lastname != null && password != null) {
            // Save new user in the database
            factory.getDao().persist(new User(lastname, firstname, email, password));

            // Populate the database
            factory.getDao().populate();
        }

        resp.sendRedirect("/");
    }
}
