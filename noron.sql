DROP DATABASE IF EXISTS NoronDB;
CREATE DATABASE NoronDB;
USE NoronDB;

DROP TABLE IF EXISTS Users;
CREATE TABLE Users(
	id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    pass VARCHAR(50),
    email VARCHAR(50),
    phonenumber VARCHAR(50),
    createat DATE,
    updateat DATE,
    deleteat DATE
);

DROP TABLE IF EXISTS Post;
CREATE TABLE Post(
	id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(50),
    content VARCHAR(50),
    type_ VARCHAR(50),
    view_ INT,
    created_at DATE,
    update_at DATE,
    delete_at DATE,
    user_id INT,
    FOREIGN KEY(user_id) REFERENCES Users (id) 
);

DROP TABLE IF EXISTS Topic;
CREATE TABLE Topic(
	id INT PRIMARY KEY AUTO_INCREMENT,
    name_topic VARCHAR(50),
    create_at DATE,
    update_at DATE,
    delete_at DATE
);

DROP TABLE IF EXISTS Post_Topic;
CREATE TABLE Post_Topic(
	post_id INT NOT NULL,
    topic_id INT NOT NULL,
    FOREIGN KEY(post_id) REFERENCES Post(id),
    FOREIGN KEY(topic_id) REFERENCES Topic(id),
    PRIMARY KEY(post_id,topic_id)
);

DROP TABLE IF EXISTS Comments;
CREATE TABLE Comments(
	id INT PRIMARY KEY AUTO_INCREMENT,
    content VARCHAR(255),
    type_comment VARCHAR(50),
    parent_id INT,
    created_at DATE,
    update_at DATE,
    delete_at DATE,
    user_id INT,
    post_id INT,
    FOREIGN KEY(user_id) REFERENCES Users(id),
    FOREIGN KEY(post_id) REFERENCES Post(id)
);

DROP TABLE IF EXISTS Vote;
CREATE TABLE Vote(
	id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    create_at DATE,
    update_at DATE,
    delete_at DATE
);

DROP TABLE IF EXISTS Vote_Comment;
CREATE TABLE Vote_Comment(
	comment_id INT,
    vote_id INT,
    FOREIGN KEY(comment_id) REFERENCES Comments(id),
    FOREIGN KEY(vote_id) REFERENCES Vote(id),
    PRIMARY KEY(comment_id,vote_id)
);

DROP TABLE IF EXISTS Vote_Post;
CREATE TABLE Vote_Post(
	vote_id INT,
    post_id INT,
    FOREIGN KEY(vote_id) REFERENCES Vote(id),
    FOREIGN KEY(post_id) REFERENCES Post(id),
    PRIMARY KEY(vote_id,post_id)
);












