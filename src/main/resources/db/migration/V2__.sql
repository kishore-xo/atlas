CREATE SEQUENCE IF NOT EXISTS refresh_token_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE refresh_token
(
    id          BIGINT NOT NULL,
    token       VARCHAR(255),
    users_id    BIGINT,
    expire_time TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_refreshtoken PRIMARY KEY (id)
);

ALTER TABLE refresh_token
    ADD CONSTRAINT FK_REFRESHTOKEN_ON_USERS FOREIGN KEY (users_id) REFERENCES users (id);