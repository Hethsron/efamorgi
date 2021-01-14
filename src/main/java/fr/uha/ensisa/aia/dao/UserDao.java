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
package fr.uha.ensisa.aia.dao;
/**
 *		@file            	UserDao.java
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
import fr.uha.ensisa.aia.model.User;
import fr.uha.ensisa.aia.res.Parameter;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class UserDao implements Dao<User> {

    private Map<Long, User> store = Collections.synchronizedMap(new TreeMap<Long, User>());

    @Override
    public Optional<User> find(long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Collection<User> findAll() {
        return store.values();
    }

    @Override
    public void persist(User user) {
        store.put(user.getId(), user);
    }

    @Override
    public void update(User user, String[] params) {
        user.setLastname(Objects.requireNonNull(params[0], "User lastname cannot be null"));
        user.setFirstname(Objects.requireNonNull(params[0], "User firstname cannot be null"));
        user.setPassword(Objects.requireNonNull(params[0], "User password cannot be null"));
        store.put(user.getId(), user);
    }

    @Override
    public void remove(User user) {
        store.remove(user.getId());
    }

    @Override
    public long count() {
        return store.size();
    }

    public void populate() {
        final String FILENAME = "src/main/webapp/META-INF/assets/json/database.json";
        if (store.size() != 0) {
            // Create a JSON array Builder
            JsonArrayBuilder array = Json.createArrayBuilder();
            for (User user : store.values()) {
                // Creates a JsonObject Builder
                array.add(Json.createObjectBuilder()
                        .add(Parameter.ID.getName(), user.getId())
                        .add(Parameter.LASTNAME.getName(), user.getLastname())
                        .add(Parameter.FIRSTNAME.getName(), user.getFirstname())
                        .add(Parameter.EMAIL.getName(), user.getEmail())
                        .add(Parameter.PASSWORD.getName(), user.getPassword())
                        .add(Parameter.DATE.getName(), user.getDate())
                        .build());
            }

            // Write JSON file
            try (FileWriter writer = new FileWriter(FILENAME)) {
                // Save JSON object to server
                writer.write(array.build().toString());

                // Flush the stream
                writer.flush();
            }
            catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void retrieval() {
        final String FILENAME = "src/main/webapp/META-INF/assets/json/database.json";
        if (store.size() == 0) {
            // Creates a parser factory for creating JsonParser object
            JsonParserFactory parserFactory = Json.createParserFactory(null);

            // Read JSON file
            try (FileReader reader = new FileReader(FILENAME)) {
                // Creates a JsonParser from a stream character
                JsonParser parser = parserFactory.createParser(reader);

                // Creates an empty list with initial capacity
                List<String> values = new ArrayList<>();

                // While there are more parsing
                while (parser.hasNext()) {
                    // Return the event of the next parsing state
                    JsonParser.Event event = parser.next();
                    switch (event) {
                        case KEY_NAME: {
                            // TODO : NOTHING
                            break;
                        }
                        case VALUE_STRING: {
                            // Appends the specified element to the list
                            values.add(parser.getString());
                            break;
                        }
                        case VALUE_NUMBER: {
                            // Appends the specified element to the list
                            values.add(String.valueOf(parser.getLong()));
                            break;
                        }
                    }

                    if (values.size() == 6) {
                        // Define new user
                        User user = new User(values.get(1), values.get(2), values.get(3), values.get(4));
                        user.setId(Long.parseLong(values.get(0)));
                        user.setDate(values.get(5));

                        // Adds the new user in the database
                        persist(user);

                        // Remove all elements from the list
                        values.clear();
                    }
                }
            }
            catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public boolean contains(String email, String password) {
        for (User user : store.values()) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

}
