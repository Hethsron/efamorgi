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

}
