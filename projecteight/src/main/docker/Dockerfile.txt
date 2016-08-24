FROM dockerfile/java:oracle-java8

VOLUME /tmp
ADD projecteight.jar /opt/projecteight/
EXPOSE 8080
WORKDIR /opt/projecteight/
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-Xms512m", "-Xmx1g", "-jar", "projecteight.jar"]
