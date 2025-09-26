CREATE TABLE IF NOT EXISTS capacities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(90) NOT NULL
);

CREATE TABLE IF NOT EXISTS capacity_technology (
    id BIGSERIAL PRIMARY KEY,
    capacity_id BIGINT NOT NULL,
    technology_id BIGINT NOT NULL
);
