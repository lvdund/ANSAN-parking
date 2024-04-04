SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE field(
	id int NOT NULL AUTO_INCREMENT,
    name varchar(120) not null,
    latitude varchar(120) not null,
    longitude varchar(120) not null,
    address varchar(120) not null,
    image varchar(200) default null,
    price double not null,
    openstatus varchar(1) not null,
    space decimal(10,1) not null,
    details longtext not null,
    primary key (id),
    UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE gateway(
	id int NOT NULL AUTO_INCREMENT,
	field_id int not null,
    address_gateway varchar(20) not null,
    primary key (id),
    UNIQUE KEY `id_UNIQUE` (`id`),
    CONSTRAINT FOREIGN KEY (`field_id`) REFERENCES `field` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE slot(
	id int NOT NULL,
    field_id int not null,
    status_detector bit(1),
    status_cam bit(1),
    primary key (id),
    UNIQUE KEY `id_UNIQUE` (`id`),
    CONSTRAINT FOREIGN KEY (`field_id`) REFERENCES `field` (`id`) ON DELETE CASCADE
) ENGINE=INNODB CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE detector(
	id int NOT NULL,
    address_detector varchar(20) not null,
    slot_id int not null,
    gateway_id int not null,
    battery_level varchar(20) not null,
    communication_level varchar(20) not null,
    last_time_update datetime not null,
    last_time_setup datetime not null,
    primary key (id),
    UNIQUE KEY `id_UNIQUE` (`id`),
    CONSTRAINT FOREIGN KEY (`slot_id`) REFERENCES `slot` (`id`) ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (`gateway_id`) REFERENCES `gateway` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE user(
	id int NOT NULL AUTO_INCREMENT,
    pass varchar(256) not null,
    id_number int not null,
    email varchar(40) not null,
	equipment varchar(20) not null,
    address varchar(120) not null,
    phone varchar(45) not null,
    last_time_access datetime,
	image varchar(200) default null,
    sex varchar(1) not null,
    birth date not null,
    primary key (id),
    UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tag(
	id int NOT NULL AUTO_INCREMENT,
    user_id int not null,
	time_car_in datetime,
    time_car_out datetime,
    primary key (id),
    UNIQUE KEY `id_UNIQUE` (`id`),
    CONSTRAINT FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE manager(
	id int NOT NULL AUTO_INCREMENT,
    email varchar(40) not null,
    pass varchar(256) not null,
    last_time_access datetime,
    acp bit(1) not null,
    primary key (id),
    UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE contract(
	id int NOT NULL AUTO_INCREMENT,
    field_id int not null,
    user_id int not null,
    time_car_in datetime,
    time_car_out datetime,
    time_in_book datetime not null,
    time_out_book datetime not null,
    car_number varchar(45) not null,
    dt_create datetime not null,
    cost varchar(45) not null,
    status varchar(1) not null, 
    primary key (id),
    UNIQUE KEY `id_UNIQUE` (`id`),
    CONSTRAINT FOREIGN KEY (`field_id`) REFERENCES `field` (`id`) ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE manager_field(
	id int NOT NULL AUTO_INCREMENT,
    field_id int not null,
    manager_id int not null,
    last_time_setup datetime not null,
    primary key (id),
    UNIQUE KEY `id_UNIQUE` (`id`),
	CONSTRAINT FOREIGN KEY (`field_id`) REFERENCES `field` (`id`) ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (`manager_id`) REFERENCES `manager` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE admin(
	id int NOT NULL AUTO_INCREMENT,
    email varchar(40) not null,
    pass varchar(256) not null,
    primary key (id),
    UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE black_list(
	id int NOT NULL AUTO_INCREMENT,
    token varchar(256) not null,
    primary key (id),
    UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE package(
	id int not null AUTO_INCREMENT,
	packet_number varchar(20) not null,
    id_node varchar(20) not null,
	battery_level varchar(20) not null,
    node_address varchar(20) not null,
    state bit(1) not null,
    communication_level varchar(20) not null,
    time varchar(20) not null,
	location varchar(20) not null,
    primary key (id),
    UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE data_cam_and_detector(
	id int not null AUTO_INCREMENT,
	slot_id int not null,
    status_detector bit(1),
    status_cam bit(1),
    time datetime not null,
    primary key (id),
    UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE verify_table(
	code varchar(20) not null,
	id int NOT NULL AUTO_INCREMENT,
    pass varchar(256) not null,
    id_number int not null,
    email varchar(40) not null,
	equipment varchar(20) not null,
    address varchar(120) not null,
    phone varchar(45) not null,
    last_time_access datetime,
	image varchar(200) default null,
    sex varchar(1) not null,
    birth date not null,
    primary key (id),
    UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE area (
    id INT NOT NULL AUTO_INCREMENT,
    area_name nvarchar(256) NOT NULL,
    id_district INT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY `id_UNIQUE` (`id`),
    CONSTRAINT FOREIGN KEY (`id_district`) REFERENCES `district` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE district (
    id INT NOT NULL AUTO_INCREMENT,
    district nvarchar(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE code_reset_pass(
	id int not null AUTO_INCREMENT,
	email varchar(40) not null,
    code varchar(20) not null,
    primary key (id),
    UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

BEGIN;
INSERT INTO `parking_space_2021`.`admin` (`id`, `email`, `pass`) VALUES ('0', 'admin@gmail.com', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918');

INSERT INTO `field` VALUES ('1', 'C9', '20.960377427559497', '105.79658800934396', "Đại học BKHN", "", '50000', 'O', 50, "Bãi đồ xe C9");
INSERT INTO `field` VALUES ('2', 'C3', '21.0066272', '105.8416806', "Đại học BKHN", "", '50000', 'O', 40, "Bãi đồ xe C3");
INSERT INTO `field` VALUES ('3', '17 Phùng Hưng, Hà Đông - Hà Nội', 'Hà Nội', '', '20.960377427559497', '105.79658800934396', '50000', 'O', 15, 'id, image, address, lat, lon, location, price, distance, openstatus, space, details');
INSERT INTO `field` VALUES ('4', 'Điểm đỗ xe sau toà nhà Viglacera 671 Hoàng Hoa Thám', 'Hà Nội', '', '21.045006923839967', '105.80782829707834', '50000', 'O', 15, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Bibendum est ultricies integer quis. Iaculis urna id volutpat lacus laoreet. Mauris vitae ultricies leo integer malesuada. Ac odio tempor orci dapibus ultrices in. Egestas diam in arcu cursus euismod. Dictum fusce ut');
INSERT INTO `field` VALUES ('5', 'Điểm đỗ xe Nguyễn Đình Chiểu', 'Hà Nội', '', '21.01916918502266', '105.84623814002637', '50000', 'O', 16, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Bibendum est ultricies integer quis. Iaculis urna id volutpat lacus laoreet. Mauris vitae ultricies leo integer malesuada. Ac odio tempor orci dapibus ultrices in. Egestas diam in arcu cursus euismod. Dictum fusce ut');
INSERT INTO `field` VALUES ('6', 'Điểm đỗ xe Hà Thành plaza', 'Hà Nội', '', '21.024340036520023', '105.82015308050798', '50000', 'O', 2, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Bibendum est ultricies integer quis. Iaculis urna id volutpat lacus laoreet. Mauris vitae ultricies leo integer malesuada. Ac odio tempor orci dapibus ultrices in. Egestas diam in arcu cursus euismod. Dictum fusce ut');
INSERT INTO `field` VALUES ('7', 'Điểm đỗ xe Đại học Y Hà Nội, số 1 Tôn Thất Tùng', 'Hà Nội', '', '21.03347234978806', '105.83061729584946', '50000', 'O', 45, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Bibendum est ultricies integer quis. Iaculis urna id volutpat lacus laoreet. Mauris vitae ultricies leo integer malesuada. Ac odio tempor orci dapibus ultrices in. Egestas diam in arcu cursus euismod. Dictum fusce ut');
INSERT INTO `field` VALUES ('8', 'Điểm đỗ xe Khách sạn Phụ nữ – Số 10 Thụy Khuê', 'Hà Nội', '', '21.048472303387822', '105.81253402653292', '50000', 'O', 15, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Bibendum est ultricies integer quis. Iaculis urna id volutpat lacus laoreet. Mauris vitae ultricies leo integer malesuada. Ac odio tempor orci dapibus ultrices in. Egestas diam in arcu cursus euismod. Dictum fusce ut');
INSERT INTO `field` VALUES ('9', 'Điểm đô xe Tòa nhà Syrena', 'Hà Nội', '', '21.012966931751308', '105.82812847071014', '50000', 'O', 35, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Bibendum est ultricies integer quis. Iaculis urna id volutpat lacus laoreet. Mauris vitae ultricies leo integer malesuada. Ac odio tempor orci dapibus ultrices in. Egestas diam in arcu cursus euismod. Dictum fusce ut');
INSERT INTO `field` VALUES ('10', 'Điểm đỗ xe Khách sạn Sheraton', 'Hà Nội', '', '21.00897946128564', '105.83171034187457', '50000', 'O', 34, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Bibendum est ultricies integer quis. Iaculis urna id volutpat lacus laoreet. Mauris vitae ultricies leo integer malesuada. Ac odio tempor orci dapibus ultrices in. Egestas diam in arcu cursus euismod. Dictum fusce ut');
INSERT INTO `field` VALUES ('11', 'Điểm đỗ xe Văn Phú', 'Hà Nội', '', '21.003471770161585', '105.7658958839578', '50000', 'O', 12, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Bibendum est ultricies integer quis. Iaculis urna id volutpat lacus laoreet. Mauris vitae ultricies leo integer malesuada. Ac odio tempor orci dapibus ultrices in. Egestas diam in arcu cursus euismod. Dictum fusce ut');
INSERT INTO `field` VALUES ('12', 'Điểm đỗ xe Quang Trung, Ngã 4 Trần hưng đạo đến Tràng thi, 24/24h', 'Hà Nội', '', '21.046715461089097', '105.85934545536792', '50000', 'O', 20, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Bibendum est ultricies integer quis. Iaculis urna id volutpat lacus laoreet. Mauris vitae ultricies leo integer malesuada. Ac odio tempor orci dapibus ultrices in. Egestas diam in arcu cursus euismod. Dictum fusce ut');
INSERT INTO `field` VALUES ('13', 'Điểm đỗ xe nhà hát lớn, Phố Cổ Tân', 'Hà Nội', '', '21.064494902781068', '105.85787149584964', '50000', 'O', 21, '. Chỉ trông xe đến 10h đêm.');
INSERT INTO `field` VALUES ('14', 'Điểm đỗ xe Vạn Phúc, Ba Đình', 'Hà Nội', '', '21.060184939684095', '105.81693711119136', '50000', 'O', 42, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Bibendum est ultricies integer quis. Iaculis urna id volutpat lacus laoreet. Mauris vitae ultricies leo integer malesuada. Ac odio tempor orci dapibus ultrices in. Egestas diam in arcu cursus euismod. Dictum fusce ut');

INSERT INTO `gateway` VALUES("1","1","255.255.0.0");
INSERT INTO `gateway` VALUES("2","2","255.255.0.8");

INSERT INTO `parking_space_2021`.`user` (`id`, `pass`, `id_number`, `email`, `equipment`, `address`, `phone`, `last_time_access`, `image`, `sex`, `birth`) VALUES ('1', '0a041b9462caa4a31bac3567e0b6e6fd9100787db2ab433d96f6d178cabfce90', '0', 'user1@gmail.com', 'string', 'string', 'string', '2021-04-29 00:40:00', 'string', 'n', '2020-10-10');
INSERT INTO `parking_space_2021`.`user` (`id`, `pass`, `id_number`, `email`, `equipment`, `address`, `phone`, `last_time_access`, `image`, `sex`, `birth`) VALUES ('2', '6025d18fe48abd45168528f18a82e265dd98d421a7084aa09f61b341703901a3', '0', 'user2@gmail.com', 'string', 'string', 'string', '2021-04-29 00:40:00', 'string', 'n', '2020-10-10');
INSERT INTO `parking_space_2021`.`user` (`id`, `pass`, `id_number`, `email`, `equipment`, `address`, `phone`, `last_time_access`, `image`, `sex`, `birth`) VALUES ('3', '5860faf02b6bc6222ba5aca523560f0e364ccd8b67bee486fe8bf7c01d492ccb', '0', 'user3@gmail.com', 'string', 'string', 'string', '2021-04-29 00:40:00', 'string', 'n', '2020-10-10');

-- District Insert
INSERT INTO `parking_space_2021`.`district` (`id`, `district`) VALUES ('1', 'Ba Đình');
INSERT INTO `parking_space_2021`.`district` (`id`, `district`) VALUES ('2', 'Bắc Từ Liêm');
INSERT INTO `parking_space_2021`.`district` (`id`, `district`) VALUES ('3', 'Cầu Giấy');
INSERT INTO `parking_space_2021`.`district` (`id`, `district`) VALUES ('4', 'Đống Đa');
INSERT INTO `parking_space_2021`.`district` (`id`, `district`) VALUES ('5', 'Hai Bà Trưng');
INSERT INTO `parking_space_2021`.`district` (`id`, `district`) VALUES ('6', 'Hà Đông');
INSERT INTO `parking_space_2021`.`district` (`id`, `district`) VALUES ('7', 'Hoàn Kiếm');
INSERT INTO `parking_space_2021`.`district` (`id`, `district`) VALUES ('8', 'Hoàng Mai');
INSERT INTO `parking_space_2021`.`district` (`id`, `district`) VALUES ('9', 'Long Biên');
INSERT INTO `parking_space_2021`.`district` (`id`, `district`) VALUES ('10', 'Nam Từ Liêm');
INSERT INTO `parking_space_2021`.`district` (`id`, `district`) VALUES ('11', 'Tây Hồ');
INSERT INTO `parking_space_2021`.`district` (`id`, `district`) VALUES ('12', 'Thanh Xuân');

-- Area Insert
INSERT INTO `parking_space_2021`.`area` (`id`, `area_name`, `id_district`) VALUES ('1', 'Trường Đại học Bách Khoa Hà Nội', '5');
INSERT INTO `parking_space_2021`.`area` (`id`, `area_name`, `id_district`) VALUES ('2', 'Phường Ngã Tư Sở', '4');
INSERT INTO `parking_space_2021`.`area` (`id`, `area_name`, `id_district`) VALUES ('3', 'Phường Nguyễn Du & Lê Đại Hành', '5');
INSERT INTO `parking_space_2021`.`area` (`id`, `area_name`, `id_district`) VALUES ('4', 'Phường Phúc La', '6');
INSERT INTO `parking_space_2021`.`area` (`id`, `area_name`, `id_district`) VALUES ('5', 'Phường Ngọc Hà & Cống Vị', '1');
INSERT INTO `parking_space_2021`.`area` (`id`, `area_name`, `id_district`) VALUES ('6', 'Phường Trung Tự', '4');
INSERT INTO `parking_space_2021`.`area` (`id`, `area_name`, `id_district`) VALUES ('7', 'Phường Thụy Khuê', '1');
INSERT INTO `parking_space_2021`.`area` (`id`, `area_name`, `id_district`) VALUES ('8', 'Phường Quảng An', '11');
INSERT INTO `parking_space_2021`.`area` (`id`, `area_name`, `id_district`) VALUES ('9', 'Khu đô thị Văn Phú', '6');
INSERT INTO `parking_space_2021`.`area` (`id`, `area_name`, `id_district`) VALUES ('10', 'Phường Trần Hưng Đạo', '7');
INSERT INTO `parking_space_2021`.`area` (`id`, `area_name`, `id_district`) VALUES ('11', 'Phường Tràng Tiền', '7');
INSERT INTO `parking_space_2021`.`area` (`id`, `area_name`, `id_district`) VALUES ('12', 'Phường Liễu Giai', '1');



ALTER TABLE tag MODIFY id varchar(50);

-- Update detector table -- 13/11
ALTER TABLE detector
MODIFY gateway_id int not null,
MODIFY battery_level varchar(20),
MODIFY communication_level varchar(20),
MODIFY last_time_update datetime;

-- after alter table detector
--INSERT INTO `parking_space_2021`.`slot` (`field_id`, `status_detector`, `status_cam`) VALUES (1, 1, 1);
--INSERT INTO `parking_space_2021`.`slot` (`field_id`, `status_detector`, `status_cam`) VALUES (2, 1, 0);
--INSERT INTO `parking_space_2021`.`slot` (`field_id`, `status_detector`, `status_cam`) VALUES (3, 0, 1);



ALTER TABLE manager
ADD address varchar(120),
ADD	phone varchar(45),
ADD image varchar(200) default null,
ADD sex varchar(1),
ADD birth date,
ADD id_number int;


ALTER TABLE field
MODIFY latitude varchar(120),
MODIFY longitude varchar(120);



-- phai init data trong be xong ms dc chay cac dong ben duoi
-- INSERT INTO `detector` VALUES ("0","255.255.0.100","22","1","015","Communication Level","20210422123247","2021-04-22 12:25:54");
-- INSERT INTO `detector` VALUES ("1","255.255.0.101","23","1","015","Communication Level","20210422123247","2021-04-22 13:25:54");
-- INSERT INTO `detector` VALUES ("2","255.255.0.102","24","1","015","Communication Level","2021-04-22 14:32:47","2021-04-22 14:25:54");
-- INSERT INTO `detector` VALUES ("3","255.255.0.103","25","1","015","Communication Level","2021-04-22 15:32:47","2021-04-22 15:25:54");
-- INSERT INTO `detector` VALUES ("4","255.255.0.104","26","1","015","Communication Level","2021-04-22 16:32:47","2021-04-22 16:25:54");
-- INSERT INTO `detector` VALUES ("5","255.255.0.105","27","1","015","Communication Level","2021-04-22 17:32:47","2021-04-22 17:25:54");
-- INSERT INTO `detector` VALUES ("6","255.255.0.106","28","1","015","Communication Level","2021-04-22 18:32:47","2021-04-22 18:25:54");
-- INSERT INTO `detector` VALUES ("7","255.255.0.107","29","1","015","Communication Level","2021-04-22 19:32:47","2021-04-22 19:25:54");
-- INSERT INTO `detector` VALUES ("8","255.255.0.108","30","1","015","Communication Level","2021-04-22 20:32:47","2021-04-22 20:25:54");
-- INSERT INTO `detector` VALUES ("9","255.255.0.109","31","1","015","Communication Level","2021-04-22 21:32:47","2021-04-22 21:25:54");


INSERT INTO `manager` VALUES (1,'man1@gmail.com','58e02d8635563de1a93c43573ef8af294e96e023bb67adccf4d97b4b95b41bee','2021-11-09 16:59:22',_binary '','Số 1 Đại Cồ Việt','0123456789','url avatar','M','1990-10-10',123);
INSERT INTO `manager` VALUES (2,'man@gmail.com','48b676e2b107da679512b793d5fd4cc4329f0c7c17a97cf6e0e3d1005b600b03','2021-11-09 17:13:54',_binary '',NULL,NULL,NULL,NULL,NULL,0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;