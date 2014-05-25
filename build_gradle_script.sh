#!/bin/bash
GRADLE_OPTS="-Xmx1024m -Xms256m -XX:MaxPermSize=512m -XX:+CMSClassUnloadingEnabled -XX:+HeapDumpOnOutOfMemoryError" /home/shawn/bin/gradle-1.10/bin/gradle clean build --stacktrace
