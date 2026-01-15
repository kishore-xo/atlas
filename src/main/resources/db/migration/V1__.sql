CREATE SEQUENCE IF NOT EXISTS comments_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS task_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS workspace_members_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS workspace_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE comments
(
    id         BIGINT NOT NULL,
    content    VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id    BIGINT,
    task_id    BIGINT,
    CONSTRAINT pk_comments PRIMARY KEY (id)
);

CREATE TABLE task
(
    id           BIGINT       NOT NULL,
    title        VARCHAR(255) NOT NULL,
    description  VARCHAR(255) NOT NULL,
    status       VARCHAR(255),
    workspace_id BIGINT,
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_task PRIMARY KEY (id)
);

CREATE TABLE users
(
    id         BIGINT       NOT NULL,
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    role       VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE workspace
(
    id         BIGINT       NOT NULL,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_workspace PRIMARY KEY (id)
);

CREATE TABLE workspace_members
(
    id           BIGINT NOT NULL,
    user_id      BIGINT NOT NULL,
    workspace_id BIGINT NOT NULL,
    role         VARCHAR(255),
    created_at   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_workspacemembers PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE comments
    ADD CONSTRAINT FK_COMMENTS_ON_TASK FOREIGN KEY (task_id) REFERENCES task (id);

ALTER TABLE comments
    ADD CONSTRAINT FK_COMMENTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE task
    ADD CONSTRAINT FK_TASK_ON_WORKSPACE FOREIGN KEY (workspace_id) REFERENCES workspace (id);

ALTER TABLE workspace_members
    ADD CONSTRAINT FK_WORKSPACEMEMBERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE workspace_members
    ADD CONSTRAINT FK_WORKSPACEMEMBERS_ON_WORKSPACE FOREIGN KEY (workspace_id) REFERENCES workspace (id);