FROM azul/zulu-openjdk-debian:11

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get -y install libreadline7 && apt-get -y install openssl

ENV CONSUMER_JDBC_URL=""
ENV CONSUMER_DB_APP_USER=""
ENV CONSUMER_DB_APP_PWD=""
ENV CONSUMER_DB_FLYWAY_LOCATIONS="/home/app/consumer/conf"
ENV CONSUMER_DB_FLYWAY_USER=""
ENV CONSUMER_DB_FLYWAY_PWD=""
ENV CONSUMER_POLL="1000ms"
ENV CONSUMER_THREADS_AMOUNT=1
ENV CONSUMER_TOPIC_NAME="osmetrics"
ENV KAFKA_BROKERS=""

RUN mkdir -p /home/app

COPY ./consumer/build/distributions/consumer.tar /home/app
COPY ./consumer/docker/run-consumer /home/app

WORKDIR /home/app

RUN tar xvf consumer.tar
RUN mkdir -p /home/app/consumer/conf
COPY ./conf/ /home/app/consumer/conf

CMD sh run-consumer


