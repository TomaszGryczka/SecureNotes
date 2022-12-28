FROM openjdk:17-alpine as builder

WORKDIR app

COPY gradlew .
COPY .gradle .gradle
COPY build.gradle .
COPY gradle gradle
COPY gradlew.bat .
COPY settings.gradle .
COPY src src

RUN ./gradlew clean build -x test

ARG JAR_FILE=build/libs/*.jar
RUN java -Djarmode=layertools -jar ${JAR_FILE} extract --destination build/extracted

FROM openjdk:17-alpine

WORKDIR app
ARG EXTRACTED=app/build/extracted

COPY --from=builder ${EXTRACTED}/dependencies/ ./
COPY --from=builder ${EXTRACTED}/spring-boot-loader/ ./
COPY --from=builder ${EXTRACTED}/snapshot-dependencies/ ./
COPY --from=builder ${EXTRACTED}/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]