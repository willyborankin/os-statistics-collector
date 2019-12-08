FROM azul/zulu-openjdk:11

ENV CONSUMER_JDBC_URL=""
ENV CONSUMER_DB_APP_USER=""
ENV CONSUMER_DB_APP_PWD=""
ENV CONSUMER_DB_FLYWAY_USER=""
ENV CONSUMER_DB_FLYWAY_PWD=""
ENV CONSUMER_POLL="100ms"
ENV CONSUMER_THREADS_AMOUNT=1
ENV CONSUMER_TOPIC_NAME="os-statistics"

ENV KAFKA_BROKERS=""

RUN mkdir -p /usr/app

COPY ./consumer/build/distributions/consumer.tar /usr/app

WORKDIR /usr/app

RUN tar xvf consumer.tar

CMD consumer/bin/consumer



