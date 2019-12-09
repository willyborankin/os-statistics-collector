FROM azul/zulu-openjdk-debian:11

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get -y install libreadline7 && apt-get -y install openssl

#Producer hostname
ENV PRODUCER_HOSTNAME=""
#Producer IP address, since Java doesn't  return real public IP address
ENV PRODUCER_IP_ADDRESS=""
#Time in seconds which collector uses to trigger meeages into Kafka topiv
ENV PRODUCER_TICK_DURATION="1s"
#Kafka brokers, comma-separated list
ENV KAFKA_BROKERS=""
#Kafka topic name
ENV KAFKA_TOPIC_NAME="osmetrics"

RUN mkdir -p /home/app

COPY ./producer/build/distributions/producer.tar /home/app
COPY ./producer/docker/run-producer /home/app

WORKDIR /home/app

RUN tar xvf producer.tar
RUN mkdir -p /home/app/producer/conf
COPY ./conf/ /home/app/producer/conf

CMD sh run-producer