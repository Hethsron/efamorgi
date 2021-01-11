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
package fr.uha.ensisa.aia.res;
/**
 *		@file            	Mime.java
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
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

public enum Mime {

    CSS("text/css", "^([-/a-zA-Z0-9]+.css)$"),
    HTML("text/html", "^([-/a-zA-Z0-9]+.html)$"),
    ICON("image/x-icon", "^([-/a-zA-Z0-9]+.ico)$"),
    JAVASCRIPT("application/javascript", "^([-/a-zA-Z0-9]+.js)$"),
    JPEG("image/jpeg", "^([-/a-zA-Z0-9]+.jpeg)$"),
    JSON("application/json", "^([-/a-zA-Z0-9]+.json)$"),
    TTF("font/ttf", "^([-/a-zA-Z0-9]+.ttf)$"),
    WOFF("font/woff", "^([-/a-zA-Z0-9]+.woff)$"),
    WOFF2("font/woff2", "^([-/a-zA-Z0-9]+.woff2)$"),
    XML("application/xml", "^([-/a-zA-Z0-9]+.xml)$");

    private final String type;
    private final String regex;

    Mime(String type, String regex) {
        this.type = type;
        this.regex = regex;
    }

    public String getType() {
        return type;
    }

    public String getRegex() {
        return regex;
    }

    public static Optional<Mime> find(String type) {
        return Arrays.stream(values())
                .filter(mime -> mime.type.equalsIgnoreCase(type))
                .findFirst();
    }

    public static Optional<Mime> get(String input) {
        return Arrays.stream(values())
                .filter(mime -> Pattern.matches(mime.regex, input))
                .findFirst();
    }

}
