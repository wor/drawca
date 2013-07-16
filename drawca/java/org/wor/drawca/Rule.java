/**
 * Rule.java
 *
 * Copyright 2013 Esa Määttä
 *
 * This file is part of drawca.
 *
 * drawca is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * drawca is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with drawca.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.wor.drawca;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.common.collect.ImmutableBiMap;

/**
 * Represents elementary (3-neighborhood) cellular automata rules and their
 * application.
 *
 * @author "Esa Määttä" (esa.maatta@iki.fi)
 */
public class Rule {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(Rule.class.getName());

    /**
     * Rule as String to Character mapping where String represents the
     * neighborhood consisting of Characters.
     */
    private Map<String, Character> rule;

    /**
     * 2-way color (int/RGB) to character mapping.
     *
     * Determines which binary value is mapped to which color so the int is in
     * range [0-1].
     */
    public static final ImmutableBiMap<Integer, Character> COLOR_TO_CHAR =
        ImmutableBiMap.of(
                Color.WHITE.getRGB(), '0',
                Color.BLACK.getRGB(), '1');

    /**
     * Constructs rule from integer number.
     *
     * @param num Rule number to construct [0-255].
     */
    public Rule(final int num) {
        // Transform integer rule to binary rule as a String.
        final String ruleStr = String.format("%8s",
                Integer.toBinaryString(num)).replace(' ', '0');
        LOG.info(num + ":" + ruleStr);

        // TODO: Use collect lib, builder pattern is just wee little bit cleaner
        //       than double brace initilization ;)
        // TODO: Don't use magic numbers
        rule = new HashMap<String, Character>() {
            private static final long serialVersionUID = 1L;
            {
                put("000", ruleStr.charAt(7));
                put("001", ruleStr.charAt(6));
                put("010", ruleStr.charAt(5));
                put("011", ruleStr.charAt(4));
                put("100", ruleStr.charAt(3));
                put("101", ruleStr.charAt(2));
                put("110", ruleStr.charAt(1));
                put("111", ruleStr.charAt(0));
            }
        };
        rule = Collections.unmodifiableMap(rule);

        LOG.info(rule.toString());
    }

    /**
     * Gets matching Character value for the given neighborhood.
     *
     * @param neighborhood Which Character value is asked.
     * @return Character which matches given neighborhood.
     */
    public final Character getValue(final String neighborhood) {
        return rule.get(neighborhood);
    }

    /**
     * Gets mathcing Character value for the given neighborhood.
     *
     * The neighborhood is defined by three integers in this case. Integers are
     * RGB color values.
     *
     * @param a Left neighborhood RGB color value.
     * @param b Middle neighborhood RGB color value.
     * @param c Right neighborhood RGB color value.
     * @return Character which matches given neighborhood.
     */
    public final Character getValue(final int a, final int b, final int c) {
        String neighborhood = COLOR_TO_CHAR.get(a).toString()
            + COLOR_TO_CHAR.get(b) + COLOR_TO_CHAR.get(c);
        return rule.get(neighborhood);
    }
}

