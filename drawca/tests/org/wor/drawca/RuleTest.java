/**
 * RuleTest.java
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

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link Rule}.
 *
 * @author esa.maatta@iki.fi (Esa Määttä)
 */
@RunWith(JUnit4.class)
public class RuleTest {

    /**
     * Test Rule string value getter.
     *
     * Just checks that even/odd rules have correct start byte.
     */
    @Test
    public final void getValueTest() {
        for (int i = 0; i < 255; i++) {
            Rule rule = new Rule(i);
            if ((i & 1) == 0) {
                assertEquals("Even Failed with rule: " + i, (Character) '0',
                        rule.getValue("000"));
            } else {
                assertEquals("Odd Failed with rule: " + i, (Character) '1',
                        rule.getValue("000"));
            }
        }
    }

    /**
     * Test Rule int value getter.
     *
     * Just checks that even/odd rules have correct start byte.
     */
    @Test
    public final void getValueIntTest() {
        int zeroColor = Rule.COLOR_TO_CHAR.inverse().get('0');
        for (int i = 0; i < 255; i++) {
            Rule rule = new Rule(i);
            if ((i & 1) == 0) {
                assertEquals("Even Failed with rule: " + i, (Character) '0',
                        rule.getValue(zeroColor, zeroColor, zeroColor));
            } else {
                assertEquals("Odd Failed with rule: " + i, (Character) '1',
                        rule.getValue(zeroColor, zeroColor, zeroColor));
            }
        }
    }
}
