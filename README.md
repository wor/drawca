drawca
======

Elementary (3-neighbour) cellular automata visualizer.

Done for Santa Fe Institute online course "Introduction to Complexity" as
a voluntary exercise.

Usage of Java drawca
--------------------

    java -cp \
    "./drawca/bin:/usr/share/java/commons-cli/commons-cli.jar:/usr/share/java/guava/guava.jar" \
    org.wor.drawca.DrawCAMain -h

Usage of python drawca
----------------------

For example, start drawing CA defined by rule 30. Initial condition is always
randomly generated:

    drawca 30

Java Dependencies
-----------------

* [commons-cli](http://commons.apache.org/proper/commons-cli/)
* [guava-libraries](http://code.google.com/p/guava-libraries/)

Python Dependencies
-------------------

* [pygame](http://www.pygame.org/)
