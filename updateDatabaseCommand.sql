ALTER TABLE `gateway` DROP FOREIGN KEY `gateway_ibfk_1`, ADD constraint FOREIGN KEY (`field_id`) REFERENCES `field` (`id`) ON DELETE CASCADE;
ALTER TABLE `slot` DROP FOREIGN KEY `slot_ibfk_1`, ADD constraint FOREIGN KEY (`field_id`) REFERENCES `field` (`id`) ON DELETE CASCADE;
ALTER TABLE `detector` DROP FOREIGN KEY `detector_ibfk_1`, ADD constraint FOREIGN KEY (`slot_id`) REFERENCES `slot` (`id`) ON DELETE CASCADE;
ALTER TABLE `detector` DROP FOREIGN KEY `detector_ibfk_2`, ADD constraint FOREIGN KEY (`gateway_id`) REFERENCES `gateway` (`id`) ON DELETE CASCADE;
ALTER TABLE `tag` DROP FOREIGN KEY `tag_ibfk_1`, ADD constraint FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE;
ALTER TABLE `contract` DROP FOREIGN KEY `contract_ibfk_1`, ADD constraint FOREIGN KEY (`field_id`) REFERENCES `field` (`id`) ON DELETE CASCADE;
ALTER TABLE `contract` DROP FOREIGN KEY `contract_ibfk_2`, ADD constraint FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE;
ALTER TABLE `manager_field` DROP FOREIGN KEY `manager_field_ibfk_1`, ADD constraint FOREIGN KEY (`field_id`) REFERENCES `field` (`id`) ON DELETE CASCADE;
ALTER TABLE `manager_field` DROP FOREIGN KEY `manager_field_ibfk_2`, ADD constraint FOREIGN KEY (`manager_id`) REFERENCES `manager` (`id`) ON DELETE CASCADE;

CREATE TABLE stats_field(
                      id int NOT NULL AUTO_INCREMENT,
                      field_id int not null,
                      day datetime not null,
                      freq int,
                      cost int,
                      primary key (id),
                      UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;