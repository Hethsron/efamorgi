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
 *		@file            	Main.java
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
import fr.uha.ensisa.aia.res.Mime;
import fr.uha.ensisa.aia.res.Parameter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Optional;

@WebServlet(name = "Main", urlPatterns = "/")
public class Main extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Check if the object bounds with the session is not null
        if (req.getSession().getAttribute(Parameter.LOGOUT.getName()) != null) {
            // Invalidate this session then unbinds any objects bound to it
            req.getSession().invalidate();
        }

        final String uri = req.getRequestURI();
        if (uri.equalsIgnoreCase("/") || uri.equalsIgnoreCase("/#")) {
            // Display Welcome page
            this.getServletContext().getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
        }
        else {
            // Check if file exists and is not a directory
            File file = new File("src/main/webapp/META-INF/assets" + uri);
            if (file.exists() && !file.isDirectory()) {
                // Find MIME Type
                Optional<Mime> mime = Mime.get(uri);
                switch (mime.get()) {
                    case CSS: {
                        // Sets Content Type
                        resp.setContentType(Mime.CSS.getType());
                        break;
                    }
                    case ICON: {
                        // Sets Content Type
                        resp.setContentType(Mime.ICON.getType());
                        break;
                    }
                    case JAVASCRIPT: {
                        // Sets Content Type
                        resp.setContentType(Mime.JAVASCRIPT.getType());
                        break;
                    }
                    case TTF: {
                        // Sets Content Type
                        resp.setContentType(Mime.TTF.getType());
                        break;
                    }
                    case WOFF: {
                        // Sets Content Type
                        resp.setContentType(Mime.WOFF.getType());
                        break;
                    }
                    case WOFF2: {
                        // Sets Content Type
                        resp.setContentType(Mime.WOFF2.getType());
                        break;
                    }
                    default: {
                        // TODO: NOTHING
                        break;
                    }
                }

                // Creates buffered input and output streams to respectively write or read data
                BufferedInputStream input = null;

                try {
                    // Initialize these buffered input and output streams
                    BufferedOutputStream output = new BufferedOutputStream(resp.getOutputStream());
                    input = new BufferedInputStream(new FileInputStream(file));

                    // Sets default byte value
                    int bytes = 0;

                    // While there are bytes to read from buffered input stream
                    while ((bytes = input.read()) != -1) {
                        // Write bytes to this buffered output stream
                        output.write(bytes);
                    }
                }
                catch (IOException e) {
                    // Display the detail message of this Throwable
                    System.err.println(e.getMessage());
                }
                finally {
                    // Check if reference is not null
                    if (input != null) {
                        // Closes this input stream and release any system resources
                        input.close();
                    }
                }
            }
            else {
                // Display 404 page
                this.getServletContext().getRequestDispatcher("/WEB-INF/404.jsp").forward(req, resp);
            }
        }
    }
}
