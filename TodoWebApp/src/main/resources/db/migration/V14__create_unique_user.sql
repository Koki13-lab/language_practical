CREATE UNIQUE INDEX uk_users_name
ON users (name)
WHERE deleted = false;