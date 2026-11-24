-- V1: Create persons table
-- Flyway migration script for the person-mcp-service schema.

CREATE TABLE IF NOT EXISTS persons (
    id          BIGSERIAL PRIMARY KEY,
    first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL,
    age         INTEGER      NOT NULL CHECK (age > 0),
    nationality VARCHAR(100) NOT NULL,
    gender      VARCHAR(10)  CHECK (gender IN ('MALE', 'FEMALE'))
);

CREATE INDEX idx_persons_nationality ON persons (nationality);
