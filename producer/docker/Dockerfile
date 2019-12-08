FROM azul/zulu-openjdk:11

RUN mkdir -p /usr/app

COPY ./producer/build/distributions/producer.tar /usr/app

WORKDIR /usr/app

RUN tar xvf producer.tar

CMD producer/bin/producer