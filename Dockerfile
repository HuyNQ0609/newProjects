FROM openjdk:11-oraclelinux7 AS MAVEN_BUILD

MAINTAINER NguyenDuyDuc<nguyenduyduc@vnpt.vn>

#Define parameter
ARG APP_DIR=/app
ARG SRC_DIR=/target/minio-2.7.0.jar
ARG LOGPATH=/app/log/

#Create direction Application folder
RUN mkdir -p ${LOGPATH}

#Copy source to deploy direction
COPY ${SRC_DIR} ${APP_DIR}

WORKDIR ${APP_DIR}

ENV APP_FILE=$JAR_FILE
ENV LOG_PATH=${LOGPATH}

RUN chmod a+x ${APP_DIR}/*

EXPOSE 8080

ENTRYPOINT ["java","-Dlog_path=/app/log/", "-jar", "minio-2.7.0.jar"]