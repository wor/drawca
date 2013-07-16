/**
 * DrawCAMain.java
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

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.google.common.collect.ImmutableMap;

/**
 * Main class for drawca.
 *
 * @author "Esa Määttä" (esa.maatta@iki.fi)
 */
public final class DrawCAMain {
    /**
     * Map verbosity level from int [-1,0,1,3,...,7] to Level.
     */
    private static final ImmutableMap<Integer, Level> VERBOSITY_MAP = new
        ImmutableMap.Builder<Integer, Level>()
        .put(-1, Level.OFF)
        .put(0,  Level.SEVERE)
        .put(1,  Level.WARNING)
        .put(2,  Level.INFO)
        .put(3,  Level.CONFIG)
        .put(4,  Level.FINE)
        .put(5,  Level.FINER)
        .put(6,  Level.FINEST)
        .put(7,  Level.ALL)
        .build();

    /**
     * Main function which start drawing the cellular automata.
     *
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {
        final Logger log = Logger.getGlobal();
        LogManager.getLogManager().reset();

        Options options = new Options();
        boolean hasArgs = true;

        // TODO: show defaults in option description
        options.addOption("h", "help", !hasArgs, "Show this help message");
        options.addOption("pci", "perclickiteration", !hasArgs,
                "Generate one line per mouse click");

        options.addOption("v", "verbose", hasArgs, "Verbosity level [-1,7]");
        options.addOption("r", "rule", hasArgs, "Rule number to use 0-255");
        options.addOption("wh", "windowheigth", hasArgs, "Draw window height");
        options.addOption("ww", "windowwidth", hasArgs, "Draw window width");
        options.addOption("x", "xscalefactor", hasArgs, "X Scale factor");
        options.addOption("y", "yscalefactor", hasArgs, "Y scale factor");
        options.addOption("f", "initline", hasArgs,
                "File name with Initial line.");

        CommandLineParser parser = new PosixParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            showHelp(options);
            return;
        }

        // Options without an argument
        if (cmd.hasOption("h")) {
            showHelp(options);
            return;
        }
        final boolean perClickIteration = cmd.hasOption("pci");

        // Options with an argument
        final int verbosityLevel = Integer.parseInt(
                cmd.getOptionValue('v', "0"));
        final int rule = Integer.parseInt(
                cmd.getOptionValue('r', "110"));
        final int windowHeigth = Integer.parseInt(
                cmd.getOptionValue("wh", "300"));
        final int windowWidth = Integer.parseInt(
                cmd.getOptionValue("ww", "400"));
        final float xScaleFactor = Float.parseFloat(
                cmd.getOptionValue('x', "2.0"));
        final float yScaleFactor = Float.parseFloat(
                cmd.getOptionValue('y', "2.0"));
        final String initLineFile = cmd.getOptionValue('f', "");

        final Level logLevel = VERBOSITY_MAP.get(verbosityLevel);
        log.setLevel(logLevel);

        // Set log handler
        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(logLevel);
        log.addHandler(consoleHandler);

        log.info("Log level set to: " + log.getLevel());

        // Read initial line from a file
        String initLine = "";
        if (initLineFile.length() > 0) {
            Path initLineFilePath = FileSystems.getDefault()
                .getPath(initLineFile);
            try {
                // Should be string of ones and zeros only
                initLine = new String(Files
                        .readAllBytes(initLineFilePath), "UTF-8");
            } catch (IOException e) {
                System.err.format("IOException: %s\n", e);
                return;
            }
        }

        SwingUtilities.invokeLater(new RunGUI(windowWidth, windowHeigth,
                    xScaleFactor, yScaleFactor, rule, initLine,
                    perClickIteration));
    }

    /**
     * Private utility class constructor.
     *
     * {@inheritDoc}
     * @see Object#DrawCAMain()
     */
    private DrawCAMain() {
        throw new AssertionError("Utility class instantiation.");
    };

    /**
     * Prints command line help.
     *
     * @param options Defined command line options.
     */
    private static void showHelp(final Options options) {
        HelpFormatter h = new HelpFormatter();
        h.printHelp("help", options);
        System.exit(-1);
    }
}
