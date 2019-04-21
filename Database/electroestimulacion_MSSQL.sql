-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2019-04-06 14:50:16.634

-- tables
-- Table: body_parts
CREATE TABLE body_parts (
    body_part_id int  NOT NULL,
    name int  NOT NULL,
    image_id int  NOT NULL,
    updated_at datetime  NOT NULL,
    updated_by int  NOT NULL,
    CONSTRAINT body_parts_pk PRIMARY KEY  (body_part_id)
);

-- Table: domains
CREATE TABLE domains (
    domain_name varchar(100)  NOT NULL,
    value int  NOT NULL,
    description varchar(500)  NOT NULL,
    updated_at datetime  NOT NULL,
    updated_by int  NOT NULL,
    CONSTRAINT domains_pk PRIMARY KEY  (domain_name,value)
);

-- Table: images
CREATE TABLE images (
    image_id int  NOT NULL,
    image_archive image  NOT NULL,
    title varchar(100)  NOT NULL,
    updated_at datetime  NOT NULL,
    updated_by int  NOT NULL,
    CONSTRAINT images_pk PRIMARY KEY  (image_id)
);

-- Table: injuries
CREATE TABLE injuries (
    injury_id int  NOT NULL,
    name varchar(100)  NOT NULL,
    description varchar(1000)  NOT NULL,
    updated_at datetime  NOT NULL,
    updated_by int  NOT NULL,
    CONSTRAINT injuries_pk PRIMARY KEY  (injury_id)
);

-- Table: treatments
CREATE TABLE treatments (
    treatment_id int  NOT NULL,
    name int  NOT NULL,
    description int  NOT NULL,
    updated_at datetime  NOT NULL,
    updated_by int  NOT NULL,
    CONSTRAINT treatments_pk PRIMARY KEY  (treatment_id)
);

-- Table: treatments_waves
CREATE TABLE treatments_waves (
    wave_id int  NOT NULL,
    treatment_id int  NOT NULL,
    updated_at datetime  NOT NULL,
    updated_by int  NOT NULL,
    CONSTRAINT treatments_waves_pk PRIMARY KEY  (wave_id,treatment_id)
);

-- Table: user_passwords
CREATE TABLE user_passwords (
    password varchar(100)  NOT NULL,
    user_id int  NOT NULL,
    updated_at datetime  NOT NULL,
    updated_by int  NOT NULL,
    CONSTRAINT user_passwords_pk PRIMARY KEY  (password,user_id)
);

-- Table: users
CREATE TABLE users (
    user_id int  NOT NULL,
    user_account varchar(100)  NOT NULL,
    first_name varchar(100)  NOT NULL,
    last_name varchar(100)  NOT NULL,
    email int  NOT NULL,
    phone varchar(100)  NOT NULL,
    birthdate date  NOT NULL,
    kind int  NOT NULL,
    updated_at datetime  NOT NULL,
    updated_by int  NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY  (user_id)
);

-- Table: users_injuries
CREATE TABLE users_injuries (
    user_id int  NOT NULL,
    injury_id int  NOT NULL,
    injurie_date datetime  NOT NULL,
    body_part_id int  NOT NULL,
    updated_at datetime  NOT NULL,
    updated_by int  NOT NULL,
    CONSTRAINT users_injuries_pk PRIMARY KEY  (user_id,injury_id,injurie_date,body_part_id)
);

-- Table: users_treatments
CREATE TABLE users_treatments (
    user_id int  NOT NULL,
    treatment_id int  NOT NULL,
    body_part_id int  NOT NULL,
    updated_at datetime  NOT NULL,
    updated_by int  NOT NULL,
    CONSTRAINT users_treatments_pk PRIMARY KEY  (user_id,treatment_id)
);

-- Table: waves
CREATE TABLE waves (
    wave_id int  NOT NULL,
    name varchar(100)  NOT NULL,
    kind int  NOT NULL,
    updated_at datetime  NOT NULL,
    updated_by int  NOT NULL,
    CONSTRAINT waves_pk PRIMARY KEY  (wave_id)
);

-- foreign keys
-- Reference: body_parts_images (table: body_parts)
ALTER TABLE body_parts ADD CONSTRAINT body_parts_images
    FOREIGN KEY (image_id)
    REFERENCES images (image_id);

-- Reference: treatments_waves_treatments (table: treatments_waves)
ALTER TABLE treatments_waves ADD CONSTRAINT treatments_waves_treatments
    FOREIGN KEY (treatment_id)
    REFERENCES treatments (treatment_id);

-- Reference: treatments_waves_waves (table: treatments_waves)
ALTER TABLE treatments_waves ADD CONSTRAINT treatments_waves_waves
    FOREIGN KEY (wave_id)
    REFERENCES waves (wave_id);

-- Reference: user_passwords_users (table: user_passwords)
ALTER TABLE user_passwords ADD CONSTRAINT user_passwords_users
    FOREIGN KEY (user_id)
    REFERENCES users (user_id);

-- Reference: users_injuries_body_parts (table: users_injuries)
ALTER TABLE users_injuries ADD CONSTRAINT users_injuries_body_parts
    FOREIGN KEY (body_part_id)
    REFERENCES body_parts (body_part_id);

-- Reference: users_injuries_injuries (table: users_injuries)
ALTER TABLE users_injuries ADD CONSTRAINT users_injuries_injuries
    FOREIGN KEY (injury_id)
    REFERENCES injuries (injury_id);

-- Reference: users_injuries_users (table: users_injuries)
ALTER TABLE users_injuries ADD CONSTRAINT users_injuries_users
    FOREIGN KEY (user_id)
    REFERENCES users (user_id);

-- Reference: users_treatments_body_parts (table: users_treatments)
ALTER TABLE users_treatments ADD CONSTRAINT users_treatments_body_parts
    FOREIGN KEY (body_part_id)
    REFERENCES body_parts (body_part_id);

-- Reference: users_treatments_treatments (table: users_treatments)
ALTER TABLE users_treatments ADD CONSTRAINT users_treatments_treatments
    FOREIGN KEY (treatment_id)
    REFERENCES treatments (treatment_id);

-- Reference: users_treatments_users (table: users_treatments)
ALTER TABLE users_treatments ADD CONSTRAINT users_treatments_users
    FOREIGN KEY (user_id)
    REFERENCES users (user_id);

-- End of file.

