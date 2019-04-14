INSERT INTO employee(first_name,last_name) values ('ben','johnson');
INSERT INTO employee(first_name,last_name) values ('tina','munim');
INSERT INTO employee(first_name,last_name) values ('jack','jones');

INSERT INTO tweet(entry,hashtag,employee_id) values ('Ni Hao','greeting',1);
INSERT INTO tweet(entry,hashtag,employee_id) values ('Hola','greeting',1);
INSERT INTO tweet(entry,hashtag,employee_id) values ('Nice day','greeting',1);
INSERT INTO tweet(entry,hashtag,employee_id) values ('Good weather','greeting',2);
INSERT INTO tweet(entry,hashtag,employee_id) values ('Thanks','greeting',2);
INSERT INTO tweet(entry,hashtag,employee_id) values ('Hello','greeting',2);
INSERT INTO tweet(entry,hashtag,employee_id) values ('Good Morning','greeting',3);
INSERT INTO tweet(entry,hashtag,employee_id) values ('How are you?','greeting',3);
INSERT INTO tweet(entry,hashtag,employee_id) values ('Good Night','greeting',3);
INSERT INTO tweet(entry,hashtag,employee_id) values ('Chao','greeting',3);

INSERT INTO follower(employee_id,follower_id) values(1,3);
INSERT INTO follower(employee_id,follower_id) values(1,2);
INSERT INTO follower(employee_id,follower_id) values(2,3);