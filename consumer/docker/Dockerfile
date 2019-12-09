FROM azul/zulu-openjdk-debian:11

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get -y install libreadline7 && apt-get -y install openssl

#JDBC URL
ENV CONSUMER_JDBC_URL=""
#Database applcation user name, which consumer app uses to store topica data in the database
ENV CONSUMER_DB_APP_USER=""
#Database applcation user password
ENV CONSUMER_DB_APP_PWD=""
#Path where Flyway SQL migration scripts are located
ENV CONSUMER_DB_FLYWAY_LOCATIONS="/home/app/consumer/conf"
#Database migration user, which consumer app uses to migrate your database
ENV CONSUMER_DB_FLYWAY_USER=""
#Database migration user pwaaword
ENV CONSUMER_DB_FLYWAY_PWD=""
#kafka consumer poll timout in milliseconds
ENV CONSUMER_POLL="1000ms"
#Amount of threads per each consumer
ENV CONSUMER_THREADS_AMOUNT=1
#Kafka topic name
ENV CONSUMER_TOPIC_NAME="osmetrics"
#Kafka brokers, comma-separated list
ENV KAFKA_BROKERS=""

RUN mkdir -p /home/app

COPY ./consumer/build/distributions/consumer.tar /home/app
COPY ./consumer/docker/run-consumer /home/app

WORKDIR /home/app

RUN tar xvf consumer.tar
RUN mkdir -p /home/app/consumer/conf
COPY ./conf/ /home/app/consumer/conf

CMD sh run-consumer


