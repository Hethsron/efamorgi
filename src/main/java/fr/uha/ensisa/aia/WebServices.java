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
package fr.uha.ensisa.aia;
/**
 *		@file            	WebServices.java
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
import fr.uha.ensisa.aia.factory.ServiceFactory;
import fr.uha.ensisa.aia.model.Service;
import fr.uha.ensisa.aia.res.Mime;
import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(name = "WebServices", urlPatterns = "/services")
public class WebServices extends HttpServlet {

    private ServiceFactory factory = new ServiceFactory();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Sets the character encoding (MIME charset)
        resp.setCharacterEncoding("UTF-8");

        // Gets PrintWriter object that can send character to client
        PrintWriter out = resp.getWriter();

        // Gets MIME type of the body of the request
        final String type = req.getContentType();
        if (type == null || type.isEmpty()) {
            // Check if the database is empty
            if (factory.getDao().count() == 0) {
                // Sets the status code for this response.
                resp.setStatus(404);

                // Send HTML object to client
                out.println("{ error=\'There's no service\', status=\'HTTP/1.1 404\' }");
            }
            else {
                // Sets the status code for this response.
                resp.setStatus(200);
                for (Service s : factory.getDao().findAll()) {
                    // Send HTML object to client
                    out.println(s.toString());
                }
            }
        }
        else {
            // Find MIME Type
            Optional<Mime> mime = Mime.find(type);
            switch (mime.get()) {
                case JSON:
                    // Sets the content type of the response being sent to the client,
                    resp.setContentType(Mime.JSON.getType());

                    // Check if the database is empty
                    if (factory.getDao().count() == 0) {
                        // Sets the status code for this response.
                        resp.setStatus(404);

                        // Creates a JsonObject Builder
                        JsonObject value = Json.createObjectBuilder()
                                .add("error", "There's no service")
                                .add("status", "HTTP/1.1 404")
                                .build();

                        // Send JSON object to client
                        out.println(value.toString());
                    }
                    else {
                        // Sets the status code for this response.
                        resp.setStatus(200);

                        // Create a JSON array Builder
                        JsonArrayBuilder array = Json.createArrayBuilder();
                        for (Service s : factory.getDao().findAll()) {
                            // Creates a JsonObject Builder
                            array.add(Json.createObjectBuilder()
                                    .add("id", s.getId())
                                    .add("name", s.getName())
                                    .add("description", s.getDescription())
                                    .add("date", s.getDate())
                                    .add("status", "OK")
                                    .build());
                        }

                        // Send JSON object to client
                        out.println(array.build().toString());
                    }
                    break;
                case XML:
                    // Sets the content type of the response being sent to the client,
                    resp.setContentType(Mime.XML.getType());

                    // Check if the database is empty
                    if (factory.getDao().count() == 0) {
                        // Sets the status code for this response.
                        resp.setStatus(404);

                        // Send XML object to client
                        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                        out.println("<error>There's no service</error>");
                        out.println("<status>HTTP/1.1 404</status>");
                    }
                    else {
                        // Sets the status code for this response.
                        resp.setStatus(200);

                        // Send XML object to client
                        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                        out.println("<services>");
                        for (Service s : factory.getDao().findAll()) {
                            out.println("\t<service>");
                            out.println("\t\t<id>" + s.getId() + "</id>");
                            out.println("\t\t<name>" + s.getName() + "</name>");
                            out.println("\t\t<description>" + s.getDescription() + "</description>");
                            out.println("\t\t<date>" + s.getDate() + "</date>");
                            out.println("\t\t<status>OK</status>");
                            out.println("\t</service>");
                        }
                        out.println("</services>");
                    }
                    break;
                default:
                    // Sets the content type of the response being sent to the client,
                    resp.setContentType(Mime.HTML.getType());

                    // Check if the database is empty
                    if (factory.getDao().count() == 0) {
                        // Sets the status code for this response.
                        resp.setStatus(404);

                        // Send HTML object to client
                        out.println("{ error=\'There's no service\', status=\'HTTP/1.1 404\' }");
                    }
                    else {
                        // Sets the status code for this response.
                        resp.setStatus(200);

                        for (Service s : factory.getDao().findAll()) {
                            // Send HTML object to client
                            out.println(s.toString());
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Sets the character encoding (MIME charset)
        resp.setCharacterEncoding("UTF-8");

        // Gets PrintWriter object that can send character to client
        PrintWriter out = resp.getWriter();

        // Gets values of request parameter as a String
        String name = req.getParameter("name");
        String description = req.getParameter("description");

        // Gets MIME type of the body of the request
        final String type = req.getContentType();
        if ((type == null && name == null && description == null) || (type.isEmpty() && name.isEmpty() && description.isEmpty())) {
            // Sets the status code for this response.
            resp.setStatus(401);

            // Send HTML object to client
            out.println("{ error=\'Unauthorized\', status=\'HTTP/1.1 401\' }");
        }
        else {
            // Sets the status code for this response.
            resp.setStatus(201);

            // Create resource from request parameter
            Service s = new Service(name, description);

            // Save resource in the database
            factory.getDao().persist(s);

            // Find MIME Type
            Optional<Mime> mime = Mime.find(type);
            switch (mime.get()) {
                case JSON:
                    // Sets the content type of the response being sent to the client,
                    resp.setContentType(Mime.JSON.getType());

                    // Creates a JsonObject Builder
                    JsonObject value = Json.createObjectBuilder()
                            .add("id", s.getId())
                            .add("name", s.getName())
                            .add("description", s.getDescription())
                            .add("date", s.getDate())
                            .add("status", "Created")
                            .build();

                    // Send JSON object to client
                    out.println(value.toString());
                    break;
                case XML:
                    // Sets the content type of the response being sent to the client,
                    resp.setContentType(Mime.XML.getType());

                    // Send XML object to client
                    out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                    out.println("<service>");
                    out.println("\t<id>" + s.getId() + "</id>");
                    out.println("\t<name>" + s.getName() + "</name>");
                    out.println("\t<description>" + s.getDescription() + "</description>");
                    out.println("\t<date>" + s.getDate() + "</date>");
                    out.println("\t<status>Created</status>");
                    out.println("</service>");
                    break;
                default:
                    // Sets the content type of the response being sent to the client,
                    resp.setContentType(Mime.HTML.getType());

                    // Send HTML object to client
                    out.println(s.toString());
                    break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Sets the character encoding (MIME charset)
        resp.setCharacterEncoding("UTF-8");

        // Gets PrintWriter object that can send character to client
        PrintWriter out = resp.getWriter();

        // Gets values of request parameter as a String
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String description = req.getParameter("description");

        // Gets MIME type of the body of the request
        final String type = req.getContentType();
        if ((type == null && id == null && name == null && description == null) || (type.isEmpty() && id.isEmpty() && name.isEmpty() && description.isEmpty())) {
            // Sets the status code for this response.
            resp.setStatus(401);

            // Send HTML object to client
            out.println("{ error=\'Unauthorized\', status=\'HTTP/1.1 401\' }");
        }
        else {
            // Gets resources from database
            Optional<Service> s = factory.getDao().find(Long.valueOf(id));

            // Check if service is not null
            if (s.isPresent()) {
                // Sets the status code for this response.
                resp.setStatus(200);

                // Update information in the database
                factory.getDao().update(s.get(), new String[]{name, description});

                // Find MIME Type
                Optional<Mime> mime = Mime.find(type);
                switch (mime.get()) {
                    case JSON:
                        // Sets the content type of the response being sent to the client,
                        resp.setContentType(Mime.JSON.getType());

                        // Creates a JsonObject Builder
                        JsonObject value = Json.createObjectBuilder()
                                .add("id", s.get().getId())
                                .add("name", s.get().getName())
                                .add("description", s.get().getDescription())
                                .add("date", s.get().getDate())
                                .add("status", "Updated")
                                .build();

                        // Send JSON object to client
                        out.println(value.toString());
                        break;
                    case XML:
                        // Sets the content type of the response being sent to the client,
                        resp.setContentType(Mime.XML.getType());

                        // Send XML object to client
                        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                        out.println("<service>");
                        out.println("\t<id>" + s.get().getId() + "</id>");
                        out.println("\t<name>" + s.get().getName() + "</name>");
                        out.println("\t<description>" + s.get().getDescription() + "</description>");
                        out.println("\t<date>" + s.get().getDate() + "</date>");
                        out.println("\t<status>Updated</status>");
                        out.println("</service>");
                        break;
                    default:
                        // Sets the content type of the response being sent to the client,
                        resp.setContentType(Mime.HTML.getType());

                        // Send HTML object to client
                        out.println(s.toString());
                        break;
                }
            }
            else {
                // Sets the status code for this response.
                resp.setStatus(409);

                // Find MIME Type
                Optional<Mime> mime = Mime.find(type);
                switch (mime.get()) {
                    case JSON:
                        // Sets the content type of the response being sent to the client,
                        resp.setContentType(Mime.JSON.getType());

                        // Creates a JsonObject Builder
                        JsonObject value = Json.createObjectBuilder()
                                .add("error", "Conflict")
                                .add("status", "HTTP/1.1 409")
                                .build();

                        // Send JSON object to client
                        out.println(value.toString());
                        break;
                    case XML:
                        // Sets the content type of the response being sent to the client,
                        resp.setContentType(Mime.XML.getType());

                        // Send XML object to client
                        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                        out.println("<error>Conflict</error>");
                        out.println("<status>HTTP/1.1 409</status>");
                        break;
                    default:
                        // Send HTML object to client
                        out.println("{ error=\'Conflict\', status=\'HTTP/1.1 409\' }");
                        break;
                }
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Sets the character encoding (MIME charset)
        resp.setCharacterEncoding("UTF-8");

        // Gets PrintWriter object that can send character to client
        PrintWriter out = resp.getWriter();

        // Gets values of request parameter as a String
        String id = req.getParameter("id");

        // Gets MIME type of the body of the request
        final String type = req.getContentType();
        if ((type == null && id == null) || (type.isEmpty() && id.isEmpty())) {
            // Sets the status code for this response.
            resp.setStatus(401);

            // Send HTML object to client
            out.println("{ error=\'Unauthorized\', status=\'HTTP/1.1 401\' }");
        }
        else {
            // Gets resources from database
            Optional<Service> s = factory.getDao().find(Long.valueOf(id));

            // Check if service is not null
            if (s.isPresent()) {
                // Sets the status code for this response.
                resp.setStatus(200);

                // Remove service from database
                factory.getDao().remove(s.get());

                // Find MIME Type
                Optional<Mime> mime = Mime.find(type);
                switch (mime.get()) {
                    case JSON:
                        // Sets the content type of the response being sent to the client,
                        resp.setContentType(Mime.JSON.getType());

                        // Creates a JsonObject Builder
                        JsonObject value = Json.createObjectBuilder()
                                .add("id", s.get().getId())
                                .add("name", s.get().getName())
                                .add("description", s.get().getDescription())
                                .add("date", s.get().getDate())
                                .add("status", "Deleted")
                                .build();

                        // Send JSON object to client
                        out.println(value.toString());
                        break;
                    case XML:
                        // Sets the content type of the response being sent to the client,
                        resp.setContentType(Mime.XML.getType());

                        // Send XML object to client
                        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                        out.println("<service>");
                        out.println("\t<id>" + s.get().getId() + "</id>");
                        out.println("\t<name>" + s.get().getName() + "</name>");
                        out.println("\t<description>" + s.get().getDescription() + "</description>");
                        out.println("\t<date>" + s.get().getDate() + "</date>");
                        out.println("\t<status>Deleted</status>");
                        out.println("</service>");
                        break;
                    default:
                        // Sets the content type of the response being sent to the client,
                        resp.setContentType(Mime.HTML.getType());

                        // Send HTML object to client
                        out.println(s.toString());
                        break;
                }
            }
            else {
                // Sets the status code for this response.
                resp.setStatus(409);

                // Find MIME Type
                Optional<Mime> mime = Mime.find(type);
                switch (mime.get()) {
                    case JSON:
                        // Sets the content type of the response being sent to the client,
                        resp.setContentType(Mime.JSON.getType());

                        // Creates a JsonObject Builder
                        JsonObject value = Json.createObjectBuilder()
                                .add("error", "Conflict")
                                .add("status", "HTTP/1.1 409")
                                .build();

                        // Send JSON object to client
                        out.println(value.toString());
                        break;
                    case XML:
                        // Sets the content type of the response being sent to the client,
                        resp.setContentType(Mime.XML.getType());

                        // Send XML object to client
                        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                        out.println("<error>Conflict</error>");
                        out.println("<status>HTTP/1.1 409</status>");
                        break;
                    default:
                        // Send HTML object to client
                        out.println("{ error=\'Conflict\', status=\'HTTP/1.1 409\' }");
                        break;
                }
            }
        }
    }
}
