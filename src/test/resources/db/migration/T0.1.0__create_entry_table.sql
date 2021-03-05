CREATE TABLE entry (
    id VARCHAR(255) PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    time TIMESTAMP NOT NULL,
    category VARCHAR(255) NOT NULL,
    amount DECIMAL NOT NULL
);