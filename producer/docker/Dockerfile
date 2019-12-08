FROM azul/zulu-openjdk:11

ENV PRODUCER_HOSTNAME=""
ENV PRODUCER_IP_ADDRESS=""
ENV PRODUCER_TICK_DURATION="1s"

ENV KAFKA_BROKERS=""
ENV KAFKA_TOPIC_NAME=""

RUN mkdir -p /usr/app

COPY ./producer/build/distributions/producer.tar /usr/app
COPY ./producer/docker/run-producer /usr/app

WORKDIR /usr/app

RUN tar xvf producer.tar

CMD sh run-producer