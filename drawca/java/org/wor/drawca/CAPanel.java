/**
 * CAPanel.java
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

import static java.lang.System.out;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

/**
 * Cellular automata panel.
 *
 * @author "Esa Määttä" (esa.maatta@iki.fi)
 * @version
 */
public class CAPanel extends JPanel {

    /**
     * serialVersionUID needed for serializable class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Panel background image.
     */
    private BufferedImage backgroundImage;

    /**
     * Panel width.
     */
    private int xScaled;

    /**
     * Panel height.
     */
    private int yScaled;

    /**
     * Current iteration/line of the cellular automata being drawn.
     */
    private int iteration;

    /**
     * Rule used to draw the cellular automata.
     */
    private Rule rule;

    /**
     * Initial/first line of the cellular automata if given before hand.
     */
    private String initialLine;

    /**
     * Constructs panel to which cellular automata is drawn.
     *
     * @param width Panel width.
     * @param heigth Panel height.
     * @param xScaleFactor Scaling factor of pixels drawn in x-axis.
     * @param yScaleFactor Scaling factor of pixels darwn in y-axis.
     * @param rule Cellular automata rule number [0-255].
     * @param initialLine Initial condition (x-axis pixels) as a string of ones
     * and zeroes.
     */
    public CAPanel(final int width, final int heigth, final float xScaleFactor,
            final float yScaleFactor, final int rule, final String initialLine)
    {
        super();
        iteration = 0;
        xScaled = width;
        yScaled = heigth;
        backgroundImage = new BufferedImage((int) (width / xScaleFactor),
                (int) (heigth / yScaleFactor), BufferedImage.TYPE_INT_ARGB);
        this.rule = new Rule(rule);
        this.initialLine = initialLine;
    }

    /**
     * {@inheritDoc}
     * @see javax.swing.JComponent#paintComponent(Graphics)
     */
    public final void paintComponent(final Graphics g) {
        super.paintComponent(g);

        // Draw background image each time the panel is repainted.
        g.drawImage(backgroundImage, 0, 0, xScaled, yScaled, null);
    }

    /**
     * {@inheritDoc}
     * @see javax.swing.JComponent#getWidth()
     */
    public final int getWidth() {
        return xScaled;
    }

    /**
     * {@inheritDoc}
     * @see javax.swing.JComponent#getHeight()
     */
    public final int getHeight() {
        return yScaled;
    }

    /**
     * Initializes background for the panel.
     */
    public final void setupBackground() {
        Graphics2D g2d = backgroundImage.createGraphics();

        // Fill bg color
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, xScaled, yScaled);

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    }

    /**
     * Draws one line (iteration) of cellular automata.
     */
    public final void drawCellularAutomataIteration() {
        Raster r = backgroundImage.getData();
        DataBuffer b = r.getDataBuffer();
        boolean useRandInit = true;
        final Logger log = Logger.getGlobal();

        if (iteration >= backgroundImage.getHeight()) {
            return;
        } else if (iteration == 0) {
            if (initialLine.length() > 0) {
                for (int i = 0; i < initialLine.length(); i++) {
                    b.setElem(i,
                            Rule.COLOR_TO_CHAR
                            .inverse()
                            .get(initialLine.charAt(i)));
                }
            } else {
                Random rand = new Random();
                for (int i = 0; i < backgroundImage.getWidth(); i++) {
                    if (useRandInit) {
                        if (rand.nextBoolean()) {
                            b.setElem(i, Color.BLACK.getRGB());
                        }
                    } else if ((i & 1) == 0) {
                        // Yes this is just an "even" check ;)
                        // (Javas '%' isn't the mod operation
                        // it's the remainder.)
                        b.setElem(i, Color.BLACK.getRGB());
                    }
                }
            }
        } else {
            int w = backgroundImage.getWidth();

            if (log.isLoggable(Level.FINER)) {
                // Print previous line as ones and zeroes
                out.print((iteration - 1) + ": ");
                for (int i = (iteration - 1) * w; i < (iteration * w); i++) {
                    out.print(Rule.COLOR_TO_CHAR.get(b.getElem(i)));
                }
                out.println("");
            }

            log.finest("-----------NEW LINE---------");
            int lastLineOffset = (iteration - 1) * w;
            for (int i = 0; i < w; i++) {
                int left = ((i - 1) % w + w) % w  + lastLineOffset;
                int middle = i + lastLineOffset;
                int right = (i + 1) % w + lastLineOffset;
                int current = i + iteration * w;

                Character c = rule.getValue(b.getElem(left), b.getElem(middle),
                        b.getElem(right));

                if (log.isLoggable(Level.FINEST)) {
                    out.println("left   i/c: " + left   + " : "
                            + b.getElem(left)); out.println("middle i/c: "
                            + middle + " : " + b.getElem(middle));
                    out.println("right  i/c: " + right  + " : "
                            + b.getElem(right)); out.println("char: " + c);
                    out.println("abs pos i=[" + current + "," + (iteration + 1)
                            * w + "] line " + iteration + " pos: i=" + i);
                    out.println("=============");
                }

                b.setElem(current, Rule.COLOR_TO_CHAR.inverse().get(c));
            }
        }
        iteration++;
        backgroundImage.setData(r);
    }

    /**
     * Draws cellular automata line by line.
     */
    public final void drawCellularAutomata() {
        for (int i = 0; i < backgroundImage.getHeight(); i++) {
            drawCellularAutomataIteration();
        }
    }

}

