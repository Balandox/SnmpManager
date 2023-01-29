FROM openjdk:17-jdk-slim

ARG JAR_FILE=target/SnmpManager-1.0-SNAPSHOT-jar-with-dependencies.jar

RUN mkdir /opt/snmp-manager

COPY ${JAR_FILE} opt/snmp-manager/SnmpManager-1.0-SNAPSHOT-jar-with-dependencies.jar

ENTRYPOINT ["java", "-jar", "opt/snmp-manager/SnmpManager-1.0-SNAPSHOT-jar-with-dependencies.jar"]









