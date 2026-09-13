CREATE TABLE todo (
	id SERIAL PRIMARY KEY,
	title VARCHAR(50) NOT NULL,
	priority INT,
	due_date DATE,
	category_id INT,
	content VARCHAR(2000),

	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	created_by VARCHAR(50),

	updated_at TIMESTAMP,
	updated_by VARCHAR(50),

	deleted_at TIMESTAMP,
	deleted_by VARCHAR(50),

	is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
	
	FOREIGN KEY (category_id)
	REFERENCES category(id)
	ON DELETE RESTRICT
);