ALTER TABLE users
RENAME COLUMN is_deleted TO deleted;

ALTER TABLE todo
RENAME COLUMN is_deleted TO deleted;

ALTER TABLE user_todo
RENAME COLUMN is_deleted TO deleted;