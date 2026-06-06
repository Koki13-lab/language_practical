ALTER TABLE todo
	ALTER COLUMN created_by TYPE INTEGER USING created_by::integer,
	ALTER COLUMN created_by SET NOT NULL,
	ALTER COLUMN updated_by TYPE INTEGER USING created_by::integer,
	ALTER COLUMN deleted_by TYPE INTEGER USING created_by::integer;