-- member 테이블
CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255),
    username VARCHAR(255),
    nick_name VARCHAR(255),
    role VARCHAR(50)
);

-- challenge 테이블
CREATE TABLE challenge (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id ENUM('HOBBY', 'SELF_IMPROVEMENT', 'SPORT', 'STUDY'),
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
    CONSTRAINT fk_host FOREIGN KEY (host_id) REFERENCES member(id)
);

-- participant 테이블
CREATE TABLE participant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    challenge_id BIGINT,
    user_id BIGINT,
    CONSTRAINT fk_challenge FOREIGN KEY (challenge_id) REFERENCES challenge(id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES member(id)
);

-- user_profile 테이블
CREATE TABLE user_profile (
    profile_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    user_nick_name VARCHAR(255),
    image_url VARCHAR(255),
    point INT,
    CONSTRAINT fk_user_profile FOREIGN KEY (user_id) REFERENCES member(id)
);
