CREATE TABLE users (
	id SERIAL PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	password VARCHAR(255) NOT NULL,
	mail VARCHAR(256) NOT NULL UNIQUE,
	role VARCHAR(20) NOT NULL
		CHECK (role IN ('ROLE_ADMIN', 'ROLE_USER', 'ROLE_TODO')),
	remarks VARCHAR(2000),

	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	created_by VARCHAR(50),

	updated_at TIMESTAMP,
	updated_by VARCHAR(50),

	deleted_at TIMESTAMP,
	deleted_by VARCHAR(50),

	is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

INSERT INTO users (name, password, mail, role)
VALUES ('admin', '$2a$10$mJcfMj4MMAFslbw.1LbsbeYOy7UiYJiuVNYI7Fa7zs7jQra13O5lu','itatyan17_130811@yahoo.co.jp','ROLE_ADMIN');