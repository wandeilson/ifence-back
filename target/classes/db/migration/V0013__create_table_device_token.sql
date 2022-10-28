CREATE TABLE IF NOT EXISTS device_token(
	id BIGSERIAL,
	token VARCHAR(255),
	user_id BIGINT,
	
	CONSTRAINT device_id_pk PRIMARY KEY (id),
	CONSTRAINT user_id_fk_user_roles FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
