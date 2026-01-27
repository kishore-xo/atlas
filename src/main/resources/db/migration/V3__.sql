CREATE SEQUENCE IF NOT EXISTS reset_password_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE reset_password
(
    id        BIGINT  NOT NULL,
    token     VARCHAR(255),
    expire_at TIMESTAMP WITHOUT TIME ZONE,
    used      BOOLEAN NOT NULL,
    users_id  BIGINT,
    CONSTRAINT pk_resetpassword PRIMARY KEY (id)
);

ALTER TABLE reset_password
    ADD CONSTRAINT FK_RESETPASSWORD_ON_USERS FOREIGN KEY (users_id) REFERENCES users (id);
