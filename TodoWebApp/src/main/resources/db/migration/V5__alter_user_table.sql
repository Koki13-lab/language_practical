UPDATE users
	SET created_by = 1
	WHERE id=1;

ALTER TABLE users
	ALTER COLUMN created_by TYPE INTEGER USING created_by::integer,
	ALTER COLUMN created_by SET NOT NULL,
	ALTER COLUMN updated_by TYPE INTEGER USING created_by::integer,
	ALTER COLUMN deleted_by TYPE INTEGER USING created_by::integer;