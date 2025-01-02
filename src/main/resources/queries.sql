CREATE TABLE IF NOT EXISTS users
(
    id    SERIAL PRIMARY KEY,          -- Уникальный идентификатор пользователя
    name  VARCHAR(100) NOT NULL,       -- Имя пользователя
    email VARCHAR(100) NOT NULL UNIQUE -- Электронная почта пользователя
);

INSERT INTO users (name, email)
VALUES ('John Doe', 'johndoe@example.com'),
       ('Alice Smith', 'alice.smith@example.com'),
       ('Bob Johnson', 'bob.johnson@example.com'),
       ('Emily Davis', 'emily.davis@example.com');

CREATE TABLE IF NOT EXISTS posts
(
    id      SERIAL PRIMARY KEY,                                    -- Уникальный идентификатор поста
    title   VARCHAR(200) NOT NULL,                                 -- Заголовок поста
    content TEXT         NOT NULL,                                 -- Содержимое поста
    user_id INT          NOT NULL,                                 -- Внешний ключ на таблицу users
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE -- Связь с пользователем
);

INSERT INTO posts (title, content, user_id)
VALUES ('First Post', 'This is the first post content', 1),
       ('Second Post', 'This is the second post content', 1),
       ('Post by Alice', 'Alice post content', 2),
       ('Post by Bob', 'Bob post content', 3),
       ('Post by Emily', 'Emily post content', 4),
       ('Another Post by Alice', 'Another post by Alice content', 2);
