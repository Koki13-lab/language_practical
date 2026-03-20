ALTER TABLE skills DROP CONSTRAINT skills_user_id_fkey;

ALTER TABLE skills
ADD CONSTRAINT skills_user_id_fkey
FOREIGN KEY (user_id)
REFERENCES users(id)
ON DELETE CASCADE;