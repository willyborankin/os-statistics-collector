# Operation system statistics collector

This project is just small example of using Kafka producer/consumer.
It takes operation system metrics using and puts them into PostgreSQL database 

Before building the project make sure that you have installed:
- JDK version >= 11
- Docker version >= 19.03.5-ce
- Docker Compose version >= 1.25.x

and have access or have installed:
- PostgreSQL database
- Kafka service version >= 2.3

### How to build
You will need gradle for building `os-statistics-collector`
- To build run `./gradlew clean build` this will build the project

*Before you run consumer and producer, you should prepare you SSL certificates*

Download your:
- Access Key - expected file name is: `service.key`
- Access Certificate - expected file name is: `service.cert`
- CA Certificate - expected file name is: `ca.pem`

and put them into `config` folder

### How to run Kafka consumer
To run Kafka consumer you need:

1. Create a database in you PostgreSQL server with users:
    - `flyway` - which has access to to your database
    - `application`  - which has access to you database as well
    e.g.
    
    ```sql
        CREATE DATABASE db_name;
        CREATE USER flyway WITH PASSWORD 'flyway';
        CREATE USER application WITH PASSWORD 'application';
        GRANT ALL PRIVILEGES ON DATABASE db_name TO flyway;
    ```

    All other database structures like schema, tables and setting permissions will be created by flyway migration which located in `consumer/config`.

2. Create a kafka topic, default kafka topic is: `osmetrics`.
if you want to use another name please see the full list of environment variables the `consumer/docker/Dockerfile` file

3. Change environment variables in `run-consumer-run.yml`:
    - `KAFKA_BROKERS` - comma-separated list of Kafka brokers
    - `CONSUMER_JDBC_URL` - PgSQL jdbc URL to the database
    - `CONSUMER_DB_APP_USER` - application user name
    - `CONSUMER_DB_APP_PWD` - application user password
    - `CONSUMER_DB_FLYWAY_USER` - flyway user name
    - `CONSUMER_DB_FLYWAY_PWD` - flyway user password

    Full list of environment variables can be found in the `consumer/docker/Dockerfile` file

4. run `docker-compose -f run-consumer-run.yml up --build`

### How to run Kafka producer
To run kafka producer you need:

1. Create a kafka topic, default kafka topic is: `osmetrics`. 
if you want to use another name please see the full list of environment variables the `producer/docker/Dockerfile` file

2. Change environment variables in `run-consumer-run.yml`
    - KAFKA_BROKERS - comma-separated list of Kafka brokers
    - PRODUCER_HOSTNAME - producer host name
    - PRODUCER_IP_ADDRESS - producer IP address
    - KAFKA_TOPIC_NAME - Kafka topic name

    Full list of environment variables can be found in the `producer/docker/Dockerfile` file

3. run `docker-compose -f run-producer-run.yml up --build`

### Project structure
- `config` - configuration folder for the SSL certificates 
- `commons` - contains common code and model for the consumer and producer
- `consumer` - consumer implementation
- `producer` - producer implementation
