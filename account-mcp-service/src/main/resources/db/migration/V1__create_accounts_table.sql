-- V1: Create accounts table
CREATE TABLE IF NOT EXISTS accounts (
    id        BIGSERIAL    PRIMARY KEY,
    number    VARCHAR(30)  NOT NULL,
    balance   INTEGER      NOT NULL DEFAULT 0 CHECK (balance >= 0),
    person_id BIGINT       NOT NULL
);

CREATE INDEX idx_accounts_person_id ON accounts (person_id);
