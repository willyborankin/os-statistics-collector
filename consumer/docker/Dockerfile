FROM azul/zulu-openjdk:11

RUN mkdir -p /usr/app

COPY ./consumer/build/distributions/consumer.tar /usr/app

WORKDIR /usr/app

RUN tar xvf consumer.tar

CMD consumer/bin/consumer



