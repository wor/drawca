#!/bin/bash

# TODO: Parse class paths from eclipse project conf file: ./drawca/.classpath
# TODO: main class automatically
java -cp "./drawca/bin:/usr/share/java/commons-cli/commons-cli.jar:/usr/share/java/guava/guava.jar" org.wor.drawca.DrawCAMain ${@}
