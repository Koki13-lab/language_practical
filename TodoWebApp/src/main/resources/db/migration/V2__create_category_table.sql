CREATE TABLE category (
	id SERIAL PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	sort_order INTEGER NOT NULL
);

INSERT INTO category (name, sort_order)
VALUES
('仕事', 1),('準備', 2),('学習', 3),('その他', 4);