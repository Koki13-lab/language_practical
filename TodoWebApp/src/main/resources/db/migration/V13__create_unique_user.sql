CREATE UNIQUE INDEX uk_users_mail
ON users (mail)
WHERE deleted = false;