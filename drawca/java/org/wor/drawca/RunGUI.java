/**
 * RunGUI.java
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

import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/**
 * Mouse click listener.
 *
 * @author "Esa Määttä" (esa.maatta@iki.fi)
 */
class MouseListener extends MouseInputAdapter {
    @Override
    public void mouseClicked(final MouseEvent e) {
        // Draw one line iteration on a mouse click
        CAPanel c = (CAPanel) ((JFrame) e.getSource()).getContentPane();
        c.drawCellularAutomataIteration();
        c.repaint();
    }
}

/**
 * GUI runner class.
 *
 * @author "Esa Määttä" (esa.maatta@iki.fi)
 */
public class RunGUI implements Runnable {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(Rule.class.getName());

    /**
     * Window width of the main GUI window.
     */
    private int windowWidth;

    /**
     * Window height of the main GUI window.
     */
    private int windowHeigth;

    /**
     * X-Scaling factor of the pixels to drawn.
     */
    private float xScaleFactor;

    /**
     * Y-Scaling factor of the pixels to drawn.
     */
    private float yScaleFactor;

    /**
     * Rule to be used as integer from range [0-255].
     */
    private int rule;

    /**
     * Initial pixel line to be used as string of ones and zeroes.
     */
    private String initialLine;

    /**
     * Generate new line with mouse click.
     */
    private boolean perClickIteration;

    /**
     * Construct GUI runner with window size, scaling and a rule number.
     *
     * @param windowWidth Window width.
     * @param windowHeigth Window height.
     * @param xScaleFactor X-scaling of pixels.
     * @param yScaleFactor Y-scaling of pixels.
     * @param rule Cellural automata rule as an integer [0-255].
     */
    public RunGUI(
            final int windowWidth,
            final int windowHeigth,
            final float xScaleFactor,
            final float yScaleFactor,
            final int rule) {
        this.windowWidth = windowWidth;
        this.windowHeigth = windowHeigth;
        this.xScaleFactor = xScaleFactor;
        this.yScaleFactor = yScaleFactor;
        this.rule = rule;
        // Defaults
        initialLine = "";
        perClickIteration = false;
    }

    /**
     * Constructor which allows also initial line and possible line per mouse
     * click iteration.
     *
     * for base parameters see:
     * {@link RunGUI#RunGUI(int, int, float, float, int)}
     *
     * @param initialLine Initial line as a String of ones and zeroes.
     * @param perClickIteration Perform iteration of next line per mouse click?
     */
    public RunGUI(
            final int windowWidth,
            final int windowHeigth,
            final float xScaleFactor,
            final float yScaleFactor,
            final int rule,
            final String initialLine,
            final boolean perClickIteration) {
        this.initialLine = initialLine.replaceAll("(\\r)?\\n", "");

        if (initialLine.length() > 0) {
            this.windowWidth = initialLine.length() * (int) xScaleFactor;
        } else {
            this.windowWidth = windowWidth;
        }
        this.windowHeigth = windowHeigth;
        this.xScaleFactor = xScaleFactor;
        this.yScaleFactor = yScaleFactor;
        this.rule = rule;
        this.perClickIteration = perClickIteration;
    }

    @Override
    public final void run() {
        createAndShowGUI();
    }

    /**
     * Creates GUI elements and shows it to the user.
     */
    private void createAndShowGUI() {
        if (!SwingUtilities.isEventDispatchThread()) {
            LOG.warning("GUI was not created on EDT!");
        }

        JFrame f = new JFrame("Cajava");

        if (perClickIteration) {
            MouseListener mouseListener = new MouseListener();
            f.addMouseListener(mouseListener);
        }

        CAPanel canvas = new CAPanel(windowWidth, windowHeigth, xScaleFactor,
                yScaleFactor, rule, initialLine);
        f.setContentPane(canvas);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(windowWidth, windowHeigth);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        canvas.setupBackground();

        if (!perClickIteration) {
            canvas.drawCellularAutomata();
        }
    }
}
