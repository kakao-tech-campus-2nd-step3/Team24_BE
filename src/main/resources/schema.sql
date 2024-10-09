CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255),
    username VARCHAR(255),
    nick_name VARCHAR(255),
    role VARCHAR(50)
);

CREATE TABLE category (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255)
);

CREATE TABLE challenge (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id INT,
    host_id BIGINT,
    name VARCHAR(255),
    body TEXT,
    point INT,
    date DATE,
    start_time TIME,
    end_time TIME,
    image_url VARCHAR(255),
    min_participant_num INT,
    max_participant_num INT,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(id),
    CONSTRAINT fk_host FOREIGN KEY (host_id) REFERENCES member(id)
);

CREATE TABLE participant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    challenge_id BIGINT,
    user_id BIGINT,
    CONSTRAINT fk_challenge FOREIGN KEY (challenge_id) REFERENCES challenge(id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES member(id)
);

CREATE TABLE user_profile (
    profile_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    user_nick_name VARCHAR(255),
    image_url VARCHAR(255),
    point INT,
    CONSTRAINT fk_user_profile FOREIGN KEY (user_id) REFERENCES member(id)
);

CREATE TABLE refresh_token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT,
    token VARCHAR(255),
    expiration VARCHAR(255),
    CONSTRAINT fk_member FOREIGN KEY (member_id) REFERENCES member(id)
);
