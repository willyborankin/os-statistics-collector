FROM azul/zulu-openjdk-debian:11

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get -y install libreadline7 && apt-get -y install openssl

ENV PRODUCER_HOSTNAME=""
ENV PRODUCER_IP_ADDRESS=""
ENV PRODUCER_TICK_DURATION="1s"
ENV KAFKA_BROKERS=""
ENV KAFKA_TOPIC_NAME="osmetrics"

RUN mkdir -p /home/app

COPY ./producer/build/distributions/producer.tar /home/app
COPY ./producer/docker/run-producer /home/app

WORKDIR /home/app

RUN tar xvf producer.tar
RUN mkdir -p /home/app/producer/conf
COPY ./conf/ /home/app/producer/conf

CMD sh run-producer