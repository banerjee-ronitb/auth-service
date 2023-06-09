FROM openjdk:11
ADD target/auth-service-0.0.1-SNAPSHOT.jar auth-service-0.0.1-SNAPSHOT.jar
ADD okta.com.cer /home/app/ssl.cer
RUN \
keytool -importcert -alias startssl -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -file /home/app/ssl.cer -noprompt

ENTRYPOINT ["java", "-jar", "auth-service-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080