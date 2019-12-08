CREATE SCHEMA IF NOT EXISTS os_statistics;

GRANT ALL PRIVILEGES ON SCHEMA os_statistics TO flyway;
GRANT ALL PRIVILEGES ON SCHEMA os_statistics TO application;

SET search_path TO os_statistics;

CREATE TABLE host_hardware (
    id                         UUID         NOT NULL,
    host                       VARCHAR(100) NOT NULL,
    ip_address                 INET         NULL,
    cpu_utilization            BIGINT       NOT NULL,
    system_load_average        BIGINT       NOT NULL,
    free_physical_memory_size  BIGINT       NOT NULL,
    total_physical_memory_size BIGINT       NOT NULL,
    free_swap_space_size       BIGINT       NOT NULL,
    total_swap_space_size      BIGINT       NOT NULL,
    created_at                 TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) WITHOUT OIDS;

CREATE TABLE host_file_system (
    id           UUID         NOT NULL,
    host         VARCHAR(100) NOT NULL,
    ip_address   INET         NULL,
    path         VARCHAR(50)  NOT NULL,
    total_space  BIGINT       NOT NULL,
    usable_space BIGINT       NOT NULL,
    free_space   BIGINT       NOT NULL,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) WITHOUT OIDS;

GRANT ALL PRIVILEGES ON SCHEMA os_statistics TO application;
GRANT ALL PRIVILEGES ON host_file_system TO application;
GRANT ALL PRIVILEGES ON host_hardware TO application;
