SET NAMES utf8;
SET foreign_key_checks = 0;
SET time_zone = 'SYSTEM';
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

DROP TABLE IF EXISTS `Driver`;
CREATE TABLE `Driver` (
  `driver_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `street` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `city` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `full_name` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `zip` int(10) unsigned NOT NULL,
  PRIMARY KEY (`driver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


DROP TABLE IF EXISTS `Requests`;
CREATE TABLE `Requests` (
  `request_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `vehicle_id` int(10) unsigned NOT NULL,
  `driver_id` int(10) unsigned NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `status` tinyint(3) unsigned NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  PRIMARY KEY (`request_id`),
  KEY `vehicle_id` (`vehicle_id`),
  KEY `driver_id` (`driver_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `Requests_ibfk_1` FOREIGN KEY (`vehicle_id`) REFERENCES `Vehicles` (`vehicle_id`),
  CONSTRAINT `Requests_ibfk_2` FOREIGN KEY (`driver_id`) REFERENCES `Driver` (`driver_id`),
  CONSTRAINT `Requests_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `User` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
  `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `is_admin` bit(1) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


DROP TABLE IF EXISTS `Vehicles`;
CREATE TABLE `Vehicles` (
  `vehicle_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `make` text COLLATE utf8_unicode_ci NOT NULL,
  `model` text COLLATE utf8_unicode_ci NOT NULL,
  `license` text COLLATE utf8_unicode_ci NOT NULL,
  `state` tinyint(3) unsigned NOT NULL,
  `year` int(10) unsigned NOT NULL,
  PRIMARY KEY (`vehicle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;