ALTER TABLE user AUTO_INCREMENT = 1;
INSERT INTO user(name,passwd) VALUES("admin","root");
INSERT INTO administrator VALUES(LAST_INSERT_ID());
INSERT INTO user(name,passwd) VALUES("user1","test");
INSERT INTO purchaser VALUES(LAST_INSERT_ID(),"男",0017);
INSERT INTO user(name,passwd) VALUES("user2","test");
INSERT INTO seller VALUES(LAST_INSERT_ID(),"简介简介简介简介简介","地址地址地址地址地址","菜菜菜菜菜菜",0);
ALTER TABLE user AUTO_INCREMENT = 100000;
ALTER TABLE orderOverview AUTO_INCREMENT = 10000;
ALTER TABLE Dish AUTO_INCREMENT = 10000;
INSERT INTO user(name,passwd) VALUES("肯德基","KFC");
INSERT INTO seller VALUES(LAST_INSERT_ID(),"Finger Lickin' Good","南区门口","全家桶",0);
INSERT INTO user(name,passwd) VALUES("喜茶","xicha");
INSERT INTO seller VALUES(LAST_INSERT_ID(),"喜悦发生","三号湾","多肉杨梅",0);